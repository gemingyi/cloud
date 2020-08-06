package com.example.userserver.es;

import com.example.userserver.es.model.BookArticle;
import com.example.userserver.es.model.BookChapter;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ESCURDTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void createIndex() {
        // 创建索引，会根据Item类的@Document注解信息来创建
        elasticsearchTemplate.createIndex(BookChapter.class);
        // 配置映射，会根据Item类中的id、Field等字段来自动完成映射
        elasticsearchTemplate.putMapping(BookChapter.class);
    }

    @Test
    public void InsertData() {
        for (int i = 1; i < 11; i++) {
            BookChapter bookChapter = new BookChapter();
            bookChapter.setId(i);
            bookChapter.setArticleTotal(2);
            bookChapter.setAuthor("作者" + i % 2);
            bookChapter.setTitle("这是第" + i + "章节");
            bookChapter.setDesc("这是第" + i + "章节的描述");
            List<BookArticle> bookArticleList = new ArrayList<>();
            for (int j = 1; j < 3; j++) {
                BookArticle bookArticle = new BookArticle();
                bookArticle.setId(j);
                bookArticle.setTitle("这是标题" + j);
                bookArticle.setPublishDate(new Date());
                bookArticle.setContent("这是内容" + j);
                bookArticleList.add(bookArticle);
            }
            bookChapter.setBookArticleList(bookArticleList);

            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(String.valueOf(bookChapter.getId()))
                    .withObject(bookChapter)
                    .build();
            elasticsearchTemplate.index(indexQuery);
        }
    }


    @Test
    public void testSearch() {
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//
//        QueryBuilder queryBuilder = QueryBuilders.termQuery("author", "作者1");
//        boolQueryBuilder.must().add(queryBuilder);

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("author", "作者5"))
                .must(new QueryStringQueryBuilder("5章节的描述").field("desc"))
                .must(QueryBuilders.nestedQuery("bookArticleList", QueryBuilders.termQuery("bookArticleList.title", "这是标题1"), ScoreMode.Avg))
                .filter(QueryBuilders.termQuery("id", "5"))
        ).build();

//        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery()
//                .must(QueryBuilders.matchPhraseQuery("desc", "是第5章节的"))).build();

        List<BookChapter> list = elasticsearchTemplate.queryForList(searchQuery, BookChapter.class);
        System.out.println(list);
    }


    @Test
    public void testSearch2() {
        //分组
        TermsAggregationBuilder termsAggregationBuilder=  AggregationBuilders.terms("group_author").field("author")
                //平均
                .subAggregation(AggregationBuilders.avg("avgArticleTotal").field("articleTotal"));
        SearchQuery searchQuery = new NativeSearchQueryBuilder().addAggregation(termsAggregationBuilder).build();

        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
        System.out.println(aggregations);
        StringTerms teamAgg = (StringTerms) aggregations.asMap().get("group_author");
        List<StringTerms.Bucket> bucketList = teamAgg.getBuckets();
        for (StringTerms.Bucket bucket : bucketList) {
            String key = bucket.getKeyAsString();
            long num = bucket.getDocCount();
            System.out.println(String.format("key:[%s], value:[%s]", key, num));

            InternalAvg avg = (InternalAvg)  bucket.getAggregations().asMap().get("avgArticleTotal");
            System.out.println(avg);
        }
    }

}

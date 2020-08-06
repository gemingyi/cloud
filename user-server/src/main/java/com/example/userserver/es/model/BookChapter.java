package com.example.userserver.es.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.util.List;

@Document(indexName = "book", type = "chapter", shards = 3)
//@Setting(settingPath = "/settings/settings.json")
//@Mapping(mappingPath = "/mappings/mappings.json")
public class BookChapter implements Serializable {

    // 查看某个字段数据的分词结果
//    GET /${index}/${type}/${id}/_termvectors?fields=${fields_name}

    @Id
    private Integer id;

    @Field(type = FieldType.Keyword)
    private String title;

    @Field(type = FieldType.Keyword)
    private String author;

    @Field(type = FieldType.Integer)
    private Integer articleTotal;

    @Field(type = FieldType.Text)
    private String desc;

    @Field(type=FieldType.Nested, includeInParent=true)
    private List<BookArticle> bookArticleList;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getArticleTotal() {
        return articleTotal;
    }

    public void setArticleTotal(Integer articleTotal) {
        this.articleTotal = articleTotal;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<BookArticle> getBookArticleList() {
        return bookArticleList;
    }

    public void setBookArticleList(List<BookArticle> bookArticleList) {
        this.bookArticleList = bookArticleList;
    }

}

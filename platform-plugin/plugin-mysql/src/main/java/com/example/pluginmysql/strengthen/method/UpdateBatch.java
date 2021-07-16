package com.example.pluginmysql.strengthen.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/6/28 14:53
 */
public class UpdateBatch extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sqlTemp = "<script>UPDATE %s %s WHERE %s in %s</script>";

        String whereSql = "<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\",\" close=\")\">#{item."+tableInfo.getKeyProperty()+"}</foreach>";

        StringBuilder sb =  new StringBuilder("set ");
        for (TableFieldInfo tableFieldInfo : tableInfo.getFieldList()) {
            sb.append(tableFieldInfo.getColumn()).append(" = case ").append(tableInfo.getKeyColumn());
            sb.append(" <foreach item='item' index='index' collection='list' > ");
            sb.append(" WHEN #{item." + tableInfo.getKeyProperty() + "}");
            sb.append(" <if test='item."+tableFieldInfo.getProperty()+"!=null'>  ");
            sb.append(" THEN #{item."+ tableFieldInfo.getProperty() +"} </if> ");
            sb.append(" <if test='item."+tableFieldInfo.getProperty()+"==null'>  ");
            sb.append(" THEN "+ tableFieldInfo.getColumn() + "</if> ");
            sb.append(" </foreach> end,");
        }
        // 删除最后一个,逗号
        String setSql = sb.toString().substring(0, sb.toString().length()-1);

        String sql = String.format(sqlTemp, tableInfo.getTableName(), setSql, tableInfo.getKeyColumn(),
                whereSql);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, "updateBatch", sqlSource);
    }
}

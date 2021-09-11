package com.example.pluginmq.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("local_message")
public class LocalMessage implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer mqType;

    private String exchangeOrTopic;

    private String routingKeyOrPartition;

    private String messageId;

    private Integer messageStatus;

    private String messageData;

    private Date createTime;

    private Date updateTime;

}
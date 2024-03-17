package com.example.pluginnetty.netty.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.nio.charset.StandardCharsets;
import java.util.List;

@ChannelHandler.Sharable
public class MessageCodecJson extends MessageToMessageCodec<ByteBuf, Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> list) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        //获取内容字节
        ObjectMapper mapper = new ObjectMapper();
        byte[] jsonBytes = mapper.writeValueAsString(msg).getBytes(StandardCharsets.UTF_8);
//        byte[] jsonBytes = JSON.toJSONString(msg, SerializerFeature.WriteClassName).getBytes(StandardCharsets.UTF_8);
        // 获取内容长度
        out.writeInt(jsonBytes.length);
        //  写入内容
        out.writeBytes(jsonBytes);
        list.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
        //获取内容长度
        int length = in.readInt();
        //将二进制反序列化为对象
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(new String(bytes, StandardCharsets.UTF_8), Message.class);
//        Message msg = JSON.parseObject(new String(bytes, StandardCharsets.UTF_8),Message.class);
        System.out.println(msg);
        list.add(msg);
    }
}

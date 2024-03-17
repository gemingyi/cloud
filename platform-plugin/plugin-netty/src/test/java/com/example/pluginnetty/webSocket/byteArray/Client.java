package com.example.pluginnetty.webSocket.byteArray;

import com.example.pluginnetty.netty.codec.Message;
import com.example.pluginnetty.util.SerializationUtil;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 23:29
 */
public class Client extends WebSocketClient {
    public Client(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("open----");
    }


    @Override
    public void onMessage(String s) {
        System.out.println("text message-" + s);
    }

    public void onMessage(ByteBuffer bytes) {
        byte[] contentBytes = decode(bytes);
        System.out.println("byte message:" + SerializationUtil.deserializer(contentBytes, Message.class));
    }

    public byte[] decode(ByteBuffer bytes) {
        int len = bytes.limit() - bytes.position();
        byte[] bytes1 = new byte[len];
        bytes.get(bytes1);
        return bytes1;
    }


    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("close----");
    }

    @Override
    public void onError(Exception e) {
        System.out.println("error----");
    }

    public static void main(String[] args) throws InterruptedException {
        List<Client> list = new CopyOnWriteArrayList();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                URI uri = URI.create("ws://127.0.0.1:9090");
                Client client = new Client(uri);
                client.connect();
                while (!client.getReadyState().equals(READYSTATE.OPEN)) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message p = new Message(1L, "gmy");
//                String p ="1000|1000|{\"name\":\"GLOBAL\"}";
                client.send(SerializationUtil.serializer(p));

                list.add(client);
            }
        }
        ).start();

        Thread.sleep(500000);
    }

}

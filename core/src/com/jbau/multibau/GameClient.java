package com.jbau.multibau;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import com.jbau.multibau.NetworkCommon.TextMessage;
import com.jbau.multibau.NetworkCommon.RegisterName;

public class GameClient {
    Client client;
    String name;

    public GameClient(String name) {
        client = new Client();
        client.start();
        this.name = name;

        NetworkCommon.register(client);

        client.addListener(new Listener() {
            public void connected(Connection connection) {
                RegisterName registerName = new RegisterName();
                registerName.name = name;
                client.sendTCP(registerName);
            }

            public void received(Connection connection, Object object) {
                if (object instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) object;
                    System.out.println(textMessage.text);
                    return;
                }
            }

            public void disconnected(Connection connection) {
                // run disconnection logic
            }
        });
    }

    public void connectClient() {
        try {
            client.connect(5000, NetworkCommon.host, NetworkCommon.port);
        } catch (Exception e) {
            System.out.println("Could not connect client.");
            e.printStackTrace();
        }
    }

    public void terminateClient() {
        client.close();
        client.stop();
    }
}

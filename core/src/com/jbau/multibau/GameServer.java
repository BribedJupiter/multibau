package com.jbau.multibau;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import com.esotericsoftware.minlog.Log;
import com.jbau.multibau.NetworkCommon.TextMessage;

import java.io.IOException;

public class GameServer {
    private final Server server;

    public GameServer() throws IOException {
        server = new Server() {
            protected Connection newConnection() {
                return new GameConnection();
            }
        };

        NetworkCommon.register(server);

        server.addListener(new Listener() {
            public void received(Connection incoming, Object object) {
                GameConnection connection = (GameConnection) incoming;

                if (object instanceof NetworkCommon.RegisterName) {
                    if (connection.name != null) return;
                    String name =((NetworkCommon.RegisterName)object).name;
                    if (name == null) return;
                    name = name.trim();
                    if (name.isEmpty()) return;
                    connection.name = name;
                    TextMessage message = new TextMessage();
                    message.text = name + " connected!";
                    server.sendToAllExceptTCP(connection.getID(), message);
                    return;
                }

                if (object instanceof NetworkCommon.TextMessage) {
                    if (connection.name == null) return;
                    TextMessage textMessage = (TextMessage) object;
                    String message = textMessage.text;
                    if (message == null) return;
                    message = message.trim();
                    if (message.isEmpty()) return;
                    textMessage.text = connection.name + ": " + message;
                    server.sendToAllTCP(textMessage);
                    return;
                }
            }

            public void disconnected(Connection c) {
                GameConnection connection = (GameConnection) c;
                if (connection.name != null) {
                    TextMessage textMessage = new TextMessage();
                    textMessage.text = connection.name + " disconnected.";
                    server.sendToAllTCP(textMessage);
                }
            }
        });

        server.bind(NetworkCommon.port);
        server.start();
    }

    public void terminateServer() {
        server.close();
        server.stop();
    }

    static class GameConnection extends Connection {
        public String name;
    }
}

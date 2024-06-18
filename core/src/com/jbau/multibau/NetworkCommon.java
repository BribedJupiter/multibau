package com.jbau.multibau;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class NetworkCommon {
    static public final int port = 54555;
    static public final String host = "localhost";

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(TextMessage.class);
        kryo.register(RegisterName.class);
    }

    static public class RegisterName {
        public String name;
    }

    static public class TextMessage {
        public String text;
    }
}

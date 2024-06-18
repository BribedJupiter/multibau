package com.jbau.multibau;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.minlog.Log;

public class MultibauServer extends ApplicationAdapter {
    private GameServer server;

    @Override
    public void create() {
        Log.set(Log.LEVEL_DEBUG);
        try {
            new GameServer();
        } catch (Exception e) {
            System.out.println("Could not create game server.");
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
    }

    @Override
    public void dispose() {
        server.terminateServer();
    }
}

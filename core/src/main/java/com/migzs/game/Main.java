package com.migzs.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.migzs.game.screens.FirstScreen;

public class Main extends Game {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;

    public Viewport viewport;
    public OrthographicCamera camera;
    public SpriteBatch batch;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        batch = new SpriteBatch();

        setScreen(new FirstScreen(this));
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
        if (screen != null) screen.dispose();
    }
}
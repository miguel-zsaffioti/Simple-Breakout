package com.migzs.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.migzs.game.Main;
import com.migzs.game.entities.Ball;
import com.migzs.game.entities.Cannon;

public class FirstScreen implements Screen {
    private Main main;
    private Rectangle r;
    private Vector2 mousePos;

    private Texture ballTexture;
    private Array<Ball> balls = new Array<Ball>(false, 10);

    private Cannon cannon;
    private Texture cannonTexture;

    public FirstScreen(Main main) {
        super();
        this.main = main;

        r = new Rectangle(0, 0, main.viewport.getWorldWidth(), main.viewport.getWorldHeight());
        mousePos = new Vector2();

        ballTexture = new Texture(Gdx.files.internal("Bola.png"));

        cannonTexture = new Texture(Gdx.files.internal("Canhao.png"));
        cannon = new Cannon(cannonTexture, r);
    }

    @Override
    public void show() {
    }

    private void compute(float delta) {
        mousePos.set(Gdx.input.getX(), Gdx.input.getY());
        cannon.compute(delta, main.viewport.unproject(mousePos));

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && cannon.podeAtirar()) {
            balls.add(cannon.disparar(ballTexture));
        }

        for (int i = balls.size - 1; i >= 0; i--) {
            Ball ball = balls.get(i);
            ball.compute(delta);

            if (ball.shouldRemove) {
                balls.removeIndex(i);
            }
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.batch.setProjectionMatrix(main.camera.combined);
        main.batch.begin();
        cannon.sprite.draw(main.batch);
        for (int i = 0, n = balls.size; i < n; i++) {
            balls.get(i).sprite.draw(main.batch);
        }
        main.batch.end();
    }

    @Override
    public void render(float delta) {
        this.compute(delta);
        this.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        ballTexture.dispose();
        cannonTexture.dispose();
    }
}
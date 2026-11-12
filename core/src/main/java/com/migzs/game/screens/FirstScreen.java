package com.migzs.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.migzs.game.Main;
import com.migzs.game.entities.Ball;
import com.migzs.game.entities.Block;
import com.migzs.game.entities.Cannon;

public class FirstScreen implements Screen {
    private Main main;
    private Rectangle r;
    private Vector2 mousePos;

    private Texture ballTexture;
    private Array<Ball> balls = new Array<Ball>(false, 10);

    private Texture blockTexture;
    private Array<Block> blocks = new Array<Block>(false, 64);
    private BitmapFont font;

    private Cannon cannon;
    private Texture cannonTexture;

    public FirstScreen(Main main) {
        super();
        this.main = main;

        r = new Rectangle(0, 0, main.viewport.getWorldWidth(), main.viewport.getWorldHeight());
        mousePos = new Vector2();

        ballTexture = new Texture(Gdx.files.internal("Bola.PNG"));
        blockTexture = new Texture(Gdx.files.internal("Blocos/Bloco_1.PNG"));

        font = new BitmapFont();
        font.getData().setScale(1.5f);

        cannonTexture = new Texture(Gdx.files.internal("Canhao.PNG"));
        cannon = new Cannon(cannonTexture, r);

        blocks.add(new Block(blockTexture, 350, 350, 100, 40, 3));
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

            for (int j = 0; j < blocks.size; j++) {
                Block block = blocks.get(j);
                if (block.shouldRemove) continue;
                if (ball.hitBlock(block)) break;
            }

            if (ball.shouldRemove) {
                balls.removeIndex(i);
            }
        }

        for (int j = blocks.size - 1; j >= 0; j--) {
            if (blocks.get(j).shouldRemove) {
                blocks.removeIndex(j);
            }
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.batch.setProjectionMatrix(main.camera.combined);
        main.batch.begin();
        cannon.sprite.draw(main.batch);
        for (int i = 0, n = blocks.size; i < n; i++) {
            blocks.get(i).draw(main.batch, font);
        }
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
        blockTexture.dispose();
        cannonTexture.dispose();
        font.dispose();
    }
}
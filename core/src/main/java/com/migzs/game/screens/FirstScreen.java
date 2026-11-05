package com.migzs.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.migzs.game.Main;
import com.migzs.game.entities.Ball;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
    private Main main;
    private Rectangle r;
    
    private Texture ballTexture;
    private Ball ball;

    public FirstScreen(Main main) {
        super();
        this.main = main;
        
        r = new Rectangle(0, 0, main.viewport.getWorldWidth(), main.viewport.getWorldHeight());
        
        ballTexture = new Texture(Gdx.files.internal("Bola.png"));
        ball = new Ball(r, ballTexture);
    }

    @Override
    public void show() {
        // Prepare your screen here.
    }

    private void compute(float delta) {
        ball.compute(delta);
    }   
    
    private void draw() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.batch.setProjectionMatrix(main.camera.combined);
        main.batch.begin();
        ball.sprite.draw(main.batch);
        main.batch.end();
    }
    
    @Override
    public void render(float delta) {
        this.compute(delta);
        this.draw();
    }

    @Override
    public void resize(int width, int height) {
        if(width <= 0 || height <= 0) return;
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // 4. Liberamos a textura da GPU aqui na tela, e não mais na bola
        ballTexture.dispose();
    }
}
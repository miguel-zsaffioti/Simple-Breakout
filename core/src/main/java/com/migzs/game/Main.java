package com.migzs.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.migzs.game.screens.FirstScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
	
	public Viewport viewport;
    public OrthographicCamera camera;
    public SpriteBatch batch;
    
    private static final int WIDTH = 800;
    private static final int HEIGHT = 480;
		
    @Override
    public void create() {
    	camera = new OrthographicCamera();
    	viewport = new FitViewport(WIDTH, HEIGHT, camera);
    	batch = new SpriteBatch();
    	
        setScreen(new FirstScreen(this));
    }
    
    @Override
    public void resize(int width, int height) {
        if(width <= 0 || height <= 0) return;
        viewport.update(width, height,true);
        super.resize(width, height);
    }

    
    @Override
    public void dispose() {
    	batch.dispose();
    	super.dispose();
    	if(screen != null) screen.dispose();
    	
    }
}
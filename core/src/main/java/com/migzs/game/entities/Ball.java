package com.migzs.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Ball { 
    public final Sprite sprite;
    private final Vector2 vel;
    private final Rectangle r;
    private int bounceCount; 
    private final int bounceLimit;
    
    public boolean shouldRemove = false;
    
    public Ball(Texture textura, float x, float y, float vx, float vy, Rectangle r	) {
        super();
        this.r = r;
        sprite = new Sprite(textura);
        sprite.setSize(32, 32);
        sprite.setCenter(x, y);
        vel = new Vector2(vx, vy);
        this.bounceCount = 0;
        this.bounceLimit = 2;
    }
    
    public void compute(float delta) {
        sprite.setPosition(sprite.getX() + vel.x * delta, sprite.getY() + vel.y * delta);
        this.ballBounce();
    }
    
    private void ballBounce() {
        // Colisão no Eixo X
        if (sprite.getX() + sprite.getWidth() >= r.width) {
            handleBounce(-1, 1, r.width - sprite.getWidth(), sprite.getY());
        } else if (sprite.getX() < r.x) {
            handleBounce(-1, 1, r.x, sprite.getY());
        }
        
        // Colisão no Eixo Y
        if (sprite.getY() + sprite.getHeight() >= r.height) {
            handleBounce(1, -1, sprite.getX(), r.height - sprite.getHeight());
        } else if (sprite.getY() < r.y) {
            handleBounce(1, -1, sprite.getX(), r.y);
        }
    }
    
    private void handleBounce(float scaleX, float scaleY, float targetX, float targetY) {
        vel.scl(scaleX, scaleY);
        sprite.setPosition(targetX, targetY);
        this.bounceCount++;
        
        // Verifica logo após quicar se atingiu o limite
        if (this.bounceCount >= this.bounceLimit) {
            this.shouldRemove = true;
        }
    }
}

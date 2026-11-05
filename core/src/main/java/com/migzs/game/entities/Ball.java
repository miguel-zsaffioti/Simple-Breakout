package com.migzs.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Ball { 
    public Sprite sprite;
    private Vector2 vel = new Vector2(400, 200);
    private Rectangle r;
    
    public Ball(Rectangle r, Texture ballTexture) {
        super();
        this.r = r;
        this.sprite = new Sprite(ballTexture);
        sprite.setSize(64, 64);
    }
    
    public void compute(float delta) {
        sprite.setPosition(sprite.getX() + vel.x * delta, sprite.getY() + vel.y * delta);
        this.ballBounce();
    }
    
    
    private void ballBounce() {
        if(sprite.getX() + sprite.getWidth() >= r.width) {
            vel.scl(-1, 1);
            sprite.setX(r.width - sprite.getWidth());
        }
        if(sprite.getX() < r.x) {
            vel.scl(-1, 1);
            sprite.setX(r.x);
        }
        if(sprite.getY() + sprite.getHeight() >= r.height) {
            vel.scl(1, -1);
            sprite.setY(r.height - sprite.getHeight());
        }
        if(sprite.getY() < r.y) {
            vel.scl(1, -1);
            sprite.setY(r.y);
        }
    }
}
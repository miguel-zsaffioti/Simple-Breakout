package com.migzs.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Block {
    public final Sprite sprite;
    private final Rectangle bounds;
    private int life;

    public boolean shouldRemove = false;

    public Block(Texture texture, float x, float y, float width, float height, int life) {
        this.sprite = new Sprite(texture);
        this.sprite.setSize(width, height);
        this.sprite.setPosition(x, y);
        this.bounds = new Rectangle(x, y, width, height);
        this.life = life;
    }

    public void hit(int damage) {
        life -= damage;
        if (life <= 0) {
            shouldRemove = true;
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getLife() {
        return life;
    }
}
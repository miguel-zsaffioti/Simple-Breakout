package com.migzs.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Ball {
    public final Sprite sprite;
    private final Vector2 vel;
    private final Rectangle r;
    private final Rectangle hitbox = new Rectangle();
    private int bounceCount;
    private final int bounceLimit;

    private static final int DAMAGE = 1;

    public boolean shouldRemove = false;

    public Ball(Texture textura, float x, float y, float vx, float vy, Rectangle r) {
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

    public boolean hitBlock(Block block) {
        hitbox.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        Rectangle b = block.getBounds();

        if (!hitbox.overlaps(b)) return false;

        float overlapX = Math.min(hitbox.x + hitbox.width, b.x + b.width) - Math.max(hitbox.x, b.x);
        float overlapY = Math.min(hitbox.y + hitbox.height, b.y + b.height) - Math.max(hitbox.y, b.y);

        if (overlapX < overlapY) {
            float side = (hitbox.x + hitbox.width / 2f) < (b.x + b.width / 2f) ? -1f : 1f;
            sprite.translateX(side * overlapX);
            vel.x = side * Math.abs(vel.x);
        } else {
            float side = (hitbox.y + hitbox.height / 2f) < (b.y + b.height / 2f) ? -1f : 1f;
            sprite.translateY(side * overlapY);
            vel.y = side * Math.abs(vel.y);
        }

        block.hit(DAMAGE);
        return true;
    }

    private void ballBounce() {
        if (sprite.getY() < r.y) {
            shouldRemove = true;
            return;
        }

        boolean bounced = false;

        if (sprite.getX() + sprite.getWidth() >= r.width) {
            vel.x = -Math.abs(vel.x);
            sprite.setX(r.width - sprite.getWidth());
            bounced = true;
        } else if (sprite.getX() < r.x) {
            vel.x = Math.abs(vel.x);
            sprite.setX(r.x);
            bounced = true;
        }

        if (sprite.getY() + sprite.getHeight() >= r.height) {
            vel.y = -Math.abs(vel.y);
            sprite.setY(r.height - sprite.getHeight());
            bounced = true;
        }

        if (bounced) {
            bounceCount++;
            if (bounceCount >= bounceLimit) {
                shouldRemove = true;
            }
        }
    }
}
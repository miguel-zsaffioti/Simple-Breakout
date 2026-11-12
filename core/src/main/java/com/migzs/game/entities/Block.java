package com.migzs.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Block {
    public final Sprite sprite;
    private final Rectangle bounds;
    private final GlyphLayout layout = new GlyphLayout();
    private int life;
    private boolean textDirty = true;

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
        textDirty = true;
        if (life <= 0) {
            shouldRemove = true;
        }
    }

    public void draw(SpriteBatch batch, BitmapFont font) {
        sprite.draw(batch);

        if (textDirty) {
            layout.setText(font, String.valueOf(life));
            textDirty = false;
        }
        float textX = bounds.x + (bounds.width - layout.width) / 2f;
        float textY = bounds.y + (bounds.height + layout.height) / 2f;
        font.draw(batch, layout, textX, textY);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getLife() {
        return life;
    }
}
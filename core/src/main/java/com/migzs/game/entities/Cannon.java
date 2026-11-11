package com.migzs.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Cannon {
    public final Sprite sprite;
    private final Vector2 pos;
    private final Vector2 pivot;
    private final Vector2 worldPivot;
    private final Rectangle bounds;

    private static final float COMPRIMENTO = 90f;
    private static final float VELOCIDADE = 450f;
    private final Vector2 dir = new Vector2(0, 1);

    public Cannon(Texture texture, Rectangle screenBounds) {
        this.bounds = screenBounds;
        this.sprite = new Sprite(texture);
        this.sprite.setSize(128, 128);

        float x = (screenBounds.width / 2f) - (sprite.getWidth() / 2f);
        float y = 0;

        this.pos = new Vector2(x, y);
        this.sprite.setPosition(pos.x, pos.y);

        this.pivot = new Vector2(sprite.getWidth() / 2f, 0);
        this.sprite.setOrigin(pivot.x, pivot.y);

        this.worldPivot = new Vector2(pos.x + pivot.x, pos.y + pivot.y);
    }

    public void compute(float delta, Vector2 mousePos) {
        float angle = mousePos.cpy().sub(worldPivot).angleDeg();
        dir.set(mousePos).sub(worldPivot).nor();
        sprite.setRotation(angle - 90);
    }

    public Ball disparar(Texture textura) {
        float x = worldPivot.x + dir.x * COMPRIMENTO;
        float y = worldPivot.y + dir.y * COMPRIMENTO;
        return new Ball(textura, x, y, dir.x * VELOCIDADE, dir.y * VELOCIDADE, bounds);
    }
}
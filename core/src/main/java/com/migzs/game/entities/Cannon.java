package com.migzs.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Cannon {
    public final Sprite sprite;
    private final Vector2 pos;
    private final Vector2 pivot;
    private final Vector2 worldPivot;
    private final Rectangle bounds;

    private static final float COMPRIMENTO = 90f;
    private static final float VELOCIDADE = 650f;
    private static final float COOLDOWN = 0.6f;
    private static final float ANGULO_MIN = 15f;
    private static final float ANGULO_MAX = 165f;

    private final Vector2 dir = new Vector2(0, 1);
    private final Vector2 aim = new Vector2();
    private float cooldownRestante = 0f;

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
        cooldownRestante = Math.max(0f, cooldownRestante - delta);

        aim.set(mousePos).sub(worldPivot);
        float angle = aim.angleDeg();

        if (angle > 180f) {
            angle = angle >= 270f ? ANGULO_MIN : ANGULO_MAX;
        }
        angle = MathUtils.clamp(angle, ANGULO_MIN, ANGULO_MAX);

        dir.set(MathUtils.cosDeg(angle), MathUtils.sinDeg(angle));
        sprite.setRotation(angle - 90);
    }

    public boolean podeAtirar() {
        return cooldownRestante <= 0f;
    }

    public Ball disparar(Texture textura) {
        cooldownRestante = COOLDOWN;
        float x = worldPivot.x + dir.x * COMPRIMENTO;
        float y = worldPivot.y + dir.y * COMPRIMENTO;
        return new Ball(textura, x, y, dir.x * VELOCIDADE, dir.y * VELOCIDADE, bounds);
    }
}
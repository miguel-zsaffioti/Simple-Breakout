package com.migzs.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.migzs.game.Main;
import com.migzs.game.entities.Ball;
import com.migzs.game.entities.Block;
import com.migzs.game.entities.Cannon;

public class FirstScreen implements Screen {
    private static final int MAX_BALLS = 3;

    private static final float FRAME = 8f;
    private static final float HUD_H = 60f;
    private static final float HUD_PAD = 14f;
    private static final float TEMPO_FASE = 60f;

    private static final int COLUNAS = 4;
    private static final float BLOCO_H = 44f;
    private static final float GAP = 10f;
    private static final float MARGEM_X = 20f;
    private static final float MARGEM_TOPO = 20f;
    private static final int VIDA_INICIAL = 3;
    private static final int VIDA_POR_LINHA = 2;

    private Main main;
    private Rectangle r;
    private Vector2 mousePos;
    private float worldW;
    private float worldH;
    private float hudY;

    private int fase = 1;
    private int pontos = 0;
    private float tempoRestante = TEMPO_FASE;

    private ShapeRenderer shapes;

    private Texture ballTexture;
    private Array<Ball> balls = new Array<Ball>(false, 10);

    private Array<Texture> blockTextures = new Array<Texture>(false, 5);
    private Array<Block> blocks = new Array<Block>(false, 64);
    private BitmapFont font;

    private Cannon cannon;
    private Texture cannonTexture;

    public FirstScreen(Main main) {
        super();
        this.main = main;

        worldW = main.viewport.getWorldWidth();
        worldH = main.viewport.getWorldHeight();
        r = new Rectangle(FRAME, FRAME, worldW - 2 * FRAME, worldH - FRAME - HUD_H);
        mousePos = new Vector2();

        shapes = new ShapeRenderer();

        ballTexture = new Texture(Gdx.files.internal("Bola.PNG"));
        blockTextures.add(new Texture(Gdx.files.internal("Blocos/Bloco_1.PNG")));
        blockTextures.add(new Texture(Gdx.files.internal("Blocos/Bloco_2.PNG")));
        blockTextures.add(new Texture(Gdx.files.internal("Blocos/Bloco_3.PNG")));
        blockTextures.add(new Texture(Gdx.files.internal("Blocos/Bloco_4.PNG")));
        blockTextures.add(new Texture(Gdx.files.internal("Blocos/Bloco_5.PNG")));

        font = new BitmapFont();
        font.getData().setScale(1.5f);

        float hudBottom = r.y + r.height + FRAME;
        float hudTop = worldH - FRAME;
        hudY = (hudBottom + hudTop) / 2f + font.getCapHeight() / 2f;

        cannonTexture = new Texture(Gdx.files.internal("Canhao.PNG"));
        cannon = new Cannon(cannonTexture, r);

        buildBlocks();
    }

    private int vidaDaLinha(int linha) {
        int linhasAbaixo = blockTextures.size - 1 - linha;
        return VIDA_INICIAL + linhasAbaixo * VIDA_POR_LINHA;
    }

    private void buildBlocks() {
        float blocoW = (r.width - 2 * MARGEM_X - (COLUNAS - 1) * GAP) / COLUNAS;
        float startY = r.y + r.height - MARGEM_TOPO - BLOCO_H;

        for (int linha = 0; linha < blockTextures.size; linha++) {
            Texture texture = blockTextures.get(linha);
            float y = startY - linha * (BLOCO_H + GAP);
            int vida = vidaDaLinha(linha);

            for (int coluna = 0; coluna < COLUNAS; coluna++) {
                float x = r.x + MARGEM_X + coluna * (blocoW + GAP);
                blocks.add(new Block(texture, x, y, blocoW, BLOCO_H, vida));
            }
        }
    }

    @Override
    public void show() {
    }

    private void compute(float delta) {
        tempoRestante = Math.max(0f, tempoRestante - delta);

        mousePos.set(Gdx.input.getX(), Gdx.input.getY());
        cannon.compute(delta, main.viewport.unproject(mousePos));

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                && cannon.podeAtirar()
                && balls.size < MAX_BALLS) {
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

    private void drawFrame() {
        shapes.setProjectionMatrix(main.camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.LIGHT_GRAY);
        shapes.rect(0, 0, worldW, FRAME);
        shapes.rect(0, 0, FRAME, worldH);
        shapes.rect(worldW - FRAME, 0, FRAME, worldH);
        shapes.rect(0, worldH - FRAME, worldW, FRAME);
        shapes.rect(0, r.y + r.height, worldW, FRAME);
        shapes.end();
    }

    private void drawHud() {
        String textoFase = "Fase " + fase;
        String textoPontos = "Pontos " + pontos;
        String textoTempo = "Tempo " + (int) Math.ceil(tempoRestante);

        font.draw(main.batch, textoFase, FRAME + HUD_PAD, hudY);
        font.draw(main.batch, textoPontos, 0, hudY, worldW, Align.center, false);
        font.draw(main.batch, textoTempo, 0, hudY, worldW - FRAME - HUD_PAD, Align.right, false);
    }

    private void draw() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawFrame();

        main.batch.setProjectionMatrix(main.camera.combined);
        main.batch.begin();
        cannon.sprite.draw(main.batch);
        for (int i = 0, n = blocks.size; i < n; i++) {
            blocks.get(i).draw(main.batch, font);
        }
        for (int i = 0, n = balls.size; i < n; i++) {
            balls.get(i).sprite.draw(main.batch);
        }
        drawHud();
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
        shapes.dispose();
        ballTexture.dispose();
        blockTextures.forEach(texture -> texture.dispose());
        cannonTexture.dispose();
        font.dispose();
    }
}
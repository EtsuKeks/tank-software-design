package ru.mipt.bit.platformer.graphicmodels;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.concurrent.atomic.AtomicReference;

public class BulletGraphicModel implements IGraphicModel {
    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final AtomicReference<Float> rotation;

    public BulletGraphicModel(Rectangle rectangle, String texturePath, AtomicReference<Float> rotation) {
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.rectangle = rectangle;
        this.rotation = rotation;
    }

    @Override
    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, rotation.get());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
package ru.mipt.bit.platformer.graphicmodels;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class BulletGraphicModel implements IGraphicModel {
    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final float rotation;

    public BulletGraphicModel(Rectangle rectangle, String texturePath, float rotation) {
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.rectangle = rectangle;
        this.rotation = rotation;
    }

    public Rectangle getRectangle() {
        return this.rectangle;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(float rotation) {
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    @Override
    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, rotation);
    }
}
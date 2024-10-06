package ru.mipt.bit.platformer.graphicmodels;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class TankGraphicModel {
    private final Rectangle rectangle;
    private final Texture texture;
    private final TextureRegion graphics;
    private float rotation;

    public TankGraphicModel(Rectangle rectangle, String texturePath) {
        this.rectangle = rectangle;
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.rotation = 0f;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void dispose() {
        texture.dispose();
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, rotation);
    }
}
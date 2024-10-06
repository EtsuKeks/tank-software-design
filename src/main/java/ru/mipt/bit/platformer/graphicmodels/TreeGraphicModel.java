package ru.mipt.bit.platformer.graphicmodels;

import ru.mipt.bit.platformer.util.GdxGameUtils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class TreeGraphicModel implements IGraphicModel {
    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private float rotation;

    public TreeGraphicModel(Rectangle rectangle, String texturePath) {
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.rectangle = rectangle;
        this.rotation = 0f;
    }

    @Override
    public void render(Batch batch) {
        GdxGameUtils.drawTextureRegionUnscaled(batch, graphics, rectangle, rotation);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
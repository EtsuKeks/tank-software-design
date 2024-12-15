package ru.mipt.bit.platformer.graphicmodels;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class TreeGraphicModel implements IGraphicModel {
    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public TreeGraphicModel(Rectangle rectangle, String texturePath) {
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.rectangle = rectangle;
    }

    @Override
    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
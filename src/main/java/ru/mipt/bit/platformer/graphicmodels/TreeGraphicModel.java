package ru.mipt.bit.platformer.graphicmodels;

import ru.mipt.bit.platformer.util.GdxGameUtils;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class TreeGraphicModel {
    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public TreeGraphicModel(Rectangle rectangle, String texturePath, TiledMapTileLayer layer) {
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.rectangle = rectangle;
    }

    public void render(Batch batch) {
        GdxGameUtils.drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }

    public void dispose() {
        texture.dispose();
    }
}
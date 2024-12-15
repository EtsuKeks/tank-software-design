package ru.mipt.bit.platformer.keepers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.GdxGameUtils;

public abstract class ModelCreator {
    public abstract void createModel(GridPoint2 coords);
    protected Rectangle initRectangle(TiledMapTileLayer tiledMapTileLayer, String texturePath, GridPoint2 coordinate) {
        Texture texture = new Texture(texturePath);
        TextureRegion graphics = new TextureRegion(texture);
        Rectangle rectangle = GdxGameUtils.createBoundingRectangle(graphics);
        GdxGameUtils.moveRectangleAtTileCenter(tiledMapTileLayer, rectangle, coordinate);
        return rectangle;
    }
}

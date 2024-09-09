package ru.mipt.bit.platformer.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.GdxGameUtils;

public abstract class GameObject {
    protected Texture texture;
    protected TextureRegion graphics;
    protected GridPoint2 coordinates;
    protected Rectangle rectangle;
    protected TiledMapTileLayer layer;

    public GameObject(String texturePath, TiledMapTileLayer layer, int x, int y) {
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.layer = layer;
        this.coordinates = new GridPoint2(x, y);
        this.rectangle = GdxGameUtils.createBoundingRectangle(graphics);
        GdxGameUtils.moveRectangleAtTileCenter(layer, rectangle, coordinates);
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    public void render(Batch batch) {
        GdxGameUtils.drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }

    public void dispose() {
        texture.dispose();
    }
}

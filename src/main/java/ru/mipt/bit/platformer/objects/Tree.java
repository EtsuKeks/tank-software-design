package ru.mipt.bit.platformer.objects;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Tree extends GameObject {

    public Tree(String texturePath, TiledMapTileLayer layer, int x, int y) {
        super(texturePath, layer, x, y);
    }
}

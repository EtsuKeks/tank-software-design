package ru.mipt.bit.platformer.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import ru.mipt.bit.platformer.commands.ShootPlayerCommand;
import ru.mipt.bit.platformer.modelinitializers.ModelZooKeeper;
import ru.mipt.bit.platformer.util.TileMovement;

import java.io.IOException;

public class ShootPlayerHandler implements Handler {
    private final ShootPlayerCommand shootPlayerCommand;

    public ShootPlayerHandler(ModelZooKeeper modelZooKeeper,
                              TiledMapTileLayer groundLayer, TileMovement tileMovement) throws IOException {
        shootPlayerCommand = new ShootPlayerCommand(modelZooKeeper, groundLayer, tileMovement);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            shootPlayerCommand.execute();
        }
    }
}
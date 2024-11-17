package ru.mipt.bit.platformer.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mipt.bit.platformer.commands.ShootPlayerCommand;
import ru.mipt.bit.platformer.modelinitializers.ModelZooKeeper;
import ru.mipt.bit.platformer.util.TileMovement;

@Service
public class ShootPlayerHandler implements Handler {
    private final ShootPlayerCommand shootPlayerCommand;

    @Autowired
    public ShootPlayerHandler(ModelZooKeeper modelZooKeeper,
                              TiledMapTileLayer groundLayer, TileMovement tileMovement) {
        shootPlayerCommand = new ShootPlayerCommand(modelZooKeeper, groundLayer, tileMovement);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            shootPlayerCommand.execute();
        }
    }
}
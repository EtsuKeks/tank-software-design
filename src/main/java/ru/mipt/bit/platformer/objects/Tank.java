package ru.mipt.bit.platformer.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Tank extends GameObject {
    private final TileMovement tileMovement;
    private final float movementSpeed;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation;

    public Tank(String texturePath, TiledMapTileLayer layer, TileMovement tileMovement, float movementSpeed, int startX, int startY) {
        super(texturePath, layer, startX, startY);
        this.tileMovement = tileMovement;
        this.movementSpeed = movementSpeed;
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.rotation = 0f;
    }

    public void handleInput(GridPoint2 obstacleCoordinates) {
        for (Direction direction : Direction.values()) {
            if ((Gdx.input.isKeyPressed(direction.getKeyCode()) || Gdx.input.isKeyPressed(direction.getSecondaryKeyCode())) 
                 && isEqual(movementProgress, 1f)) {
                GridPoint2 potentialDestination = direction.getNextCoordinates(coordinates);
                if (!obstacleCoordinates.equals(potentialDestination)) {
                    destinationCoordinates.set(potentialDestination);
                    movementProgress = 0f;
                    rotation = direction.getRotation();
                }
            }
        }
        move();
    }

    private void move() {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, coordinates, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, Gdx.graphics.getDeltaTime(), movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destinationCoordinates);
        }
    }

    @Override
    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, rotation);
    }
}

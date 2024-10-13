package ru.mipt.bit.platformer.logicmodels;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.IDirection;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public class TankLogicModel {
    private final Rectangle rectangle;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private final TileMovement tileMovement;
    private final float movementSpeed;
    private final ArrayList<TreeLogicModel> obstacles;

    public TankLogicModel(Rectangle rectangle, TileMovement tileMovement, float movementSpeed, GridPoint2 tankStartPos, ArrayList<TreeLogicModel> obstacles) {
        this.rectangle = rectangle;
        this.coordinates = tankStartPos;
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.tileMovement = tileMovement;
        this.movementSpeed = movementSpeed;
        this.obstacles = obstacles;
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    public boolean move(IDirection direction) {
        boolean isDirectionAccepted = false;
        if (isEqual(movementProgress, 1f) && isMovementAcceptable(direction)) {
            GridPoint2 potentialDestination = direction.getNextCoordinates(coordinates);
            destinationCoordinates.set(potentialDestination);
            movementProgress = 0f;
            isDirectionAccepted = true;
        }

        commitMove();
        return isDirectionAccepted;
    }

    private void commitMove() {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, coordinates, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, Gdx.graphics.getDeltaTime(), movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destinationCoordinates);
        }
    }

    private boolean isMovementAcceptable(IDirection direction) {
        if (direction == Direction.NULL) {
            return false;
        }

        GridPoint2 potentialDestination = direction.getNextCoordinates(coordinates);
        for (TreeLogicModel obstacle: obstacles) {
            GridPoint2 obstacleCoordinates = obstacle.getCoordinates();
            if (obstacleCoordinates.equals(potentialDestination)) {
                return false;
            }
        }

        return true;
    }
}
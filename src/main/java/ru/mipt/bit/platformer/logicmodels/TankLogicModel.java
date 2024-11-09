package ru.mipt.bit.platformer.logicmodels;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;
import ru.mipt.bit.platformer.keepers.ModelZooKeeper;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.IDirection;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import java.util.Collection;
import java.util.Random;

public class TankLogicModel implements Obstacle {
    private final ModelZooKeeper modelZooKeeper;
    private final Rectangle rectangle;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private final TileMovement tileMovement;
    private final float movementSpeed;
    private final Collection<Obstacle> obstacles;
    private float health;
    private final float totalHealth;

    public TankLogicModel(ModelZooKeeper modelZooKeeper, boolean playable, Rectangle rectangle, TileMovement tileMovement,
                          float movementSpeed, GridPoint2 tankStartPos, Collection<Obstacle> obstacles) {
        this.modelZooKeeper = modelZooKeeper;
        this.rectangle = rectangle;
        this.coordinates = tankStartPos;
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.tileMovement = tileMovement;
        this.movementSpeed = movementSpeed;
        this.obstacles = obstacles;
        this.health = 80 + new Random().nextInt(21);
        this.totalHealth = this.health;

        modelZooKeeper.notifyBorn(this, playable);
    }

    @Override
    public boolean isOccupied(GridPoint2 point){
        return coordinates.equals(point) || destinationCoordinates.equals(point);
    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    public float getHealthRatio() {
        return health / totalHealth;
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
        for (Obstacle obstacle: obstacles) {
            if (obstacle.isOccupied(potentialDestination)) {
                if (obstacle instanceof TankLogicModel && ((TankLogicModel) obstacle).equals(this)) {
                    continue;
                }
                return false;
            }
        }

        return true;
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }
}
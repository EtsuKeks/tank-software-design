package ru.mipt.bit.platformer.logicmodels;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;
import ru.mipt.bit.platformer.modelinitializers.ModelZooKeeper;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.IDirection;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;

public class TankLogicModel implements Obstacle {
    private final ModelZooKeeper modelZooKeeper;
    private final Rectangle rectangle;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private final TileMovement tileMovement;
    private final float movementSpeed;
    private float health;
    private final float totalHealth;

    public TankLogicModel(ModelZooKeeper modelZooKeeper, boolean playable, Rectangle rectangle,
                          TileMovement tileMovement, float movementSpeed, GridPoint2 tankStartPos) {
        this.modelZooKeeper = modelZooKeeper;
        this.rectangle = rectangle;
        this.coordinates = new GridPoint2(tankStartPos);
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.tileMovement = tileMovement;
        this.movementSpeed = movementSpeed;
        this.health = 80 + new Random().nextInt(21);
        this.totalHealth = this.health;

        modelZooKeeper.notifyBorn(this, playable);
    }

    @Override
    public boolean isOccupied(GridPoint2 coords){
        return coordinates.equals(coords) || destinationCoordinates.equals(coords);
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
        for (Obstacle obstacle: modelZooKeeper.getObstacles()) {
            if (obstacle.isOccupied(potentialDestination)) {
                if (obstacle instanceof TankLogicModel && obstacle.equals(this)) {
                    continue;
                }
                return false;
            }
        }

        return true;
    }

    public void takeDamage(float damage) {
        health -= damage;
        if (health <= 0) {
            modelZooKeeper.notifyDead(this, false);
        }
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }
}
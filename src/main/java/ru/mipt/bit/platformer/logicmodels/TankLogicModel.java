package ru.mipt.bit.platformer.logicmodels;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

import ru.mipt.bit.platformer.keepers.Observer;
import ru.mipt.bit.platformer.keepers.ObstacleModelKeeper;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class TankLogicModel implements Obstacle {
    private final Observer observer;
    private final Collection<Obstacle> obstacles;
    private final Rectangle rectangle;
    private final float maxHealth;
    private final AtomicReference<Float> rotation;
    private final GridPoint2 coords;
    private final GridPoint2 destinationCoords;
    private float movementProgress = 1f;
    private final TileMovement tileMovement;
    private final float movementSpeed;
    private final AtomicReference<Float> healthRatio;

    public TankLogicModel(Observer observer, ObstacleModelKeeper obstacleModelKeeper, Rectangle rectangle,
                          TileMovement tileMovement, float movementSpeed, GridPoint2 coords) {
        this.observer = observer;
        this.obstacles = obstacleModelKeeper.obstacleModels;
        this.rectangle = rectangle;
        this.maxHealth = (float) (80 + new Random().nextInt(21));
        this.rotation = new AtomicReference<>(0f);
        this.coords = new GridPoint2(coords);
        this.destinationCoords = new GridPoint2(coords);
        this.tileMovement = tileMovement;
        this.movementSpeed = movementSpeed;
        this.healthRatio = new AtomicReference<>(1f);
        observer.notifyBorn(this);
    }

    @Override
    public boolean isOccupied(GridPoint2 coords){
        return this.coords.equals(coords) || this.destinationCoords.equals(coords);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public AtomicReference<Float> getRotation() {
        return rotation;
    }

    public AtomicReference<Float> getHealthRatio() {
        return healthRatio;
    }

    public GridPoint2 getCoordinates() {
        return coords;
    }

    public void move(Direction direction) {
        if (isEqual(movementProgress, 1f) && isMovementAcceptable(direction)) {
            destinationCoords.set(direction.getNextCoordinates(coords));
            rotation.set(direction.getRotation());
            movementProgress = 0f;
        }

        commitMove();
    }

    private void commitMove() {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, coords, destinationCoords, movementProgress);
        movementProgress = continueProgress(movementProgress, Gdx.graphics.getDeltaTime(), movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            coords.set(destinationCoords);
        }
    }

    private boolean isMovementAcceptable(Direction direction) {
        if (direction == Direction.NULL) {
            return false;
        }

        GridPoint2 potentialDestination = direction.getNextCoordinates(coords);
        for (Obstacle obstacle: obstacles) {
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
        healthRatio.set(healthRatio.get() - damage / maxHealth);
        if (healthRatio.get() <= 0) {
            observer.notifyDead(this);
        }
    }
}
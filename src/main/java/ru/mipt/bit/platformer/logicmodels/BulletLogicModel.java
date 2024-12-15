package ru.mipt.bit.platformer.logicmodels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.keepers.Observer;
import ru.mipt.bit.platformer.keepers.ObstacleModelKeeper;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class BulletLogicModel {
    private final Observer observer;
    private final Collection<Obstacle> obstacles;
    private final Rectangle rectangle;
    private final GridPoint2 coords;
    private final GridPoint2 destinationCoords;
    private float movementProgress = 1f;
    private final TileMovement tileMovement;
    private final float movementSpeed;
    private final TankLogicModel sourceTank;
    private final float damage;
    private final AtomicReference<Float> rotation;
    private final Direction direction;

    public BulletLogicModel(Observer observer, ObstacleModelKeeper obstacleModelKeeper, Rectangle rectangle,
                            TileMovement tileMovement, float movementSpeed, TankLogicModel sourceTank,
                            float damage, AtomicReference<Float> rotation) {
        this.observer = observer;
        this.obstacles = obstacleModelKeeper.obstacleModels;
        this.rectangle = rectangle;
        this.coords = new GridPoint2(sourceTank.getCoordinates());
        this.destinationCoords = new GridPoint2(sourceTank.getCoordinates());
        this.tileMovement = tileMovement;
        this.movementSpeed = movementSpeed;
        this.sourceTank = sourceTank;
        this.damage = damage;
        this.rotation = new AtomicReference<>(rotation.get());
        this.direction = rotationToDirection(rotation.get());
        observer.notifyBorn(this);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public AtomicReference<Float> getRotation() {
        return rotation;
    }

    private Direction rotationToDirection(float rotation) {
        switch ((int) rotation) {
            case 0 -> {return Direction.RIGHT;}
            case 180 -> {return Direction.LEFT;}
            case 90 -> {return Direction.UP;}
            case -90 -> {return Direction.DOWN;}
            default -> {throw new IllegalArgumentException("There is invalid rotation " +
                    rotation + " passed to a bullet's rotationToDirection(");}
        }
    }

    public void move() {
        Collection<Obstacle> collidedObstacles = getCollidedObstacles();
        if (!collidedObstacles.isEmpty()) {
            handleCollisions(collidedObstacles);
            return;
        }

        if (isEqual(movementProgress, 1f)) {
            destinationCoords.set(direction.getNextCoordinates(coords));
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

    private Collection<Obstacle> getCollidedObstacles() {
        Collection<Obstacle> collidedObstacles = new ArrayList<>();
        for (Obstacle obstacle: obstacles) {
            if (obstacle.isOccupied(destinationCoords) || obstacle.isOccupied(coords)) {
                collidedObstacles.add(obstacle);
            }
        }

        collidedObstacles.remove(sourceTank);

        return collidedObstacles;
    }

    private void handleCollisions(Collection<Obstacle> collidedObstacles) {
        observer.notifyDead(this);
        for (Obstacle obstacle: collidedObstacles) {
            if (obstacle instanceof TankLogicModel) {
                ((TankLogicModel) obstacle).takeDamage(damage);
            }
        }
    }
}
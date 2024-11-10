package ru.mipt.bit.platformer.logicmodels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.modelinitializers.ModelZooKeeper;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.IDirection;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.Collection;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class BulletLogicModel implements Obstacle {
    private final ModelZooKeeper modelZooKeeper;
    private final Rectangle rectangle;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private final TileMovement tileMovement;
    private final float movementSpeed;
    private final TankLogicModel sourceTank;
    private final float damage;
    private final IDirection direction;

    public BulletLogicModel(ModelZooKeeper modelZooKeeper, Rectangle rectangle, TileMovement tileMovement,
                          float movementSpeed, GridPoint2 bulletStartPos, TankLogicModel sourceTank, float damage, float rotation) {
        this.modelZooKeeper = modelZooKeeper;
        this.rectangle = rectangle;
        this.coordinates = new GridPoint2(bulletStartPos);
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.tileMovement = tileMovement;
        this.movementSpeed = movementSpeed;
        this.sourceTank = sourceTank;
        this.damage = damage;
        this.direction = rotationToDirection(rotation);

        modelZooKeeper.notifyBorn(this, false);
    }

    @Override
    public boolean isOccupied(GridPoint2 coords){
        return coordinates.equals(coords) || destinationCoordinates.equals(coords);
    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    public float getRotation() {
        switch (direction) {
            case Direction.RIGHT -> {return 0f;}
            case Direction.LEFT -> {return 180f;}
            case Direction.UP -> {return 90f;}
            case Direction.DOWN -> {return -90;}
            default -> {throw new IllegalArgumentException("There is invalid direction " +
                    direction + " passed to a bullet");}
        }
    }

    private IDirection rotationToDirection(float rotation) {
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
        Collection<Obstacle> collidedObstacles = getCollidedObstacles(direction);
        if (!collidedObstacles.isEmpty()) {
            handleCollisions(collidedObstacles);
            return;
        }

        if (isEqual(movementProgress, 1f)) {
            destinationCoordinates.set(direction.getNextCoordinates(coordinates));
            movementProgress = 0f;
        }

        commitMove();
    }

    private void commitMove() {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, coordinates, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, Gdx.graphics.getDeltaTime(), movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destinationCoordinates);
        }
    }

    private Collection<Obstacle> getCollidedObstacles(IDirection direction) {
        if (direction == Direction.NULL) {
            throw new IllegalArgumentException("Invalid direction in bullet: " + direction);
        }

        Collection<Obstacle> collidedObstacles = new ArrayList<>();
        for (Obstacle obstacle: modelZooKeeper.getObstacles()) {
            if (obstacle.isOccupied(destinationCoordinates) || obstacle.isOccupied(coordinates)) {
                collidedObstacles.add(obstacle);
            }
        }
        collidedObstacles.remove(this);
        collidedObstacles.remove(sourceTank);

        return collidedObstacles;
    }

    private void handleCollisions(Collection<Obstacle> collidedObstacles) {
        modelZooKeeper.notifyDead(this, false);
        for (Obstacle obstacle: collidedObstacles) {
            if (obstacle instanceof TankLogicModel) {
                ((TankLogicModel) obstacle).takeDamage(damage);
            }
        }
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }
}
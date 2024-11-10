package ru.mipt.bit.platformer.logicmodels;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.modelinitializers.ModelZooKeeper;

public class TreeLogicModel implements Obstacle {
    private final Rectangle rectangle;
    private final GridPoint2 coordinates;

    public TreeLogicModel(ModelZooKeeper modelZooKeeper, Rectangle rectangle, GridPoint2 coordinates) {
        this.rectangle = rectangle;
        this.coordinates = coordinates;

        modelZooKeeper.notifyBorn(this, false);
    }

    @Override
    public boolean isOccupied(GridPoint2 point){
        return coordinates.equals(point);
    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }
}
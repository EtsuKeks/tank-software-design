package ru.mipt.bit.platformer.logicmodels;

import com.badlogic.gdx.math.GridPoint2;

public class TreeLogicModel implements Obstacle {
    private final GridPoint2 coordinates;

    public TreeLogicModel(GridPoint2 point) {
        this.coordinates = point;
    }

    @Override
    public boolean isOccupied(GridPoint2 point){
        return coordinates.equals(point);
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }
}
package ru.mipt.bit.platformer.logicmodels;

import com.badlogic.gdx.math.GridPoint2;

public class TreeLogicModel implements Obstacle {
    private final GridPoint2 coords;

    public TreeLogicModel(GridPoint2 coords) {
        this.coords = new GridPoint2(coords);
    }

    @Override
    public boolean isOccupied(GridPoint2 coords){
        return this.coords.equals(coords);
    }
}
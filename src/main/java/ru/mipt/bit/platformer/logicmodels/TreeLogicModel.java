package ru.mipt.bit.platformer.logicmodels;

import com.badlogic.gdx.math.GridPoint2;

public class TreeLogicModel {
    private final GridPoint2 coordinates;

    public TreeLogicModel(int startX, int startY) {
        this.coordinates = new GridPoint2(startX, startY);
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }
}
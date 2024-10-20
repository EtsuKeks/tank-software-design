package ru.mipt.bit.platformer.logicmodels;

import com.badlogic.gdx.math.GridPoint2;

public interface Obstacle {
    boolean isOccupied(GridPoint2 point);
}
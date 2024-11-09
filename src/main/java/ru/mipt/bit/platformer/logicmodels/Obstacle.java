package ru.mipt.bit.platformer.logicmodels;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

public interface Obstacle {
    boolean isOccupied(GridPoint2 point);
    Rectangle getRectangle();
}
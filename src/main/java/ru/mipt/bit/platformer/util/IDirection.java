package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.math.GridPoint2;

public interface IDirection {
    int getPrimaryKeyCode();
    int getSecondaryKeyCode();
    GridPoint2 getNextCoordinates(GridPoint2 current);
    float getRotation();
}
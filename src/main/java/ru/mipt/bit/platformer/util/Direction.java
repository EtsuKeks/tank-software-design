package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;

public enum Direction {
    UP(Input.Keys.UP, Input.Keys.W, 0, 1, 90f),
    DOWN(Input.Keys.DOWN, Input.Keys.S, 0, -1, -90f),
    LEFT(Input.Keys.LEFT, Input.Keys.A, -1, 0, 180f),
    RIGHT(Input.Keys.RIGHT, Input.Keys.D, 1, 0, 0f),
    NULL(Input.Keys.UNKNOWN, Input.Keys.UNKNOWN, 0, 0, 0f);

    private final int primaryKeyCode;
    private final int secondaryKeyCode;
    private final GridPoint2 directionVector;
    private final float rotation;

    Direction(int primaryKeyCode, int secondaryKeyCode, int deltaX, int deltaY, float rotation) {
        this.primaryKeyCode = primaryKeyCode;
        this.secondaryKeyCode = secondaryKeyCode;
        this.directionVector = new GridPoint2(deltaX, deltaY);
        this.rotation = rotation;
    }

    public int getPrimaryKeyCode() {
        return primaryKeyCode;
    }

    public int getSecondaryKeyCode() {
        return secondaryKeyCode;
    }

    public GridPoint2 getNextCoordinates(GridPoint2 current) {
        return new GridPoint2(current.x + directionVector.x, current.y + directionVector.y);
    }

    public float getRotation() {
        return rotation;
    }
}
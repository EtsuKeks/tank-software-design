package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;

public enum Direction {
    UP(Input.Keys.UP, Input.Keys.W, 0, 1, 90f),
    DOWN(Input.Keys.DOWN, Input.Keys.S, 0, -1, -90f),
    LEFT(Input.Keys.LEFT, Input.Keys.A, -1, 0, -180f),
    RIGHT(Input.Keys.RIGHT, Input.Keys.D, 1, 0, 0f);

    private final int keyCode;
    private final int secondaryKeyCode;
    private final int deltaX;
    private final int deltaY;
    private final float rotation;

    Direction(int keyCode, int secondaryKeyCode, int deltaX, int deltaY, float rotation) {
        this.keyCode = keyCode;
        this.secondaryKeyCode = secondaryKeyCode;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.rotation = rotation;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public int getSecondaryKeyCode() {
        return secondaryKeyCode;
    }

    public GridPoint2 getNextCoordinates(GridPoint2 current) {
        return new GridPoint2(current.x + deltaX, current.y + deltaY);
    }

    public float getRotation() {
        return rotation;
    }
}

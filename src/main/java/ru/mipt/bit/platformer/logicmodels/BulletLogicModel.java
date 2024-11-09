package ru.mipt.bit.platformer.logicmodels;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

public class BulletLogicModel implements Obstacle {
    @Override
    public boolean isOccupied(GridPoint2 point) {
        return false;
    }

    @Override
    public Rectangle getRectangle() {
        return null;
    }
}
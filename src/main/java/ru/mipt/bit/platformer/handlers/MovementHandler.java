package ru.mipt.bit.platformer.handlers;

import com.badlogic.gdx.Gdx;

import ru.mipt.bit.platformer.graphicmodels.TankGraphicModel;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.util.Direction;

public class MovementHandler extends Handler {
    private final TankGraphicModel tankGraphicModel;
    private final TankLogicModel tankLogicModel;

    public MovementHandler(TankGraphicModel tankGraphicModel, TankLogicModel tankLogicModel) {
        this.tankGraphicModel = tankGraphicModel;
        this.tankLogicModel = tankLogicModel;
    }

    @Override
    public void handleInput() {
        Direction targetDirection = Direction.NULL;
        for (Direction direction : Direction.values()) {
            if (Gdx.input.isKeyPressed(direction.getPrimaryKeyCode()) || Gdx.input.isKeyPressed(direction.getSecondaryKeyCode())) {
                targetDirection = direction;
            }
        }

        if (tankLogicModel.move(targetDirection)) {
            tankGraphicModel.setRotation(targetDirection.getRotation());
        }
    }
}
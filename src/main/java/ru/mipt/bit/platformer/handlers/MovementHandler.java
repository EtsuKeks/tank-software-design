package ru.mipt.bit.platformer.handlers;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.graphicmodels.IGraphicModel;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.IDirection;

public class MovementHandler implements Handler {
    private final IGraphicModel tankGraphicModel;
    private final TankLogicModel tankLogicModel;

    public MovementHandler(IGraphicModel tankGraphicModel, TankLogicModel tankLogicModel) {
        this.tankGraphicModel = tankGraphicModel;
        this.tankLogicModel = tankLogicModel;
    }

    @Override
    public void handleInput() {
        IDirection targetDirection = Direction.NULL;
        for (IDirection direction : Direction.values()) {
            if (Gdx.input.isKeyPressed(direction.getPrimaryKeyCode()) || Gdx.input.isKeyPressed(direction.getSecondaryKeyCode())) {
                targetDirection = direction;
            }
        }

        if (tankLogicModel.move(targetDirection)) {
            tankGraphicModel.setRotation(targetDirection.getRotation());
        }
    }
}

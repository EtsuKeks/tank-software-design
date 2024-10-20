package ru.mipt.bit.platformer.handlers;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.commands.MoveCommand;
import ru.mipt.bit.platformer.graphicmodels.IGraphicModel;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.IDirection;

public class MovementHandler implements Handler {
    private final MoveCommand moveCommand;

    public MovementHandler(IGraphicModel tankGraphicModel, TankLogicModel tankLogicModel) {
        moveCommand = new MoveCommand(tankGraphicModel, tankLogicModel, Direction.NULL);
    }

    @Override
    public void handleInput() {
        chooseDirection();
        moveCommand.execute();
    }

    private void chooseDirection() {
        IDirection targetDirection = Direction.NULL;
        for (IDirection direction : Direction.values()) {
            if (direction != Direction.NULL &&
                    (Gdx.input.isKeyPressed(direction.getPrimaryKeyCode()) ||
                            Gdx.input.isKeyPressed(direction.getSecondaryKeyCode()))) {
                targetDirection = direction;
            }
        }
        moveCommand.setDirection(targetDirection);
    }
}
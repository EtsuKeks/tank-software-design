package ru.mipt.bit.platformer.handlers;

import ru.mipt.bit.platformer.commands.MoveTankCommand;
import ru.mipt.bit.platformer.modelinitializers.ModelZooKeeper;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.IDirection;

import com.badlogic.gdx.Gdx;

public class MovementPlayerHandler implements Handler {
    private final MoveTankCommand moveTankCommand;

    public MovementPlayerHandler(ModelZooKeeper modelZooKeeper) {
        moveTankCommand = new MoveTankCommand(
                modelZooKeeper.getPlayerTankGraphicModel(),
                modelZooKeeper.getPlayerTankLogicModel(),
                Direction.NULL);
    }

    @Override
    public void handleInput() {
        chooseDirection();
        moveTankCommand.execute();
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
        moveTankCommand.setDirection(targetDirection);
    }
}
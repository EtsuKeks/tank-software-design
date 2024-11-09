package ru.mipt.bit.platformer.handlers;

import ru.mipt.bit.platformer.commands.MoveCommand;
import ru.mipt.bit.platformer.keepers.ModelZooKeeper;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.IDirection;

import com.badlogic.gdx.Gdx;

public class MovementHandler implements Handler {
    private final MoveCommand moveCommand;

    public MovementHandler(ModelZooKeeper modelZooKeeper) {
        moveCommand = new MoveCommand(
                modelZooKeeper.getPlayerTankGraphicModel(),
                modelZooKeeper.getPlayerTankLogicModel(),
                Direction.NULL);
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
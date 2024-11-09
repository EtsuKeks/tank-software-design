package ru.mipt.bit.platformer.handlers;

import ru.mipt.bit.platformer.commands.MoveCommand;
import ru.mipt.bit.platformer.keepers.ModelZooKeeper;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.IDirection;

import java.util.ArrayList;
import java.util.Collection;

import static com.badlogic.gdx.math.MathUtils.random;

public class MovementBotsHandler implements Handler {
    private final Collection<MoveCommand> moveCommands = new ArrayList<>();

    public MovementBotsHandler(ModelZooKeeper modelZooKeeper) {
        for (TankLogicModel botTankLogicModel: modelZooKeeper.getBotTankModels().keySet()) {
            moveCommands.add(new MoveCommand(modelZooKeeper.getBotTankModels().get(botTankLogicModel),
                            botTankLogicModel, Direction.NULL));
        }
    }

    @Override
    public void handleInput() {
        chooseDirections();
        for (MoveCommand moveCommand: moveCommands) {
            moveCommand.execute();
        }
    }

    private void chooseDirections() {
        Direction[] directions = Direction.values();
        for (MoveCommand moveCommand: moveCommands) {
            int randomIndex = random.nextInt(directions.length);
            IDirection targetDirection = directions[randomIndex];
            moveCommand.setDirection(targetDirection);
        }
    }
}
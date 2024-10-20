package ru.mipt.bit.platformer.handlers;

import ru.mipt.bit.platformer.commands.MoveCommand;
import ru.mipt.bit.platformer.graphicmodels.IGraphicModel;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.IDirection;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

public class RandomBotsHandler implements Handler {
    private final ArrayList<MoveCommand> moveCommands = new ArrayList<>();

    public RandomBotsHandler(ArrayList<IGraphicModel> botTankGraphicModels, ArrayList<TankLogicModel> botTankLogicModels) {
        if (botTankGraphicModels.size() != botTankLogicModels.size()) {
            throw new IllegalArgumentException("Got botGraphicModels and botTankLogicModels of different sizes!");
        }

        for (int i = 0; i < botTankGraphicModels.size(); i++) {
            moveCommands.add(new MoveCommand(botTankGraphicModels.get(i), botTankLogicModels.get(i), Direction.NULL));
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
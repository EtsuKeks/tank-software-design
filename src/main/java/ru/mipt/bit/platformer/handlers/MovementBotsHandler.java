package ru.mipt.bit.platformer.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.commands.MoveTankCommand;
import ru.mipt.bit.platformer.modelinitializers.ModelZooKeeper;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.IDirection;

import static com.badlogic.gdx.math.MathUtils.random;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class MovementBotsHandler implements Handler {
    private final ModelZooKeeper modelZooKeeper;

    @Autowired
    public MovementBotsHandler(ModelZooKeeper modelZooKeeper) {
        this.modelZooKeeper = modelZooKeeper;
    }

    @Override
    public void handleInput() {
        Collection<MoveTankCommand> moveTankCommands = new ArrayList<>();
        for (TankLogicModel botTankLogicModel: modelZooKeeper.getBotTankModels().keySet()) {
            moveTankCommands.add(new MoveTankCommand(modelZooKeeper.getBotTankModels().get(botTankLogicModel),
                    botTankLogicModel, Direction.NULL));
        }

        chooseDirections(moveTankCommands);
        for (MoveTankCommand moveTankCommand : moveTankCommands) {
            moveTankCommand.execute();
        }
    }

    private void chooseDirections(Collection<MoveTankCommand> moveTankCommands) {
        Direction[] directions = Direction.values();
        for (MoveTankCommand moveTankCommand : moveTankCommands) {
            int randomIndex = random.nextInt(directions.length);
            IDirection targetDirection = directions[randomIndex];
            moveTankCommand.setDirection(targetDirection);
        }
    }
}
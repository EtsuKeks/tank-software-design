package ru.mipt.bit.platformer.commands;

import static com.badlogic.gdx.math.MathUtils.random;

import ru.mipt.bit.platformer.keepers.BotTankModelKeeper;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.util.Direction;

public class BotTankMoveCommand implements Command {
    private final BotTankModelKeeper botTankModelKeeper;

    public BotTankMoveCommand(BotTankModelKeeper botTankModelKeeper) {
        this.botTankModelKeeper = botTankModelKeeper;
    }

    @Override
    public void execute() {
        Direction[] directions = Direction.values();
        for (TankLogicModel botTankLogicModels: botTankModelKeeper.getBotTankLogicModels()) {
            int randomIndex = random.nextInt(directions.length);
            Direction targetDirection = directions[randomIndex];
            botTankLogicModels.move(targetDirection);
        }

        botTankModelKeeper.commitKill();
    }
}
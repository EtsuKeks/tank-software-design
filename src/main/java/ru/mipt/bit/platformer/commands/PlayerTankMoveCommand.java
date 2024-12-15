package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.keepers.PlayerTankModelKeeper;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.util.Direction;

public class PlayerTankMoveCommand implements Command {
    private final TankLogicModel playerTankLogicModel;
    private final Direction direction;

    public PlayerTankMoveCommand(PlayerTankModelKeeper playerTankModelKeeper, Direction direction) {
        this.playerTankLogicModel = playerTankModelKeeper.getPlayerTankLogicModel();
        this.direction = direction;
    }

    @Override
    public void execute() {
        playerTankLogicModel.move(direction);
    }
}
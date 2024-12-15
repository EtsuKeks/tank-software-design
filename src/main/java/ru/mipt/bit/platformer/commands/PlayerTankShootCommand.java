package ru.mipt.bit.platformer.commands;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.keepers.BulletModelKeeper;
import ru.mipt.bit.platformer.keepers.PlayerTankModelKeeper;

public class PlayerTankShootCommand implements Command {
    private final BulletModelKeeper bulletModelKeeper;
    private final PlayerTankModelKeeper playerTankModelKeeper;

    public PlayerTankShootCommand(BulletModelKeeper bulletModelKeeper, PlayerTankModelKeeper playerTankModelKeeper) {
        this.bulletModelKeeper = bulletModelKeeper;
        this.playerTankModelKeeper = playerTankModelKeeper;
    }

    @Override
    public void execute() {
        GridPoint2 coords = playerTankModelKeeper.getPlayerTankLogicModel().getCoordinates();
        bulletModelKeeper.createModel(coords);
    }
}
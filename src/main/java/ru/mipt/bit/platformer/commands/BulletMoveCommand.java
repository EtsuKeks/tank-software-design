package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.keepers.BulletModelKeeper;
import ru.mipt.bit.platformer.logicmodels.BulletLogicModel;

public class BulletMoveCommand implements Command {
    private final BulletModelKeeper bulletModelKeeper;

    public BulletMoveCommand(BulletModelKeeper bulletModelKeeper) {
        this.bulletModelKeeper = bulletModelKeeper;
    }

    @Override
    public void execute() {
        for (BulletLogicModel bulletLogicModel: bulletModelKeeper.getBulletLogicModels()) {
            bulletLogicModel.move();
        }

        bulletModelKeeper.commitKill();
    }
}
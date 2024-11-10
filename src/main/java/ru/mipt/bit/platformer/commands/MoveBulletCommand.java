package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.logicmodels.BulletLogicModel;

public class MoveBulletCommand implements Command {
    private final BulletLogicModel bulletLogicModel;

    public MoveBulletCommand(BulletLogicModel bulletLogicModel) {
        this.bulletLogicModel = bulletLogicModel;
    }

    @Override
    public void execute() {
        bulletLogicModel.move();
    }
}
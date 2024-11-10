package ru.mipt.bit.platformer.handlers;

import ru.mipt.bit.platformer.commands.MoveBulletCommand;
import ru.mipt.bit.platformer.logicmodels.BulletLogicModel;
import ru.mipt.bit.platformer.modelinitializers.ModelZooKeeper;

import java.util.ArrayList;
import java.util.Collection;

public class MovementBulletHandler implements Handler {
    private final ModelZooKeeper modelZooKeeper;

    public MovementBulletHandler(ModelZooKeeper modelZooKeeper) {
        this.modelZooKeeper = modelZooKeeper;
    }

    @Override
    public void handleInput() {
        Collection<MoveBulletCommand> moveBulletCommands = new ArrayList<>();
        for (BulletLogicModel bulletLogicModel: modelZooKeeper.getBulletModels().keySet()) {
            moveBulletCommands.add(new MoveBulletCommand(bulletLogicModel));
        }

        for (MoveBulletCommand moveBulletCommand: moveBulletCommands) {
            moveBulletCommand.execute();
        }
    }
}
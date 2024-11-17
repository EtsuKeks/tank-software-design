package ru.mipt.bit.platformer.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mipt.bit.platformer.commands.MoveBulletCommand;
import ru.mipt.bit.platformer.logicmodels.BulletLogicModel;
import ru.mipt.bit.platformer.modelinitializers.ModelZooKeeper;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class MovementBulletHandler implements Handler {
    private final ModelZooKeeper modelZooKeeper;

    @Autowired
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
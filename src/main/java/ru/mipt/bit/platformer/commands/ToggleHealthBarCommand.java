package ru.mipt.bit.platformer.commands;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import ru.mipt.bit.platformer.graphicmodels.HealthBarDecorator;
import ru.mipt.bit.platformer.keepers.BotTankModelKeeper;
import ru.mipt.bit.platformer.keepers.GraphicModelKeeper;
import ru.mipt.bit.platformer.keepers.PlayerTankModelKeeper;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;

public class ToggleHealthBarCommand implements Command {
    private final AtomicReference<Boolean> healthBarVisible = new AtomicReference<>(true);

    public ToggleHealthBarCommand(PlayerTankModelKeeper playerTankModelKeeper,
                                  BotTankModelKeeper botTankModelKeeper, GraphicModelKeeper graphicModelKeeper) {
        TankLogicModel playerTankLogicModel = playerTankModelKeeper.getPlayerTankLogicModel();
        HealthBarDecorator playerTankGraphicModel = (HealthBarDecorator) graphicModelKeeper.graphicModels.get(playerTankLogicModel);
        playerTankGraphicModel.setHealthBarVisible(healthBarVisible);

        Collection<TankLogicModel> botTankLogicModels = botTankModelKeeper.getBotTankLogicModels();
        for (TankLogicModel botTankLogicModel: botTankLogicModels) {
            HealthBarDecorator botTankGraphicModel = (HealthBarDecorator) graphicModelKeeper.graphicModels.get(botTankLogicModel);
            botTankGraphicModel.setHealthBarVisible(healthBarVisible);
        }
    }

    @Override
    public void execute() {
        healthBarVisible.set(!healthBarVisible.get());
    }
}
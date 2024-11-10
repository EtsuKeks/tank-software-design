package ru.mipt.bit.platformer.handlers;

import ru.mipt.bit.platformer.commands.ToggleHealthBarCommand;
import ru.mipt.bit.platformer.graphicmodels.IGraphicModel;
import ru.mipt.bit.platformer.modelinitializers.ModelZooKeeper;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import java.util.Collection;

public class ToggleHealthBarHandler implements Handler {
    private final ToggleHealthBarCommand toggleHealthBarCommand;

    public ToggleHealthBarHandler(ModelZooKeeper modelZooKeeper) {
        Collection<IGraphicModel> tankGraphicModels = new ArrayList<>(modelZooKeeper.getBotTankModels().values());
        tankGraphicModels.add(modelZooKeeper.getPlayerTankGraphicModel());
        toggleHealthBarCommand = new ToggleHealthBarCommand(tankGraphicModels);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            toggleHealthBarCommand.execute();
        }
    }
}
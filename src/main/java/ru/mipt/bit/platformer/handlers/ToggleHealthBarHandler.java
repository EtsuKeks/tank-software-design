package ru.mipt.bit.platformer.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.commands.ToggleHealthBarCommand;
import ru.mipt.bit.platformer.graphicmodels.IGraphicModel;

import java.util.ArrayList;
import java.util.List;

public class ToggleHealthBarHandler implements Handler {
    private final ToggleHealthBarCommand toggleHealthBarCommand;

    public ToggleHealthBarHandler(List<IGraphicModel> botTankGraphicModels, IGraphicModel playerTankGraphicModel) {
        List<IGraphicModel> tankGraphicModels = new ArrayList<>(botTankGraphicModels);
        tankGraphicModels.add(playerTankGraphicModel);
        toggleHealthBarCommand = new ToggleHealthBarCommand(tankGraphicModels);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            toggleHealthBarCommand.execute();
        }
    }
}

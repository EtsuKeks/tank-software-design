package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.graphicmodels.HealthBarDecorator;
import ru.mipt.bit.platformer.graphicmodels.IGraphicModel;

import java.util.ArrayList;
import java.util.List;

public class ToggleHealthBarCommand implements Command {
    private final List<HealthBarDecorator> tankHealthBarDecorators = new ArrayList<>();

    public ToggleHealthBarCommand(List<IGraphicModel> tankGraphicModels) {
        for (IGraphicModel tankGraphicModel : tankGraphicModels) {
            if (tankGraphicModel instanceof HealthBarDecorator) {
                tankHealthBarDecorators.add((HealthBarDecorator) tankGraphicModel);
            }
        }
    }

    @Override
    public void execute() {
        for (HealthBarDecorator tankHealthBarDecorator : tankHealthBarDecorators) {
            tankHealthBarDecorator.toggleHealthBarVisibility();
        }
    }
}

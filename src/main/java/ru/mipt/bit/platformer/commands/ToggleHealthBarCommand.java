package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.graphicmodels.HealthBarDecorator;
import ru.mipt.bit.platformer.graphicmodels.IGraphicModel;

import java.util.ArrayList;
import java.util.Collection;

public class ToggleHealthBarCommand implements Command {
    private final Collection<IGraphicModel> tankGraphicModels;

    public ToggleHealthBarCommand(Collection<IGraphicModel> tankGraphicModels) {
        this.tankGraphicModels = tankGraphicModels;
    }

    @Override
    public void execute() {
        Collection<HealthBarDecorator> tankHealthBarDecorators = new ArrayList<>();
        for (IGraphicModel tankGraphicModel : tankGraphicModels) {
            if (tankGraphicModel instanceof HealthBarDecorator) {
                tankHealthBarDecorators.add((HealthBarDecorator) tankGraphicModel);
            }
        }

        for (HealthBarDecorator tankHealthBarDecorator : tankHealthBarDecorators) {
            tankHealthBarDecorator.toggleHealthBarVisibility();
        }
    }
}

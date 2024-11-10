package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.graphicmodels.IGraphicModel;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.util.IDirection;

public class MoveTankCommand implements Command {
    private final TankLogicModel tankLogicModel;
    private final IGraphicModel tankGraphicModel;
    private IDirection direction;

    public MoveTankCommand(IGraphicModel tankGraphicModel, TankLogicModel tankLogicModel, IDirection direction) {
        this.tankGraphicModel = tankGraphicModel;
        this.tankLogicModel = tankLogicModel;
        this.direction = direction;
    }

    public void setDirection(IDirection direction) {
        this.direction = direction;
    }

    @Override
    public void execute() {
        if (tankLogicModel.move(direction)) {
            tankGraphicModel.setRotation(direction.getRotation());
        }
    }
}
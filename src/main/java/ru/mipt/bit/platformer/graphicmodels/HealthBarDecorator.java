package ru.mipt.bit.platformer.graphicmodels;

import ru.mipt.bit.platformer.graphicmodels.texturefactories.TextureFactory;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

public class HealthBarDecorator implements IGraphicModel {
    private final TankGraphicModel tankGraphicModel;
    private final TankLogicModel tankLogicModel;
    private boolean healthBarVisible = true;

    public HealthBarDecorator(TankGraphicModel tankGraphicModel, TankLogicModel tankLogicModel) {
        this.tankGraphicModel = tankGraphicModel;
        this.tankLogicModel = tankLogicModel;
    }

    private void renderHealthBar(Batch batch) {
        float healthPercentage = tankLogicModel.getHealthRatio();

        float barWidth = tankGraphicModel.getRectangle().width;
        float barHeight = 5;
        float x = tankGraphicModel.getRectangle().x;
        float y = tankGraphicModel.getRectangle().y + tankGraphicModel.getRectangle().height + 5;

        batch.setColor(Color.RED);
        batch.draw(TextureFactory.getSolidColorTexture(Color.RED), x, y, barWidth, barHeight);

        batch.setColor(Color.GREEN);
        batch.draw(TextureFactory.getSolidColorTexture(Color.GREEN), x, y, barWidth * healthPercentage, barHeight);

        batch.setColor(Color.WHITE);
    }

    public void toggleHealthBarVisibility() {
        healthBarVisible = !healthBarVisible;
    }

    @Override
    public float getRotation() {
        return tankGraphicModel.getRotation();
    }

    @Override
    public void setRotation(float rotation) {
        tankGraphicModel.setRotation(rotation);
    }

    @Override
    public void dispose() {
        tankGraphicModel.dispose();
    }

    @Override
    public void render(Batch batch) {
        tankGraphicModel.render(batch);

        if (healthBarVisible) {
            renderHealthBar(batch);
        }
    }
}
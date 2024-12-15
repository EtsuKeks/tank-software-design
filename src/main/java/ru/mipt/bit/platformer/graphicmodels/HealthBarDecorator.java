package ru.mipt.bit.platformer.graphicmodels;

import ru.mipt.bit.platformer.graphicmodels.texturefactories.TextureFactory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.concurrent.atomic.AtomicReference;

public class HealthBarDecorator implements IGraphicModel {
    private final TankGraphicModel tankGraphicModel;
    private final AtomicReference<Float> healthRatio;
    private AtomicReference<Boolean> healthBarVisible = new AtomicReference<>(true);

    public HealthBarDecorator(TankGraphicModel tankGraphicModel, AtomicReference<Float> healthRatio) {
        this.tankGraphicModel = tankGraphicModel;
        this.healthRatio = healthRatio;
    }

    public void setHealthBarVisible(AtomicReference<Boolean> healthBarVisible) {
        this.healthBarVisible = healthBarVisible;
    }

    private void renderHealthBar(Batch batch) {
        float barWidth = tankGraphicModel.getRectangle().width;
        float barHeight = 5;
        float x = tankGraphicModel.getRectangle().x;
        float y = tankGraphicModel.getRectangle().y + tankGraphicModel.getRectangle().height + 5;

        batch.setColor(Color.RED);
        batch.draw(TextureFactory.getSolidColorTexture(Color.RED), x, y, barWidth, barHeight);

        batch.setColor(Color.GREEN);
        batch.draw(TextureFactory.getSolidColorTexture(Color.GREEN), x, y, barWidth * healthRatio.get(), barHeight);

        batch.setColor(Color.WHITE);
    }

    @Override
    public void render(Batch batch) {
        tankGraphicModel.render(batch);

        if (healthBarVisible.get()) {
            renderHealthBar(batch);
        }
    }

    @Override
    public void dispose() {
        tankGraphicModel.dispose();
    }
}
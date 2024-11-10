package ru.mipt.bit.platformer.graphicmodels;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface IGraphicModel {
    void render(Batch batch);
    void dispose();
    float getRotation();
    void setRotation(float rotation);
}
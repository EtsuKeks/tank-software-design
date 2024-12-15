package ru.mipt.bit.platformer.keepers;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.graphicmodels.IGraphicModel;

import java.util.HashMap;
import java.util.Map;

public class GraphicModelKeeper {
    public final Map<Object, IGraphicModel> graphicModels = new HashMap<>();

    public void render(Batch batch) {
        for (IGraphicModel graphicModel: graphicModels.values()) {
            graphicModel.render(batch);
        }
    }

    public void dispose() {
        for (IGraphicModel graphicModel: graphicModels.values()) {
            graphicModel.dispose();
        }
    }
}

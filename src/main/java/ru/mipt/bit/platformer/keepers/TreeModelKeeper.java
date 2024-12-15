package ru.mipt.bit.platformer.keepers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.graphicmodels.TreeGraphicModel;
import ru.mipt.bit.platformer.logicmodels.TreeLogicModel;

public class TreeModelKeeper extends ModelCreator {
    private final GameConfig gameConfig;
    private final ObstacleModelKeeper obstacleModelKeeper;
    private final GraphicModelKeeper graphicModelKeeper;
    private final TiledMapTileLayer tiledMapTileLayer;

    @Autowired
    public TreeModelKeeper(GameConfig gameConfig, ObstacleModelKeeper obstacleModelKeeper,
                             GraphicModelKeeper graphicModelKeeper, TiledMapTileLayer tiledMapTileLayer) {
        this.gameConfig = gameConfig;
        this.obstacleModelKeeper = obstacleModelKeeper;
        this.graphicModelKeeper = graphicModelKeeper;
        this.tiledMapTileLayer = tiledMapTileLayer;
    }

    @Override
    public void createModel(GridPoint2 coords) {
        Rectangle treeRectangle = initRectangle(tiledMapTileLayer, gameConfig.treeModelConfig.texturePath, coords);
        TreeLogicModel treeLogicModel = new TreeLogicModel(coords);
        obstacleModelKeeper.obstacleModels.add(treeLogicModel);
        graphicModelKeeper.graphicModels.put(treeLogicModel, new TreeGraphicModel(treeRectangle,
                gameConfig.treeModelConfig.texturePath));
    }
}
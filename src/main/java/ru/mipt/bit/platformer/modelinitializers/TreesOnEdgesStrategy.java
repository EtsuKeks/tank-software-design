package ru.mipt.bit.platformer.modelinitializers;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.keepers.*;

public class TreesOnEdgesStrategy extends RandomCoordsGenerator implements  Strategy{
    private final TreeModelKeeper treeModelKeeper;

    public TreesOnEdgesStrategy(GameConfig gameConfig, ObstacleModelKeeper obstacleModelKeeper,
                                              TreeModelKeeper treeModelKeeper) {
        super(gameConfig, obstacleModelKeeper);
        this.treeModelKeeper = treeModelKeeper;
    }

    public void initialize() {
        treeModelKeeper.createModel(new GridPoint2(-1, -1));
        treeModelKeeper.createModel(new GridPoint2(-1, gameConfig.levelConfig.mapSize.height));
        treeModelKeeper.createModel(new GridPoint2(gameConfig.levelConfig.mapSize.width, -1));
        treeModelKeeper.createModel(new GridPoint2(gameConfig.levelConfig.mapSize.width, gameConfig.levelConfig.mapSize.height));

        for (int i = 0; i < gameConfig.levelConfig.mapSize.height; i++) {
            treeModelKeeper.createModel(new GridPoint2(-1, i));
            treeModelKeeper.createModel(new GridPoint2(gameConfig.levelConfig.mapSize.width, i));
        }

        for (int i = 0; i < gameConfig.levelConfig.mapSize.width; i++) {
            treeModelKeeper.createModel(new GridPoint2(i, -1));
            treeModelKeeper.createModel(new GridPoint2(i, gameConfig.levelConfig.mapSize.height));
        }
    }
}
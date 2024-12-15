package ru.mipt.bit.platformer.modelinitializers;

import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.keepers.*;

import java.util.Random;

public class TreesAndPlayerTankOnRandomStrategy extends RandomCoordsGenerator implements  Strategy{
    private final TreeModelKeeper treeModelKeeper;
    private final PlayerTankModelKeeper playerTankModelKeeper;

    public TreesAndPlayerTankOnRandomStrategy(GameConfig gameConfig, ObstacleModelKeeper obstacleModelKeeper,
                                    TreeModelKeeper treeModelKeeper, PlayerTankModelKeeper playerTankModelKeeper) {
        super(gameConfig, obstacleModelKeeper);
        this.treeModelKeeper = treeModelKeeper;
        this.playerTankModelKeeper = playerTankModelKeeper;
    }

    public void initialize() {
        Random random = new Random();
        int numTrees = random.nextInt(gameConfig.levelConfig.treesMaxCount - gameConfig.levelConfig.treesMinCount) +
                gameConfig.levelConfig.treesMinCount;
        for (int i = 0; i < numTrees; i++) {
            treeModelKeeper.createModel(generateRandomCoords());
        }

        playerTankModelKeeper.createModel(generateRandomCoords());
    }
}
package ru.mipt.bit.platformer.modelinitializers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.keepers.*;

@Service
public class ModelsInitializer {
    @Autowired
    public ModelsInitializer(GameConfig gameConfig, BotTankModelKeeper botTankModelKeeper,
                             PlayerTankModelKeeper playerTankModelKeeper, TreeModelKeeper treeModelKeeper,
                             ObstacleModelKeeper obstacleModelKeeper) throws IOException {
        new TreesOnEdgesStrategy(gameConfig, obstacleModelKeeper, treeModelKeeper).initialize();

        if (gameConfig.levelConfig.levelType.equals("file")) {
            new TreesAndPlayerTankFromFileStrategy(gameConfig, obstacleModelKeeper,
                    treeModelKeeper, playerTankModelKeeper).initialize();
        } else if (gameConfig.levelConfig.levelType.equals("random")) {
            new TreesAndPlayerTankOnRandomStrategy(gameConfig, obstacleModelKeeper,
                    treeModelKeeper, playerTankModelKeeper).initialize();
        }

        new TankBotsOnRandomStrategy(gameConfig, obstacleModelKeeper, botTankModelKeeper).initialize();
    }
}
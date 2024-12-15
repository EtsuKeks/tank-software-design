package ru.mipt.bit.platformer.modelinitializers;

import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.keepers.*;

import java.util.Random;

public class TankBotsOnRandomStrategy extends RandomCoordsGenerator implements  Strategy{
    private final BotTankModelKeeper botTankModelKeeper;

    public TankBotsOnRandomStrategy(GameConfig gameConfig, ObstacleModelKeeper obstacleModelKeeper,
                                    BotTankModelKeeper botTankModelKeeper) {
        super(gameConfig, obstacleModelKeeper);
        this.botTankModelKeeper = botTankModelKeeper;
    }

    public void initialize() {
        Random random = new Random();
        int numBotTanks = random.nextInt(gameConfig.levelConfig.botTanksMaxCount - gameConfig.levelConfig.botTanksMinCount) +
                gameConfig.levelConfig.botTanksMinCount;

        for (int i = 0; i < numBotTanks; i++) {
            botTankModelKeeper.createModel(generateRandomCoords());
        }
    }
}
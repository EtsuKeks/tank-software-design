package ru.mipt.bit.platformer.modelinitializers;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.keepers.ObstacleModelKeeper;
import ru.mipt.bit.platformer.logicmodels.Obstacle;

import java.util.Collection;
import java.util.Random;

public class RandomCoordsGenerator {
    protected final GameConfig gameConfig;
    private final Collection<Obstacle> obstacles;

    public RandomCoordsGenerator(GameConfig gameConfig, ObstacleModelKeeper obstacleModelKeeper) {
        this.gameConfig = gameConfig;
        this.obstacles = obstacleModelKeeper.obstacleModels;
    }

    protected GridPoint2 generateRandomCoords() {
        Random random = new Random();
        GridPoint2 coords;
        do {
            coords = new GridPoint2(random.nextInt(gameConfig.levelConfig.mapSize.width), random.nextInt(gameConfig.levelConfig.mapSize.height));
        } while (isOccupied(coords));

        return coords;
    }

    private boolean isOccupied(GridPoint2 position) {
        for (Obstacle obstacle: obstacles) {
            if (obstacle.isOccupied(position)) {
                return true;
            }
        }
        return false;
    }
}

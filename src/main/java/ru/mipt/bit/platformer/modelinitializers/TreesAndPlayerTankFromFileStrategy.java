package ru.mipt.bit.platformer.modelinitializers;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.config.ConfigLoader;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.keepers.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TreesAndPlayerTankFromFileStrategy extends RandomCoordsGenerator implements  Strategy{
    private final TreeModelKeeper treeModelKeeper;
    private final PlayerTankModelKeeper playerTankModelKeeper;

    public TreesAndPlayerTankFromFileStrategy(GameConfig gameConfig, ObstacleModelKeeper obstacleModelKeeper,
                                              TreeModelKeeper treeModelKeeper, PlayerTankModelKeeper playerTankModelKeeper) {
        super(gameConfig, obstacleModelKeeper);
        this.treeModelKeeper = treeModelKeeper;
        this.playerTankModelKeeper = playerTankModelKeeper;
    }

    public void initialize() throws IOException {
        InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(gameConfig.levelConfig.levelDescPath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! " + gameConfig.levelConfig.levelDescPath);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        int y = gameConfig.levelConfig.mapSize.height - 1;
        while ((line = reader.readLine()) != null && y >= 0) {
            for (int x = 0; x < Math.min(line.length(), gameConfig.levelConfig.mapSize.width); x++) {
                char cell = line.charAt(x);

                switch (cell) {
                    case 'T':
                        treeModelKeeper.createModel(new GridPoint2(x, y));
                        break;
                    case 'X':
                        playerTankModelKeeper.createModel(new GridPoint2(x, y));
                        break;
                    case '_':
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid character in level file: " + cell);
                }
            }
            y--;
        }
        reader.close();
    }
}
package ru.mipt.bit.platformer.util;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mipt.bit.platformer.commands.Handler;
import ru.mipt.bit.platformer.config.ConfigLoader;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.keepers.*;
import ru.mipt.bit.platformer.modelinitializers.ModelsInitializer;

import java.io.IOException;

@Configuration
public class AppConfig {
    @Bean
    public GameConfig gameConfig() throws IOException {
        return ConfigLoader.loadConfig("config");
    }

    @Bean
    public SpriteBatch spriteBatch() {
        return new SpriteBatch();
    }

    @Bean
    public TiledMap tiledMap() {
        return new TmxMapLoader().load("level.tmx");
    }

    @Bean
    public MapRenderer mapRenderer(TiledMap tiledMap, SpriteBatch spriteBatch) {
        return createSingleLayerMapRenderer(tiledMap, spriteBatch);
    }

    @Bean
    public TiledMapTileLayer tiledMapTileLayer(TiledMap tiledMap) {
        return getSingleLayer(tiledMap);
    }

    @Bean
    public TileMovement tileMovement(TiledMapTileLayer tiledMapTileLayer) {
        return new TileMovement(tiledMapTileLayer, Interpolation.smooth);
    }

    @Bean
    public ObstacleModelKeeper obstacleModelKeeper() {
        return new ObstacleModelKeeper();
    }

    @Bean
    public GraphicModelKeeper graphicModelKeeper() {
        return new GraphicModelKeeper();
    }

    @Bean
    public PlayerTankModelKeeper playerTankModelKeeper(GameConfig gameConfig, ObstacleModelKeeper obstacleModelKeeper,
                                                       GraphicModelKeeper graphicModelKeeper, TiledMapTileLayer tiledMapTileLayer,
                                                       TileMovement tileMovement) {
        return new PlayerTankModelKeeper(gameConfig, obstacleModelKeeper, graphicModelKeeper, tiledMapTileLayer, tileMovement);
    }

    @Bean
    public BotTankModelKeeper botTankModelKeeper(GameConfig gameConfig, ObstacleModelKeeper obstacleModelKeeper,
                                                 GraphicModelKeeper graphicModelKeeper, TiledMapTileLayer tiledMapTileLayer,
                                                 TileMovement tileMovement) {
        return new BotTankModelKeeper(gameConfig, obstacleModelKeeper, graphicModelKeeper, tiledMapTileLayer, tileMovement);
    }

    @Bean
    public BulletModelKeeper bulletModelKeeper(GameConfig gameConfig, PlayerTankModelKeeper playerTankModelKeeper,
                                               ObstacleModelKeeper obstacleModelKeeper, GraphicModelKeeper graphicModelKeeper,
                                               TiledMapTileLayer tiledMapTileLayer, TileMovement tileMovement) {
        return new BulletModelKeeper(gameConfig, playerTankModelKeeper, obstacleModelKeeper, graphicModelKeeper,
                tiledMapTileLayer, tileMovement);
    }

    @Bean
    public TreeModelKeeper treeModelKeeper(GameConfig gameConfig, ObstacleModelKeeper obstacleModelKeeper,
                                           GraphicModelKeeper graphicModelKeeper, TiledMapTileLayer tiledMapTileLayer) {
        return new TreeModelKeeper(gameConfig, obstacleModelKeeper, graphicModelKeeper, tiledMapTileLayer);
    }

    @Bean
    public ModelsInitializer modelsInitializer(GameConfig gameConfig, BotTankModelKeeper botTankModelKeeper,
                                               PlayerTankModelKeeper playerTankModelKeeper, TreeModelKeeper treeModelKeeper,
                                               ObstacleModelKeeper obstacleModelKeeper) throws IOException {
        return new ModelsInitializer(gameConfig, botTankModelKeeper, playerTankModelKeeper,
                treeModelKeeper, obstacleModelKeeper);
    }

    @Bean
    public Handler handler(BotTankModelKeeper botTankModelKeeper, BulletModelKeeper bulletModelKeeper,
                   PlayerTankModelKeeper playerTankModelKeeper, GraphicModelKeeper graphicModelKeeper) {
        return new Handler(botTankModelKeeper, bulletModelKeeper, playerTankModelKeeper, graphicModelKeeper);
    }
}
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
import ru.mipt.bit.platformer.handlers.*;
import ru.mipt.bit.platformer.modelinitializers.ModelZooKeeper;
import java.io.IOException;

@Configuration
public class AppConfig {
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
    public ModelZooKeeper modelZooKeeper(TiledMapTileLayer tiledMapTileLayer, TileMovement tileMovement) throws IOException {
        return new ModelZooKeeper(tiledMapTileLayer, tileMovement);
    }

    @Bean
    public ToggleHealthBarHandler toggleHealthBarHandler(ModelZooKeeper modelZooKeeper) {
        return new ToggleHealthBarHandler(modelZooKeeper);
    }

    @Bean
    public MovementPlayerHandler movementPlayerHandler(ModelZooKeeper modelZooKeeper) {
        return new MovementPlayerHandler(modelZooKeeper);
    }

    @Bean
    public MovementBotsHandler movementBotsHandler(ModelZooKeeper modelZooKeeper) {
        return new MovementBotsHandler(modelZooKeeper);
    }

    @Bean
    public ShootPlayerHandler shootPlayerHandler(ModelZooKeeper modelZooKeeper, TiledMapTileLayer tiledMapTileLayer, TileMovement tileMovement) {
        return new ShootPlayerHandler(modelZooKeeper, tiledMapTileLayer, tileMovement);
    }

    @Bean MovementBulletHandler movementBulletHandler(ModelZooKeeper modelZooKeeper) {
        return new MovementBulletHandler(modelZooKeeper);
    }
}
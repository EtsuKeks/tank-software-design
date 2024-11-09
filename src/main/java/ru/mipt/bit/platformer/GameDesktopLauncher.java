package ru.mipt.bit.platformer;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import ru.mipt.bit.platformer.handlers.*;
import ru.mipt.bit.platformer.keepers.ModelZooKeeper;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameDesktopLauncher implements ApplicationListener {
    private Batch batch;
    private TiledMap level;
    private MapRenderer levelRenderer;

    private ModelZooKeeper modelZooKeeper;
    private final List<Handler> handlers = new ArrayList<>();

    @Override
    public void create() {
        batch = new SpriteBatch();
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);

        TiledMapTileLayer groundLayer = getSingleLayer(level);
        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        try {
            modelZooKeeper = new ModelZooKeeper("config.json", groundLayer, tileMovement);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        handlers.add(new ToggleHealthBarHandler(modelZooKeeper));
        handlers.add(new MovementPlayerHandler(modelZooKeeper));
        handlers.add(new MovementBotsHandler(modelZooKeeper));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        handleInput();

        levelRenderer.render();

        batch.begin();

        modelZooKeeper.render(batch);

        batch.end();
    }

    private void handleInput() {
        for (Handler handler: handlers) {
            handler.handleInput();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        modelZooKeeper.dispose();
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
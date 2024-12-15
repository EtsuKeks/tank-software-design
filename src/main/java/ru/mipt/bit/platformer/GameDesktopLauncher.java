package ru.mipt.bit.platformer;

import ru.mipt.bit.platformer.commands.Handler;
import ru.mipt.bit.platformer.keepers.GraphicModelKeeper;
import ru.mipt.bit.platformer.modelinitializers.ModelsInitializer;
import ru.mipt.bit.platformer.util.AppConfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;

import java.io.IOException;

public class GameDesktopLauncher implements ApplicationListener {
    private Batch batch;
    private TiledMap level;
    private MapRenderer levelRenderer;
    private GraphicModelKeeper graphicModelKeeper;
    private Handler handler;

    @Override
    public void create() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        batch = context.getBean(SpriteBatch.class);
        level = context.getBean(TiledMap.class);
        levelRenderer = context.getBean(MapRenderer.class);

        graphicModelKeeper = context.getBean(GraphicModelKeeper.class);
        handler = context.getBean(Handler.class);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        handleInput();

        levelRenderer.render();

        batch.begin();

        graphicModelKeeper.render(batch);

        batch.end();
    }

    private void handleInput() {
        handler.handleInput();
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
        graphicModelKeeper.dispose();
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
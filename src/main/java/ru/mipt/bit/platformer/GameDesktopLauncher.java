package ru.mipt.bit.platformer;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import ru.mipt.bit.platformer.graphicmodels.*;
import ru.mipt.bit.platformer.handlers.*;
import ru.mipt.bit.platformer.logicmodels.*;
import ru.mipt.bit.platformer.util.GdxGameUtils;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;

public class GameDesktopLauncher implements ApplicationListener {
    private Batch batch;
    private TiledMap level;
    private MapRenderer levelRenderer;

    private TankGraphicModel tankGraphicModel;
    private TankLogicModel tankLogicModel;
    
    private final ArrayList<TreeGraphicModel> treeGraphicModels = new ArrayList<>();
    private final ArrayList<TreeLogicModel> treeLogicModels = new ArrayList<>();
    private final ArrayList<Handler> handlers = new ArrayList<>();

    @Override
    public void create() {
        batch = new SpriteBatch();

        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        createTrees("images/greenTree.png", groundLayer, new GridPoint2[] {
                                                                        new GridPoint2(1, 3), 
                                                                        new GridPoint2(3, 4)}
                                                                        );
        createTank("images/tank_blue.png", groundLayer, tileMovement, 1, 1, treeLogicModels);
        handlers.add(new MovementHandler(tankGraphicModel, tankLogicModel));
    }

    private void createTank(String texturePath, TiledMapTileLayer layer, TileMovement tileMovement, int startX, int startY, ArrayList<TreeLogicModel> obstacles) {
        Texture texture = new Texture(texturePath);
        TextureRegion graphics = new TextureRegion(texture);
        Rectangle rectangle = GdxGameUtils.createBoundingRectangle(graphics);
        GdxGameUtils.moveRectangleAtTileCenter(layer, rectangle, new GridPoint2(startX, startY));
        
        tankGraphicModel = new TankGraphicModel(rectangle, texturePath);
        tankLogicModel = new TankLogicModel(rectangle, tileMovement, startX, startY, obstacles);
    }

    private void createTrees(String texturePath, TiledMapTileLayer layer, GridPoint2[] startCoordinates) {
        for (GridPoint2 coordinate: startCoordinates) {
            Texture texture = new Texture(texturePath);
            TextureRegion graphics = new TextureRegion(texture);
            Rectangle rectangle = GdxGameUtils.createBoundingRectangle(graphics);
            GdxGameUtils.moveRectangleAtTileCenter(layer, rectangle, new GridPoint2(coordinate.x, coordinate.y));

            treeGraphicModels.add(new TreeGraphicModel(rectangle, texturePath, layer));
            treeLogicModels.add(new TreeLogicModel(coordinate.x, coordinate.y));
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        handleInput();

        levelRenderer.render();

        batch.begin();

        tankGraphicModel.render(batch);

        for (TreeGraphicModel treeGraphicModel: treeGraphicModels) {
            treeGraphicModel.render(batch);
        }

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
        tankGraphicModel.dispose();

        for (TreeGraphicModel treeGraphicModel: treeGraphicModels) {
            treeGraphicModel.dispose();
        }

        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}

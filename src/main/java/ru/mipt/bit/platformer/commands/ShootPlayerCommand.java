package ru.mipt.bit.platformer.commands;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.graphicmodels.IGraphicModel;
import ru.mipt.bit.platformer.logicmodels.BulletLogicModel;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.modelinitializers.ModelZooKeeper;
import ru.mipt.bit.platformer.util.GdxGameUtils;
import ru.mipt.bit.platformer.util.TileMovement;

public class ShootPlayerCommand implements Command {
    public final GameConfig config;
    private final ModelZooKeeper modelZooKeeper;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;

    public ShootPlayerCommand(ModelZooKeeper modelZooKeeper,
                              TiledMapTileLayer groundLayer, TileMovement tileMovement) {
        config = modelZooKeeper.getConfig();
        this.modelZooKeeper = modelZooKeeper;
        this.groundLayer = groundLayer;
        this.tileMovement = tileMovement;
    }

    @Override
    public void execute() {
        initBullet(new GridPoint2(modelZooKeeper.getPlayerTankLogicModel().getCoordinates()));
    }

    private void initBullet(GridPoint2 coords) {
        Rectangle bulletRectangle = initRectangle(groundLayer, config.bulletTexturePath, coords);
        IGraphicModel playerTankGraphicModel = modelZooKeeper.getPlayerTankGraphicModel();
        TankLogicModel playerTankLogicModel = modelZooKeeper.getPlayerTankLogicModel();
        new BulletLogicModel(modelZooKeeper, bulletRectangle, tileMovement, config.bulletMovementSpeed,
                playerTankLogicModel.getCoordinates(), playerTankLogicModel, config.bulletDefaultDamage, playerTankGraphicModel.getRotation());
    }

    private Rectangle initRectangle(TiledMapTileLayer groundLayer, String texturePath, GridPoint2 coordinate) {
        Texture texture = new Texture(texturePath);
        TextureRegion graphics = new TextureRegion(texture);
        Rectangle rectangle = GdxGameUtils.createBoundingRectangle(graphics);
        GdxGameUtils.moveRectangleAtTileCenter(groundLayer, rectangle, coordinate);
        return rectangle;
    }
}
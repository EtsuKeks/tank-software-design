package ru.mipt.bit.platformer.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.springframework.beans.factory.annotation.Autowired;

import ru.mipt.bit.platformer.keepers.BotTankModelKeeper;
import ru.mipt.bit.platformer.keepers.BulletModelKeeper;
import ru.mipt.bit.platformer.keepers.GraphicModelKeeper;
import ru.mipt.bit.platformer.keepers.PlayerTankModelKeeper;
import ru.mipt.bit.platformer.util.Direction;

public class Handler {
    private final BotTankMoveCommand botTankMoveCommand;
    private final BulletMoveCommand bulletMoveCommand;
    private final PlayerTankMoveCommand playerTankMoveNullCommand;
    private final PlayerTankMoveCommand playerTankMoveUpCommand;
    private final PlayerTankMoveCommand playerTankMoveDownCommand;
    private final PlayerTankMoveCommand playerTankMoveRightCommand;
    private final PlayerTankMoveCommand playerTankMoveLeftCommand;
    private final PlayerTankShootCommand playerTankShootCommand;
    private final ToggleHealthBarCommand toggleHealthBarCommand;


    @Autowired
    public Handler(BotTankModelKeeper botTankModelKeeper, BulletModelKeeper bulletModelKeeper,
                   PlayerTankModelKeeper playerTankModelKeeper, GraphicModelKeeper graphicModelKeeper) {
        this.botTankMoveCommand = new BotTankMoveCommand(botTankModelKeeper);
        this.bulletMoveCommand = new BulletMoveCommand(bulletModelKeeper);
        this.playerTankMoveNullCommand = new PlayerTankMoveCommand(playerTankModelKeeper, Direction.NULL);
        this.playerTankMoveUpCommand = new PlayerTankMoveCommand(playerTankModelKeeper, Direction.UP);
        this.playerTankMoveDownCommand = new PlayerTankMoveCommand(playerTankModelKeeper, Direction.DOWN);
        this.playerTankMoveRightCommand = new PlayerTankMoveCommand(playerTankModelKeeper, Direction.RIGHT);
        this.playerTankMoveLeftCommand = new PlayerTankMoveCommand(playerTankModelKeeper, Direction.LEFT);
        this.playerTankShootCommand = new PlayerTankShootCommand(bulletModelKeeper, playerTankModelKeeper);
        this.toggleHealthBarCommand = new ToggleHealthBarCommand(playerTankModelKeeper, botTankModelKeeper,
                graphicModelKeeper);
    }

    public void handleInput() {
        botTankMoveCommand.execute();
        bulletMoveCommand.execute();
        handlePlayerTankMoveCommand();
        handlePlayerTankShootCommand();
        handleToggleHealthBarCommand();
    }

    private void handlePlayerTankMoveCommand() {
        Direction playerTargetDirection = Direction.NULL;
        for (Direction direction: Direction.values()) {
            if (direction != Direction.NULL &&
                    (Gdx.input.isKeyJustPressed(direction.getPrimaryKeyCode()) ||
                            Gdx.input.isKeyJustPressed(direction.getSecondaryKeyCode()))) {
                playerTargetDirection = direction;
            }
        }

        switch(playerTargetDirection) {
            case Direction.UP -> playerTankMoveUpCommand.execute();
            case Direction.DOWN -> playerTankMoveDownCommand.execute();
            case Direction.RIGHT -> playerTankMoveRightCommand.execute();
            case Direction.LEFT -> playerTankMoveLeftCommand.execute();
            case Direction.NULL -> playerTankMoveNullCommand.execute();
        }
    }

    private void handlePlayerTankShootCommand() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            playerTankShootCommand.execute();
        }
    }

    private void handleToggleHealthBarCommand() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            toggleHealthBarCommand.execute();
        }
    }
}
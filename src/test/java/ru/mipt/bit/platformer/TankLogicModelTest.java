package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.Before;
import org.junit.Test;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.logicmodels.TreeLogicModel;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TankLogicModelTest {
    private TankLogicModel tankLogicModel;
    private TileMovement tileMovement;
    private Rectangle rectangle;
    private ArrayList<TreeLogicModel> obstacles;

    @Before
    public void setUp() {
        Graphics graphics = mock(Graphics.class);
        when(graphics.getDeltaTime()).thenReturn(1f);
        Gdx.graphics = graphics;

        rectangle = new Rectangle(0, 0, 1, 1);
        tileMovement = mock(TileMovement.class);
        obstacles = new ArrayList<>();
        obstacles.add(new TreeLogicModel(2, 2));
        tankLogicModel = new TankLogicModel(rectangle, tileMovement, 1, 1, obstacles);
    }

    @Test
    public void testMoveAcceptable() {
        assertTrue(tankLogicModel.move(Direction.RIGHT));
        assertEquals(new GridPoint2(2, 1), tankLogicModel.getCoordinates());
    }

    @Test
    public void testMoveNotAcceptable() {
        tankLogicModel.move(Direction.RIGHT);
        assertFalse(tankLogicModel.move(Direction.UP));
        assertEquals(new GridPoint2(2, 1), tankLogicModel.getCoordinates());
    }

    @Test
    public void testMovementToObstacle() {
        tankLogicModel.move(Direction.UP);
        assertFalse(tankLogicModel.move(Direction.RIGHT));
        assertEquals(new GridPoint2(1, 2), tankLogicModel.getCoordinates());
    }
}
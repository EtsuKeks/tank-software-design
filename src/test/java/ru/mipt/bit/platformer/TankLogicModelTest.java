package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.Before;
import org.junit.Test;
import ru.mipt.bit.platformer.logicmodels.Obstacle;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.logicmodels.TreeLogicModel;
import ru.mipt.bit.platformer.util.Direction;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TankLogicModelTest {
    private TankLogicModel tankLogicModel;
    private TileMovement tileMovement;
    private Rectangle rectangle;
    private ArrayList<Obstacle> obstacles;

    @Before
    public void setUp() {
        Graphics graphics = mock(Graphics.class);
        when(graphics.getDeltaTime()).thenReturn(1f);
        Gdx.graphics = graphics;

        rectangle = new Rectangle(0, 0, 1, 1);
        tileMovement = mock(TileMovement.class);
        obstacles = new ArrayList<>();
        obstacles.add(new TreeLogicModel(new GridPoint2(2, 2)));
        tankLogicModel = new TankLogicModel(rectangle, tileMovement, 0.4f, new GridPoint2(1, 1), obstacles);
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
    public void testMoveWithNoObstacles() {
        obstacles.clear();
        assertTrue(tankLogicModel.move(Direction.UP));
        assertEquals(new GridPoint2(1, 2), tankLogicModel.getCoordinates());

        assertTrue(tankLogicModel.move(Direction.UP));
        assertEquals(new GridPoint2(1, 3), tankLogicModel.getCoordinates());
    }

    @Test
    public void testContinuousMovement() {
        tankLogicModel.move(Direction.RIGHT);
        assertEquals(new GridPoint2(2, 1), tankLogicModel.getCoordinates());

        tankLogicModel.move(Direction.NULL);
        assertEquals(new GridPoint2(2, 1), tankLogicModel.getCoordinates());

        tankLogicModel.move(Direction.NULL);
        assertEquals(new GridPoint2(2, 1), tankLogicModel.getCoordinates());

        assertFalse(tankLogicModel.move(Direction.UP));
        assertEquals(new GridPoint2(2, 1), tankLogicModel.getCoordinates());
    }
}

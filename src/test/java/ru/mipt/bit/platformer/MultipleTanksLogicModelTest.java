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

public class MultipleTanksLogicModelTest {
    private ArrayList<TankLogicModel> tanks;
    private TileMovement tileMovement;
    private ArrayList<Obstacle> obstacles;

    @Before
    public void setUp() {
        Graphics graphics = mock(Graphics.class);
        when(graphics.getDeltaTime()).thenReturn(1f);
        Gdx.graphics = graphics;
        tileMovement = mock(TileMovement.class);

        obstacles = new ArrayList<>();
        TankLogicModel tank1 = new TankLogicModel(new Rectangle(0, 0, 1, 1), tileMovement, 0.4f, new GridPoint2(1, 1), obstacles);
        TankLogicModel tank2 = new TankLogicModel(new Rectangle(0, 0, 1, 1), tileMovement, 0.4f, new GridPoint2(1, 2), obstacles);
        obstacles.add(new TreeLogicModel(new GridPoint2(2, 2)));
        obstacles.add(tank1);
        obstacles.add(tank2);
        tanks = new ArrayList<>();
        tanks.add(new TankLogicModel(new Rectangle(0, 0, 1, 1), tileMovement, 0.4f, new GridPoint2(1, 1), obstacles));
        tanks.add(new TankLogicModel(new Rectangle(0, 0, 1, 1), tileMovement, 0.4f, new GridPoint2(1, 2), obstacles));
    }

    @Test
    public void testTankMovementWithoutCollision() {
        assertTrue(tanks.get(0).move(Direction.RIGHT));
        assertEquals(new GridPoint2(2, 1), tanks.get(0).getCoordinates());

        assertFalse(tanks.get(1).move(Direction.RIGHT));
        assertEquals(new GridPoint2(1, 2), tanks.get(1).getCoordinates());
    }

    @Test
    public void testTankMovementWithCollision() {
        tanks.get(0).move(Direction.RIGHT);

        assertFalse(tanks.get(1).move(Direction.RIGHT));
        assertEquals(new GridPoint2(1, 2), tanks.get(1).getCoordinates());

        assertFalse(tanks.get(1).move(Direction.RIGHT));
        assertEquals(new GridPoint2(1, 2), tanks.get(1).getCoordinates());
    }

    @Test
    public void testMultipleTanksMovement() {
        assertFalse(tanks.get(0).move(Direction.UP));
        assertFalse(tanks.get(1).move(Direction.DOWN));

        assertTrue(tanks.get(0).move(Direction.RIGHT));
        assertTrue(tanks.get(1).move(Direction.UP));

        assertEquals(new GridPoint2(2, 1), tanks.get(0).getCoordinates());
        assertEquals(new GridPoint2(1, 3), tanks.get(1).getCoordinates());

        assertFalse(tanks.get(0).move(Direction.UP));
        assertEquals(new GridPoint2(2, 1), tanks.get(0).getCoordinates());

        assertTrue(tanks.get(1).move(Direction.RIGHT));
        assertFalse(tanks.get(1).move(Direction.DOWN));
        assertEquals(new GridPoint2(2, 3), tanks.get(1).getCoordinates());
    }
}

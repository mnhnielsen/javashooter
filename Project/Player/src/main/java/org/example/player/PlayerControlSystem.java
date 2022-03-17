package org.example.player;

import com.badlogic.gdx.graphics.Texture;
import org.example.common.data.Entity;
import org.example.common.data.GameData;
import org.example.common.data.GameKeys;
import org.example.common.data.World;
import org.example.common.data.entityparts.LifePart;
import org.example.common.data.entityparts.MovingPart;
import org.example.common.data.entityparts.PositionPart;
import org.example.common.services.IEntityProcessingService;
import org.example.common.services.IPlayerService;
import org.example.commonbullet.BulletSPI;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;


@ServiceProviders(value = {@ServiceProvider(service = IEntityProcessingService.class),
        @ServiceProvider(service = IPlayerService.class)
})

public class PlayerControlSystem implements IEntityProcessingService, IPlayerService
{


    public static Texture man;

    @Override
    public void process(GameData gameData, World world)
    {
        for (Entity player : world.getEntities(Player.class))
        {
            PositionPart positionPart = player.getPart(PositionPart.class);
            MovingPart movingPart = player.getPart(MovingPart.class);
            LifePart lifePart = player.getPart(LifePart.class);

            movingPart.setLeft(gameData.getKeys().isDown(GameKeys.LEFT));
            movingPart.setRight(gameData.getKeys().isDown(GameKeys.RIGHT));
            movingPart.setUp(gameData.getKeys().isDown(GameKeys.UP));
            movingPart.setDeceleration(100f);

            if (gameData.getKeys().isDown(GameKeys.SPACE))
            {
                Entity bullet = Lookup.getDefault().lookup(BulletSPI.class).createBullet(player, gameData);
                world.addEntity(bullet);
            }

            movingPart.process(gameData, player);
            positionPart.process(gameData, player);
            lifePart.process(gameData, player);
            //updateShape(player);
        }
    }


    @Override
    public Texture createTexture()
    {
        man = new Texture("/home/mathias/Documents/Projects/Semester4/javashooter/Project/man.png");
        return man;
    }


    private void updateShape(Entity entity)
    {

        float[] shapex = new float[4];
        float[] shapey = new float[4];
        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = positionPart.getRadians();

        shapex[0] = (float) (x + Math.cos(radians) * entity.getRadius());
        shapey[0] = (float) (y + Math.sin(radians) * entity.getRadius());

        shapex[1] = (float) (x + Math.cos(radians - 4 * 3.1415f / 5) * entity.getRadius());
        shapey[1] = (float) (y + Math.sin(radians - 4 * 3.1145f / 5) * entity.getRadius());

        shapex[2] = (float) (x + Math.cos(radians + 3.1415f) * entity.getRadius() * 0.5);
        shapey[2] = (float) (y + Math.sin(radians + 3.1415f) * entity.getRadius() * 0.5);

        shapex[3] = (float) (x + Math.cos(radians + 4 * 3.1415f / 5) * entity.getRadius());
        shapey[3] = (float) (y + Math.sin(radians + 4 * 3.1415f / 5) * entity.getRadius());

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);


    }


}

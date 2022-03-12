package org.example.player;


import org.example.common.data.Entity;
import org.example.common.data.GameData;
import org.example.common.data.World;
import org.example.common.data.entityparts.LifePart;
import org.example.common.data.entityparts.MovingPart;
import org.example.common.data.entityparts.PositionPart;
import org.example.common.services.IGamePluginService;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
        @ServiceProvider(service = IGamePluginService.class)
})
public class PlayerPlugin implements IGamePluginService
{
    private Entity player;
    public PlayerPlugin(){

    }
    @Override
    public void start(GameData gameData, World world)
    {
        player = createPlayerShip(gameData);
        world.addEntity(player);
    }

    @Override
    public void stop(GameData gameData, World world)
    {
        world.removeEntity(player);
    }

    private Entity createPlayerShip(GameData gameData) {

        float deacceleration = 10;
        float acceleration = 200;
        float maxSpeed = 300;
        float rotationSpeed = 5;
        float x = gameData.getDisplayWidth() / 2;
        float y = gameData.getDisplayHeight() / 2;
        float radians = 3.1415f / 2;

        Entity playerShip = new Player();
        playerShip.setRadius(8);
        playerShip.add(new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed));
        playerShip.add(new PositionPart(x, y, radians));
        playerShip.add(new LifePart(1));

        return playerShip;
    }
}

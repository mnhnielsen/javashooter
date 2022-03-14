package org.example.bullet;

import org.example.common.data.Entity;
import org.example.common.data.GameData;
import org.example.common.data.World;
import org.example.common.services.IGamePluginService;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
        @ServiceProvider(service = IGamePluginService.class),})
public class BulletPlugin implements IGamePluginService
{
    private Entity bullet;

    @Override
    public void start(GameData gameData, World world) {

    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity e : world.getEntities()) {
            if (e.getClass() == Bullet.class) {
                world.removeEntity(e);
            }
        }
    }

}

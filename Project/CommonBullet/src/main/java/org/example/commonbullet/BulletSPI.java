package org.example.commonbullet;

import org.example.common.data.Entity;
import org.example.common.data.GameData;

public interface BulletSPI
{
    Entity createBullet(Entity e, GameData gameData);

}

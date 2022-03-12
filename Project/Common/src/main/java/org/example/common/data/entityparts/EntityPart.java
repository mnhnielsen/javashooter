package org.example.common.data.entityparts;

import org.example.common.data.Entity;
import org.example.common.data.GameData;

public interface EntityPart
{
    void process(GameData gameData, Entity entity);
}

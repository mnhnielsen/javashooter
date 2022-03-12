package org.example.common.services;

import org.example.common.data.GameData;
import org.example.common.data.World;

public interface IGamePluginService
{
    void start(GameData gameData, World world);

    void stop(GameData gameData, World world);
}

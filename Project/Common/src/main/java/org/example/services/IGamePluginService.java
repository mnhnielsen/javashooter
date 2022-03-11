package org.example.services;

import org.example.data.GameData;
import org.example.data.World;

public interface IGamePluginService
{
    void start(GameData gameData, World world);

    void stop(GameData gameData, World world);
}

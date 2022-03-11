package org.example.services;

import org.example.data.GameData;
import org.example.data.World;

public interface IPostEntityProcessingService
{
    void process(GameData gameData, World world);
}

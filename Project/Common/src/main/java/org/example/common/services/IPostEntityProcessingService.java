package org.example.common.services;

import org.example.common.data.GameData;
import org.example.common.data.World;

public interface IPostEntityProcessingService
{
    void process(GameData gameData, World world);

}

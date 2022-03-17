package org.example.common.services;

import org.example.common.data.GameData;
import org.example.common.data.World;

import java.util.ArrayList;
import java.util.List;

public interface IEntityProcessingService
{
    void process(GameData gameData, World world);
    List<String> getAssetResources();

}

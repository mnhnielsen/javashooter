/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.data.entityparts;

import org.example.data.Entity;
import org.example.data.GameData;

/**
 *
 * @author Alexander
 */
public interface EntityPart {

    void process(GameData gameData, Entity entity);
}

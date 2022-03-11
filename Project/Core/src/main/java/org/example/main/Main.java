package org.example.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.openide.modules.ModuleInstall;

public class Main extends ModuleInstall
{
    private static org.example.main.GameInstaller game;

    @Override
    public void restored()
    {
        game = new GameInstaller();
        LwjglApplicationConfiguration applicationConfiguration = new LwjglApplicationConfiguration();
        applicationConfiguration.title = "New Asteroids";
        applicationConfiguration.width = 1200;
        applicationConfiguration.height = 800;
        applicationConfiguration.useGL30 = false;
        applicationConfiguration.resizable = false;

        new LwjglApplication(game, applicationConfiguration);
        System.out.println("This is the core module running.");
    }
}

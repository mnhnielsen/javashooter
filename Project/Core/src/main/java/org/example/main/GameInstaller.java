package org.example.main;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.example.data.Entity;
import org.example.data.GameData;
import org.example.data.World;
import org.example.services.IEntityProcessingService;
import org.example.services.IGamePluginService;
import org.example.services.IPostEntityProcessingService;
import org.example.main.managers.GameInputProcessor;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameInstaller implements ApplicationListener
{
    private static OrthographicCamera cam; //This is the player cam from top down
    private ShapeRenderer sr;   //For rendering shapes on the screen. Vectors...
    private final Lookup lookup = Lookup.getDefault();  //Need to figure this one out
    private final GameData gameData = new GameData();
    private World world = new World();
    private List<IGamePluginService> gamePlugins = new CopyOnWriteArrayList<>();
    private Lookup.Result<IGamePluginService> result;


    @Override
    public void create()
    {


        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        gameData.setDisplayWidth(Gdx.graphics.getWidth());

        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();
        sr = new ShapeRenderer();

        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));

        result = lookup.lookupResult(IGamePluginService.class);
        result.addLookupListener(lookupListener);
        result.allItems();

        for (IGamePluginService plugin : result.allInstances())
        {
            plugin.start(gameData, world);
            gamePlugins.add(plugin);
        }
    }

    @Override
    public void resize(int i, int i1)
    {

    }


    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.

        gameData.setDelta(Gdx.graphics.getDeltaTime());
        update();
        draw();
        gameData.getKeys().update();

    }

    private void update()
    {
        // Update
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices())
        {
            entityProcessorService.process(gameData, world);
        }
/*
        // Post Update
        for (IPostEntityProcessingService postEntityProcessorService : getPostEntityProcessingServices())
        {
            postEntityProcessorService.process(gameData, world);
        }

 */
    }

    private void draw()
    {
        for (Entity entity : world.getEntities())
        {
            sr.setColor(1, 1, 1, 1);

            sr.begin(ShapeRenderer.ShapeType.Line);

            float[] shapex = entity.getShapeX();
            float[] shapey = entity.getShapeY();

            for (int i = 0, j = shapex.length - 1;
                 i < shapex.length;
                 j = i++)
            {

                sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
            }

            sr.end();
        }
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices()
    {
        return lookup.lookupAll(IEntityProcessingService.class);
    }

    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices()
    {
        return lookup.lookupAll(IPostEntityProcessingService.class);
    }

    private final LookupListener lookupListener = new LookupListener()
    {
        @Override
        public void resultChanged(LookupEvent le)
        {

            Collection<? extends IGamePluginService> updated = result.allInstances();

            for (IGamePluginService us : updated)
            {
                // Newly installed modules
                if (!gamePlugins.contains(us))
                {
                    us.start(gameData, world);
                    gamePlugins.add(us);
                }
            }

            // Stop and remove module
            for (IGamePluginService gs : gamePlugins)
            {
                if (!updated.contains(gs))
                {
                    gs.stop(gameData, world);
                    gamePlugins.remove(gs);
                }
            }
        }

    };
}

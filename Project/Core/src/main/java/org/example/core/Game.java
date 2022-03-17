package org.example.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.example.common.data.Entity;
import org.example.common.data.GameData;
import org.example.common.data.World;
import org.example.common.services.IEntityProcessingService;
import org.example.common.services.IGamePluginService;
import org.example.common.services.IPlayerService;
import org.example.common.services.IPostEntityProcessingService;
import org.example.managers.AssetsJarFileResolver;
import org.example.managers.GameInputProcessor;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game implements ApplicationListener
{
    private static OrthographicCamera cam;
    private ShapeRenderer sr;
    private final Lookup lookup = Lookup.getDefault();
    private final GameData gameData = new GameData();
    private World world = new World();
    private List<IGamePluginService> gamePlugins = new CopyOnWriteArrayList<>();
    private Lookup.Result<IGamePluginService> result;

    public static Texture backgroundTexture, man;
    public static Sprite backgroundSprite, manSprite;
    private SpriteBatch spriteBatch;
    AssetsJarFileResolver jfhr = new AssetsJarFileResolver();
    AssetManager am = new AssetManager(jfhr);

    private void loadTextures()
    {
        //Background config
        backgroundTexture = new Texture("/home/mathias/Documents/Projects/Semester4/javashooter/Project/CurrencyObtainRight.png");
        backgroundSprite = new Sprite(backgroundTexture,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        spriteBatch = new SpriteBatch();

        //Player
        for (IEntityProcessingService service : getEntityProcessingServices())
            for (String assetUrl : service.getAssetResources()){
                am.load(assetUrl, Texture.class);
                am.finishLoading();

                man = am.get(assetUrl, Texture.class);
            }
        manSprite = new Sprite(man);
    }



    public void renderSprites()
    {

        backgroundSprite.draw(spriteBatch);
        manSprite.draw(spriteBatch);
    }

    @Override
    public void create()
    {

        loadTextures();
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

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

    private void update()
    {
        // Update
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices())
        {
            entityProcessorService.process(gameData, world);
        }

        // Post Update
        for (IPostEntityProcessingService postEntityProcessorService : getPostEntityProcessingServices())
        {
            postEntityProcessorService.process(gameData, world);
        }

    }



    @Override
    public void render()
    {
        // clear screen to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);

        spriteBatch.begin();
        renderSprites();
        update();
        spriteBatch.end();

        gameData.setDelta(Gdx.graphics.getDeltaTime());
        gameData.getKeys().update();

        draw();

    }


    private void draw()
    {
        for (Entity entity : world.getEntities())
        {
            sr.setColor(255, 0, 0, 1);

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
    public void resize(int width, int height)
    {
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

    private Collection<? extends IPlayerService> getPlayerServices()
    {
        return lookup.lookupAll(IPlayerService.class);
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

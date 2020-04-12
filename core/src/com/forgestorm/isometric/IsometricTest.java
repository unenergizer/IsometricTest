package com.forgestorm.isometric;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.forgestorm.isometric.input.Keyboard;
import com.forgestorm.isometric.input.Mouse;
import com.forgestorm.isometric.scene2d.StageHandler;
import com.forgestorm.isometric.util.IsometricUtil;
import com.forgestorm.isometric.util.ScreenResolutions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;

@Getter
public class IsometricTest extends ApplicationAdapter {

    private static final Color BACKGROUND = new Color(23 / 255, 35 / 255, 35 / 255, 1);

    private int mapWidth;
    private int mapHeight;
    private int tileWidth;
    private int tileHeight;
    private int tileWidthHalf;
    private int tileHeightHalf;

    @Setter
    private int mapRotation = 0;

    private SpriteBatch spriteBatch;
    private Texture tileHoverTexture;
    private List<Texture> loadedTextures = new ArrayList<>();

    private List<TileObject> objectList = new ArrayList<>();
    @Setter
    private boolean sortNeeded = true;

    private OrthographicCamera camera;
    private MapRenderer mapRenderer;
    private TiledMap isoMap;

    private Keyboard keyboard;
    private Mouse mouse;

    private StageHandler stageHandler;

    @Override
    public void create() {

        // Textures
        spriteBatch = new SpriteBatch();
        tileHoverTexture = new Texture(Gdx.files.internal("images/tall_selector.png"));

        loadedTextures.add(new Texture("images/grass_custom_03.png"));
        loadedTextures.add(new Texture("images/grass_custom_01.png"));
        loadedTextures.add(new Texture("images/grass_custom_02.png"));
        loadedTextures.add(new Texture("images/grass_custom_04.png"));
        loadedTextures.add(new Texture("images/water_custom_01.png"));
        loadedTextures.add(new Texture("images/water_custom_02.png"));
//        loadedTextures.add(new Texture("images/green_wall.png"));
        loadedTextures.add(new Texture("images/green_wall2.png"));

        // Init map
        isoMap = new TmxMapLoader().load("map/big-iso-map.tmx");
        MapProperties isoMapProperties = isoMap.getProperties();
        mapWidth = isoMapProperties.get("width", Integer.class);
        mapHeight = isoMapProperties.get("height", Integer.class);
        tileWidth = isoMapProperties.get("tilewidth", Integer.class);
        tileHeight = isoMapProperties.get("tileheight", Integer.class);
        tileWidthHalf = tileWidth / 2;
        tileHeightHalf = tileHeight / 2;

        // Place random tiles
        int loadedTiles = loadedTextures.size();
        int totalTiles = mapWidth * mapHeight;

        System.out.println("LoadedTiles: " + loadedTiles + ", TotalTiles: " + totalTiles);

        Random random = new Random();
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                objectList.add(new TileObject(new Vector2(i, j), random.nextInt(loadedTiles)));
            }
        }

        // Camera Setup
        camera = new OrthographicCamera(ScreenResolutions.DESKTOP_800_600.getWidth(), ScreenResolutions.DESKTOP_800_600.getHeight());
        camera.position.x = mapWidth / 2;
        camera.update();

        // Map renderer
        mapRenderer = new IsometricTileMapRenderer(this, isoMap, spriteBatch);
        mapRenderer.setView(camera);
        mapRenderer.render();

        // User Interface
        stageHandler = new StageHandler();
        stageHandler.buildActors();

        // Register Input
        keyboard = new Keyboard(this, camera);
        mouse = new Mouse(this);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stageHandler.getStage());
        inputMultiplexer.addProcessor(keyboard);
        inputMultiplexer.addProcessor(mouse);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(BACKGROUND.r, BACKGROUND.g, BACKGROUND.b, BACKGROUND.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        if (mouse.getCellHovered() != null) {
            Vector2 tempVector;

            if (sortNeeded) {
                Collections.sort(objectList, new Comparator<TileObject>() {
                            @Override
                            public int compare(TileObject tileObject21, TileObject tileObject2) {
                                Vector2 xy1 = tileObject21.getTileCords();
                                Vector2 xy2 = tileObject2.getTileCords();

                                float x1 = xy1.x;
                                float y1 = xy1.y;
                                float x2 = xy2.x;
                                float y2 = xy2.y;
                                if (mapRotation == 0) {
                                    // Moving objects or objects placed within a tile
//                                if ((y2 - x2) > (y1 - x1)) {
//                                    return 1;
//                                }
//                                return -1;
                                    return (int) ((y2 - x2) - (y1 - x1));
                                } else if (mapRotation == 1) {
                                    return (int) ((y2 + x2) - (y1 + x1));
                                } else if (mapRotation == 2) {
                                    return (int) ((x2 - y2) - (x1 - y1));
                                } else {
                                    return (int) ((-x2 - y2) - (-x1 - y1));
                                }
                            }
                        }
                );
                setSortNeeded(false);
            }

            // Sort and draw objects
            for (TileObject tileObject : objectList) {
                tempVector = IsometricUtil.screenToMap(tileObject.getTileCords().x, tileObject.getTileCords().y, mapWidth, mapHeight, tileWidthHalf, tileHeightHalf, mapRotation, true);
                spriteBatch.draw(loadedTextures.get(tileObject.getUsedTextureIndex()), tempVector.x, tempVector.y);
            }

            // Draw mouse wireframe
            tempVector = IsometricUtil.screenToMap(mouse.getCellHovered().x, mouse.getCellHovered().y, mapWidth, mapHeight, tileWidthHalf, tileHeightHalf, mapRotation, true);
            spriteBatch.draw(tileHoverTexture, tempVector.x, tempVector.y);
        }
        spriteBatch.end();

        stageHandler.update(Gdx.graphics.getDeltaTime(), this);
    }

    @Override
    public void resize(int width, int height) {
        stageHandler.resize(width, height);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
        stageHandler.getStage().getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        isoMap.dispose();
        stageHandler.dispose();
        tileHoverTexture.dispose();
        for (Texture texture : loadedTextures) texture.dispose();
        spriteBatch.dispose();
    }
}

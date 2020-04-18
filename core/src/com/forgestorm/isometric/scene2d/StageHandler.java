package com.forgestorm.isometric.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.forgestorm.isometric.IsometricTest;
import com.forgestorm.isometric.scene2d.actors.CenterScreen;
import com.forgestorm.isometric.scene2d.actors.Debug;
import com.forgestorm.isometric.scene2d.actors.EdgeDescriptors;
import com.forgestorm.isometric.scene2d.actors.WorldBuilder;
import com.kotcrab.vis.ui.VisUI;

import lombok.Getter;

@Getter
public class StageHandler {

    static {
        VisUI.load(Gdx.files.internal("ui/tixel/x1/tixel.json"));
    }

    public static final int PADDING = 8;
    public static final int EDGE_PADDING = 60;

    private Stage stage = new Stage();

    private Debug debug = new Debug();
    private WorldBuilder worldBuilder = new WorldBuilder();
    private CenterScreen centerScreen = new CenterScreen();
    private EdgeDescriptors edgeDescriptors = new EdgeDescriptors();

    public void buildActors() {
        stage.addActor(worldBuilder.build(this));
        stage.addActor(debug.build(this));
        stage.addActor(centerScreen.build(this));
        stage.addActor(edgeDescriptors.build(this));
    }

    public void update(float delta, IsometricTest isometricTest) {
        for (Actor actor : stage.getActors()) {
            if (!actor.isVisible()) continue;
            if (actor instanceof Updatable) ((Updatable) actor).update(isometricTest);
        }
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    public void dispose() {
        VisUI.dispose();
        stage.dispose();
    }
}

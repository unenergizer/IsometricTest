package com.forgestorm.isometric.scene2d.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.forgestorm.isometric.scene2d.Buildable;
import com.forgestorm.isometric.scene2d.StageHandler;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

public class CenterScreen extends VisTable implements Buildable {

    @Override
    public Actor build(StageHandler stageHandler) {
        add(new VisLabel("x"));
        setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        return this;
    }
}

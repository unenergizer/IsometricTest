package com.forgestorm.isometric.scene2d.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.forgestorm.isometric.scene2d.Buildable;
import com.forgestorm.isometric.scene2d.StageHandler;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

public class WorldBuilder extends VisWindow implements Buildable {

    public WorldBuilder() {
        super("World Builder");
    }

    @Override
    public Actor build(StageHandler stageHandler) {
        VisTextButton button = new VisTextButton("Hello world!");
        add(button);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Window clicked");
            }
        });

        setPosition(StageHandler.PADDING, StageHandler.PADDING);
        addCloseButton();
        pack();
        setMovable(true);
        setVisible(false);
        return this;
    }
}

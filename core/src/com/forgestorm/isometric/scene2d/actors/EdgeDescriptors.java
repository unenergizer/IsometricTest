package com.forgestorm.isometric.scene2d.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.forgestorm.isometric.IsometricTest;
import com.forgestorm.isometric.scene2d.StageHandler;
import com.forgestorm.isometric.scene2d.Updatable;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.forgestorm.isometric.scene2d.Buildable;

public class EdgeDescriptors extends Actor implements Buildable, Updatable {


    private VisLabel startXlabel = new VisLabel("StartX: 00");
    private VisLabel startYlabel = new VisLabel("StartY: 00");
    private VisLabel endXlabel = new VisLabel("EndX: 00");
    private VisLabel endYlabel = new VisLabel("EndY: 00");

    @Override
    public Actor build(StageHandler stageHandler) {

        // Start
        VisTable startXtable = new VisTable();
        startXtable.add(startXlabel);
        startXtable.setPosition(StageHandler.EDGE_PADDING, Gdx.graphics.getHeight() - StageHandler.PADDING);
        stageHandler.getStage().addActor(startXtable);

        VisTable startYtable = new VisTable();
        startYtable.add(startYlabel);
        startYtable.setPosition(StageHandler.EDGE_PADDING, StageHandler.PADDING);
        stageHandler.getStage().addActor(startYtable);

        // End
        VisTable endXtable = new VisTable();
        endXtable.add(endXlabel);
        endXtable.setPosition(Gdx.graphics.getWidth() - StageHandler.EDGE_PADDING, StageHandler.PADDING);
        stageHandler.getStage().addActor(endXtable);

        VisTable endYtable = new VisTable();
        endYtable.add(endYlabel);
        endYtable.setPosition(Gdx.graphics.getWidth() - StageHandler.EDGE_PADDING, Gdx.graphics.getHeight() - StageHandler.PADDING);
        stageHandler.getStage().addActor(endYtable);

        return this;
    }

    @Override
    public void update(IsometricTest isometricTest) {
        int x = (int) isometricTest.getMouse().getCellHovered().x;
        int y = (int) isometricTest.getMouse().getCellHovered().y;
        startXlabel.setText("StartX: " + isometricTest.getMapRenderer().getCol1() + " (" + x + ")");
        startYlabel.setText("StartY: " + isometricTest.getMapRenderer().getRow1() + " (" + y + ")");
        endXlabel.setText("EndX: " + isometricTest.getMapRenderer().getCol2() + " (" + x + ")");
        endYlabel.setText("EndY: " + isometricTest.getMapRenderer().getRow2() + " (" + y + ")");
    }
}

package com.forgestorm.isometric.scene2d.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.forgestorm.isometric.IsometricTest;
import com.forgestorm.isometric.scene2d.Buildable;
import com.forgestorm.isometric.scene2d.StageHandler;
import com.forgestorm.isometric.scene2d.Updatable;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

public class EdgeDescriptors extends Actor implements Buildable, Updatable {


    private VisLabel topLeft = new VisLabel("StartX: 00");
    private VisLabel bottomLeft = new VisLabel("StartY: 00");
    private VisLabel topRight = new VisLabel("EndX: 00");
    private VisLabel bottomRight = new VisLabel("EndY: 00");

    @Override
    public Actor build(StageHandler stageHandler) {

        // Start
        VisTable topLeftTable = new VisTable();
        topLeftTable.add(topLeft);
        topLeftTable.setPosition(StageHandler.EDGE_PADDING, Gdx.graphics.getHeight() - StageHandler.PADDING);
        stageHandler.getStage().addActor(topLeftTable);

        VisTable bottomLeftTable = new VisTable();
        bottomLeftTable.add(bottomLeft);
        bottomLeftTable.setPosition(StageHandler.EDGE_PADDING, StageHandler.PADDING);
        stageHandler.getStage().addActor(bottomLeftTable);

        // End
        VisTable topRightTable = new VisTable();
        topRightTable.add(topRight);
        topRightTable.setPosition(Gdx.graphics.getWidth() - StageHandler.EDGE_PADDING, Gdx.graphics.getHeight() - StageHandler.PADDING);
        stageHandler.getStage().addActor(topRightTable);

        VisTable bottomRightTable = new VisTable();
        bottomRightTable.add(bottomRight);
        bottomRightTable.setPosition(Gdx.graphics.getWidth() - StageHandler.EDGE_PADDING, StageHandler.PADDING);
        stageHandler.getStage().addActor(bottomRightTable);

        return this;
    }

    @Override
    public void update(IsometricTest isometricTest) {
        int rotation = isometricTest.getMapRotation();
        int mouseTileX = (int) isometricTest.getMouse().getCellHovered().x;
        int mouseTileY = (int) isometricTest.getMouse().getCellHovered().y;

        String startX = "StartX: " + isometricTest.getMapRenderer().getStartX() + " (" + mouseTileX + ")";
        String startY = "StartY: " + isometricTest.getMapRenderer().getStartY() + " (" + mouseTileY + ")";
        String endX = "EndX: " + isometricTest.getMapRenderer().getEndX() + " (" + mouseTileX + ")";
        String endY = "EndY: " + isometricTest.getMapRenderer().getEndY() + " (" + mouseTileY + ")";

        if (rotation == 0) {
            topLeft.setText(startX);
            bottomLeft.setText(startY);
            topRight.setText(endY);
            bottomRight.setText(endX);
        } else if (rotation == 1) {
            topLeft.setText(endY);
            bottomLeft.setText(startX);
            topRight.setText(endX);
            bottomRight.setText(startY);
        } else if (rotation == 2) {
            topLeft.setText(endX);
            bottomLeft.setText(endY);
            topRight.setText(startY);
            bottomRight.setText(startX);
        } else if (rotation == 3) {
            topLeft.setText(startY);
            bottomLeft.setText(endX);
            topRight.setText(startX);
            bottomRight.setText(endY);
        }
    }
}

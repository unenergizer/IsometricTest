package com.forgestorm.isometric.scene2d.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.forgestorm.isometric.IsometricTest;
import com.forgestorm.isometric.input.Mouse;
import com.forgestorm.isometric.scene2d.Buildable;
import com.forgestorm.isometric.scene2d.StageHandler;
import com.forgestorm.isometric.scene2d.Updatable;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

public class Debug extends VisTable implements Buildable, Updatable {

    private VisLabel cameraLabel = new VisLabel("Camera X: 100, Y: 100, Zoom: 1");
    private VisLabel mouseLabel = new VisLabel("Mouse X: 100, Y: 100");
    private VisLabel tileHoverLabel = new VisLabel("MouseTile X: null, Y: null");
    private VisLabel mapCellLabel = new VisLabel("MapCell: null");
    private VisLabel fpsLabel = new VisLabel("FPS: 1000");
    private VisLabel rotationLabel = new VisLabel("Rotation: 0");

    @Override
    public Actor build(StageHandler stageHandler) {
        add(cameraLabel).align(Alignment.LEFT.getAlignment()).row();
        add(mouseLabel).align(Alignment.LEFT.getAlignment()).row();
        add(tileHoverLabel).align(Alignment.LEFT.getAlignment()).row();
        add(mapCellLabel).align(Alignment.LEFT.getAlignment()).row();
        add(fpsLabel).align(Alignment.LEFT.getAlignment()).row();
        add(rotationLabel).align(Alignment.LEFT.getAlignment()).row();
        pack();
        setPosition(StageHandler.PADDING, Gdx.graphics.getHeight() - StageHandler.PADDING - getHeight());
        return this;
    }


    @Override
    public void update(IsometricTest isometricTest) {
        OrthographicCamera camera = isometricTest.getCamera();
        cameraLabel.setText("Camera X: " + camera.position.x + ", Y: " + camera.position.y + ", Zoom: " + camera.zoom);

        Mouse mouse = isometricTest.getMouse();
        mouseLabel.setText("Mouse X: " + mouse.getMouseX() + ", Y: " + mouse.getMouseY());
        if (mouse.getCellHovered() != null) tileHoverLabel.setText("MouseTile X: " + mouse.getCellHovered().x + ", Y: " + mouse.getCellHovered().y);
        if (mouse.getCellClicked() != null) mapCellLabel.setText("MapTileClicked: " + mouse.getCellClicked().toString());

        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());

        int rotation = (isometricTest.getMapRotation() + 1) * 90;
        rotationLabel.setText("Rotation: " + rotation + "°");
        pack();
    }
}

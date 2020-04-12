package com.forgestorm.isometric.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisImage;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "unused"})
public class ImageBuilder {

    private String regionName;
    private Texture texture;
    private float width = 0;
    private float height = 0;
    private float x = 0;
    private float y = 0;
    private Color color = Color.WHITE;

    public ImageBuilder() {
    }

    public ImageBuilder(Texture texture) {
        setTexture(texture);
    }

    public ImageBuilder(Texture texture, float size) {
        setTexture(texture);
        setSize(size);
    }

    public ImageBuilder(Texture texture, String regionName) {
        setTexture(texture);
        setRegionName(regionName);
    }

    public ImageBuilder(Texture texture, String regionName, float size) {
        setTexture(texture);
        setRegionName(regionName);
        setSize(size);
    }

    public ImageBuilder(Texture texture, String regionName, float width, float height) {
        setTexture(texture);
        setRegionName(regionName);
        setWidth(width);
        setHeight(height);
    }

    public ImageBuilder setTexture(Texture texture) {
        this.texture = texture;
        return this;
    }

    public ImageBuilder setRegionName(String regionName) {
        this.regionName = regionName;
        return this;
    }

    public ImageBuilder setWidth(float width) {
        this.width = width;
        return this;
    }

    public ImageBuilder setHeight(float height) {
        this.height = height;
        return this;
    }

    public ImageBuilder setSize(float size) {
        setWidth(size);
        setHeight(size);
        return this;
    }

    public ImageBuilder setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public ImageBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    public TextureRegionDrawable buildTextureRegionDrawable() {
        if (texture == null) throw new RuntimeException("Texture must be defined.");
        if (regionName == null || regionName.isEmpty())
            throw new RuntimeException("Region Name must be defined.");

        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(texture);
        textureRegionDrawable.tint(color);
        if (width > 0) textureRegionDrawable.setMinWidth(width);
        if (height > 0) textureRegionDrawable.setMinHeight(height);

        return textureRegionDrawable;
    }

    public VisImage buildVisImage() {
        VisImage visImage = new VisImage(buildTextureRegionDrawable());
        visImage.setColor(color);
        visImage.setPosition(x, y);
        return visImage;
    }
}

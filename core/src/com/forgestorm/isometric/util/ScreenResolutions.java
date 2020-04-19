package com.forgestorm.isometric.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * https://en.wikipedia.org/wiki/Display_resolution
 */
@Getter
@AllArgsConstructor
public enum ScreenResolutions {
    DESKTOP_800_600(800, 600),
    DESKTOP_1024_600(1024, 600),
    DESKTOP_1024_768(1024, 768),
    DESKTOP_1152_864(1152, 864),
    DESKTOP_1280_720(1280, 720),
    DESKTOP_1280_768(1280, 768),
    DESKTOP_1280_800(1280, 800),
    DESKTOP_1280_1024(1280, 1024),
    DESKTOP_1360_768(1360, 768),
    DESKTOP_1366_768(1366, 768),
    DESKTOP_1440_900(1440, 900),
    DESKTOP_1536_864(1536, 864),
    DESKTOP_1600_900(1600, 900),
    DESKTOP_1680_1050(1680, 1050),
    DESKTOP_1920_1080(1920, 1080),
    DESKTOP_1920_1200(1920, 1200), // Too many extra cells from here on
    DESKTOP_2560_1080(2560, 1080),
    DESKTOP_2560_1440(2560, 1440),
    DESKTOP_3440_1440(3440, 1440),
    DESKTOP_3840_2160(3840, 2160);

    private int width, height;

    @Override
    public String toString() {
        return width + " / " + height;
    }
}

package com.chenq.player;

import java.awt.*;

public final class Const {
    public static final String VERSION = "1.0.0";
    public static final String AUTHOR = "ChenQ";
    public static final String EMAIL = "chenq491@163.com";
    public static final String GITHUB = "https://github.com/ChenQing-Chen/MusicPlayer";

    public static final String LYRIC_FILE_SUFFIX = ".lrc";
    public static final String MUSIC_FILE_SUFFIX = ".wav";
    public static final String GIF_FILE_SUFFIX = ".gif";

    public static final Color buttonColor = Color.getHSBColor(347, 280, 99);
    public static final Color backgroundColor = Color.getHSBColor(199, 60, 99);
    public static final String jarPath = System.getProperty("user.dir");
    public static final String GIF_PATH = jarPath + "\\assets\\gif";
    public static final String MUSIC_PATH = jarPath + "\\assets\\music";
    public static final String LYRIC_PATH = jarPath + "\\assets\\lyric";

    public static final int NEXT = 1;
    public static final int PRE = 2;
}
enum PlayBackMode {
    SEQUENCE,
    RANDOM,
    LOOP
}
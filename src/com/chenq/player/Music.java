package com.chenq.player;

import java.io.File;

public class Music {
    private File musicFile;  //歌曲文件
    private String musicName;  //歌曲名称
    private int musicTime;  //歌曲时长
    private String musicLyricPath;  //歌词文件路径

    public Music(File musicFile, String musicName, int musicTime, String musicLyricPath) {
        this.musicFile = musicFile;
        this.musicName = musicName;
        this.musicTime = musicTime;
        this.musicLyricPath = musicLyricPath;
    }

    public File getMusicFile() {
        return musicFile;
    }

    public void setMusicFile(File musicFile) {
        this.musicFile = musicFile;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public int getMusicTime() {
        return musicTime;
    }

    public void setMusicTime(int musicTime) {
        this.musicTime = musicTime;
    }

    public String getMusicLyricPath() {
        return musicLyricPath;
    }

    public void setMusicLyricPath(String musicLyricPath) {
        this.musicLyricPath = musicLyricPath;
    }
}

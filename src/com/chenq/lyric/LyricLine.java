package com.chenq.lyric;

public class LyricLine {
    long timestamp;//时间戳，单位毫秒
    String content;//歌词内容

    LyricLine(long timestamp,String content){
        this.timestamp = timestamp;
        this.content = content;
    }
}

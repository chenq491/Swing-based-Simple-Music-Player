package com.chenq.lyric;

import javax.swing.*;
import java.util.List;

//歌词线程
public class LyricThread extends Thread{
    private volatile boolean isLyricPaused = false;//判断歌词是否暂停
    private volatile boolean isLyricStopped = false;//判断歌词是否结束
    int nowLyricTime = 0;//当前歌词的时间
    long audioDurationMs;//歌曲总时间
    List<LyricLine> lyrics;
    JTextArea lyricArea;//显示歌词的文本区域

    public LyricThread(JTextArea lyricArea,List<LyricLine> lyrics,long audioDurationMs){
        this.lyricArea = lyricArea;
        this.lyrics = lyrics;
        this.audioDurationMs = audioDurationMs;
    }

    @Override
    public void run() {
        nowLyricTime = 0;
        while(nowLyricTime <= audioDurationMs) {
            if(!isLyricStopped) {
                if(!isLyricPaused) {
                    //显示歌词
                    try {
                        Thread.sleep(1000);//音频播放一秒
                        for(LyricLine lyric:lyrics) {
                            if(lyric.timestamp/1000 <= nowLyricTime && (lyrics.indexOf(lyric) == lyrics.size() -1 || lyrics.get(lyrics.indexOf(lyric)+1).timestamp/1000 > nowLyricTime)) {
                                lyricArea.setText(lyric.content);
                                break;
                            }
                        }
                        nowLyricTime += 1;//每秒检查一次
                    }catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    synchronized (this) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            else {
                break;
            }
        }
    }
    //暂停歌词
    public synchronized void pauseLyric() {
        isLyricPaused = true;
    }
    //继续歌词
    public synchronized void continueLyric() {
        if(isLyricPaused) {
            isLyricPaused = false;
            notifyAll();
        }
    }
    //结束歌词
    public void stopLyric() {
        isLyricStopped = true;
    }
}
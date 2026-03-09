package com.chenq.player;

import com.chenq.lyric.LyricRead;
import com.chenq.lyric.LyricLine;
import com.chenq.lyric.LyricThread;
import com.chenq.panel.PlayerPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Timer;

public class Player {
    Clip clip;

    private PlayBackMode playBackMode = PlayBackMode.SEQUENCE;  // 播放模式
    private boolean isSwitch = false;  //是否是手动切换歌曲

    int currentSongIndex = 0;  //当前歌曲在播放列表的索引
    int currentSongTime;  //当前歌曲总时长
    long nowTime;  //当前音乐播放时间

    LyricThread lyricThread;  // 歌词进程
    Timer timer;  //进度条计时器

    PlayerPanel playerPanel = new PlayerPanel();
    private List<Music> musicList = new ArrayList<>();

    public Player() throws UnsupportedAudioFileException, IOException {
        playerPanel.setActionListener(this);
        ReadMusicFiles();//读取音乐文件
        playerPanel.getFrame().setVisible(true);
    }

    public void playMusic() {
        // 如果当前正在播放歌曲，则停止
        if (clip != null && clip.isRunning()) {
            isSwitch = true;
            clip.stop();
            clip.close();
            timer.stop();
            lyricThread.stopLyric();
            playerPanel.getLyricArea().setText("歌词");
        }
        Music currentMusic = musicList.get(currentSongIndex);

        //改变主窗口当前播放音乐文本
        playerPanel.getNowMusicArea().setText("当前播放：" + currentMusic.getMusicName());

        isSwitch = false;
        //播放指定路径的音频文件
        try {
            File musicFile = currentMusic.getMusicFile();
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicFile);

            //获取Clip对象来播放音频
            clip = AudioSystem.getClip();
            clip.open(audioInput);

            //歌曲播放完自动播放下一首
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    if (!isSwitch) {
                        switchMusic(Const.NEXT);
                    }
                }
            });
            clip.start();

            //显示歌词
            List<LyricLine> lyrics = LyricRead.parseLrcFile(currentMusic.getMusicLyricPath());
            currentSongTime = currentMusic.getMusicTime();
            ShowMusicLyric(lyrics, currentSongTime);

            //更新进度条
            UpdateProgressBar();

        } catch (Exception e) {
            System.out.println("播放错误：" + e.getMessage());
        }
    }

    //暂停音乐
    public void pauseMusic() {
        if (clip != null && clip.isRunning()) {
            isSwitch = true;
            clip.stop();
            timer.stop();
            lyricThread.pauseLyric();
        }
    }

    //继续播放音乐
    public void continueMusic() {
        if (clip != null && !clip.isRunning()) {
            isSwitch = false;
            clip.start();
            timer.start();
            lyricThread.continueLyric();
        }
    }

    public void switchMusic(int choice) {
        if (musicList.isEmpty()) return;
        // 根据不同模式获取下一首歌曲的下标
        if (playBackMode == PlayBackMode.SEQUENCE) {
            if (choice == Const.NEXT) {
                currentSongIndex = (currentSongIndex + 1) % musicList.size();
            } else {
                currentSongIndex--;
                if (currentSongIndex == -1) {
                    currentSongIndex = musicList.size() - 1;
                }
            }
        } else if (playBackMode == PlayBackMode.RANDOM) {
            Random rand = new Random();
            currentSongIndex = rand.nextInt(musicList.size());
        }

        playMusic();
    }

    //更新进度条
    void UpdateProgressBar() {
        nowTime = 0;//当前音乐播放时间

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nowTime++;
                //获取进度条进度
                float progress = (float) nowTime / currentSongTime;
                progress = Math.min(progress, 1);
                playerPanel.getProgressBar().setValue((int) (progress * 1000));
                //修改时间标签
                playerPanel.getCurrentTimeLabel().setText(formatTime((int) nowTime) + "/" + formatTime(currentSongTime));

                //当歌曲放完时进度条停止
                if (clip.isRunning()) {
                    if (clip.getMicrosecondPosition() >= clip.getMicrosecondLength()) {
                        ((Timer) e.getSource()).stop();
                    }
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    //读取music文件夹下的.wav文件
    void ReadMusicFiles() throws UnsupportedAudioFileException {

        //获取音乐文件目录路径
        Path dirPath = Paths.get(Const.MUSIC_PATH);

        //获取音乐文件路径流
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*" + Const.MUSIC_FILE_SUFFIX)) {
            for (Path filePath : stream) {
                File musicfile = new File(filePath.toString());//音乐文件
                String musicFileName = musicfile.getName();//音乐文件名
                String musicName = musicFileName.substring(0, musicFileName.lastIndexOf('.'));//音乐名

                //获取每首歌的音频时长加入音频时长列表
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicfile);
                AudioFormat format = audioInput.getFormat();
                long audioFileLengthInBytes = musicfile.length();
                int musicTime = (int) ((float) audioFileLengthInBytes / format.getFrameSize() * 1000 / format.getFrameRate()) / 1000;

                String musicLyricPath = Const.LYRIC_PATH + File.separator + musicName + Const.LYRIC_FILE_SUFFIX;
                musicList.add(new Music(musicfile, musicName, musicTime, musicLyricPath));

            }
        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println("Error reading directory:" + ex.getMessage());
        }
    }

    //显示歌曲的歌词
    public void ShowMusicLyric(List<LyricLine> lyrics, long audioDurationMs) {
        lyricThread = new LyricThread(playerPanel.getLyricArea(), lyrics, audioDurationMs);
        lyricThread.start();
    }

    //查看当前播放模式
    public void checkPlayBackMode() {
        String selectedPattern = (String) playerPanel.getPattern().getSelectedItem();
        assert selectedPattern != null;
        if (selectedPattern.equals("随机播放")) {
            playBackMode = PlayBackMode.RANDOM;
        } else if (selectedPattern.equals("单曲循环")) {
            playBackMode = PlayBackMode.LOOP;
        } else {
            playBackMode = PlayBackMode.SEQUENCE;
        }
    }

    //格式化时间 00:00
    public String formatTime(int seconds) {
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public void setCurrentSongIndex(int currentSongIndex) {
        this.currentSongIndex = currentSongIndex;
    }
}

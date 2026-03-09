package com.chenq.panel;

import com.chenq.player.Const;
import com.chenq.player.GIF;
import com.chenq.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class PlayerPanel {

    private JFrame frame;
    //布局组件
    private JPanel mainPanel, controlPanel, centerPanel;  //主面板，控制面板和中心面板
    private final JLabel gifLabel = new JLabel();  //gif图标签
    private final JButton playButton, pauseButton, prevButton, nextButton, musicListButton;  //几个按钮
    private JTextArea nowMusicArea, lyricArea;  //歌词文本域和当前播放歌曲文本域
    private JComboBox<String> pattern;//播放模式
    private JProgressBar progressBar;  //进度条
    private JLabel currentTimeLabel;  //进度条上面的时间标签
    private final GIF gif = new GIF();

    MusicListDialog musicListDialog;

    public PlayerPanel() throws IOException {
        frame = new JFrame("Music Player");
        frame.setBounds(200, 200, 960, 540);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //播放模式
        pattern = new JComboBox<String>();
        pattern.addItem("顺序播放");
        pattern.addItem("随机播放");
        pattern.addItem("单曲循环");

        //创建文本域显示当前播放歌曲
        nowMusicArea = new JTextArea("当前播放：无");
        nowMusicArea.setBorder(BorderFactory.createEmptyBorder());//去除文本域边框
        nowMusicArea.setEditable(false);//设置不可编辑
        nowMusicArea.setFont(new Font("SansSerif", Font.BOLD, 16));//设置字体

        //创建文本域显示歌词
        lyricArea = new JTextArea("歌词");
        lyricArea.setBorder(BorderFactory.createEmptyBorder());//去除文本域边框
        lyricArea.setEditable(false);
        lyricArea.setFont(new Font("SansSerif", Font.BOLD, 20));//设置字体

        progressBar = new JProgressBar(0, 1000);
        progressBar.setValue(0);
        currentTimeLabel = new JLabel("00:00/00:00");

        //创建gif动图标签
        gifLabel.setIcon(gif.loadGif());
        //双击gif图片切换图片
        gifLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    gifLabel.setIcon(gif.changeGif());
                }
            }
        });

        playButton = new JButton("播放");  //播放按钮
        pauseButton = new JButton("暂停");  //暂停按钮
        prevButton = new JButton("上一首");  //上一首按钮
        nextButton = new JButton("下一首");  //下一首按钮
        musicListButton = new JButton("播放列表");  //设置播放列表

        //设置按钮背景颜色
        pattern.setBackground(Const.buttonColor);
        playButton.setBackground(Const.buttonColor);
        pauseButton.setBackground(Const.buttonColor);
        prevButton.setBackground(Const.buttonColor);
        nextButton.setBackground(Const.buttonColor);
        musicListButton.setBackground(Const.buttonColor);

        //控制面板使用GridLayout布局管理器
        controlPanel = new JPanel(new GridLayout(4, 1));
        JPanel p0 = new JPanel(new BorderLayout());
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel(new GridLayout(1, 6, 5, 5));
        JPanel p3 = new JPanel();
        p0.add(progressBar, BorderLayout.CENTER);
        p0.add(currentTimeLabel, BorderLayout.NORTH);
        p2.add(pattern);
        p2.add(prevButton);
        p2.add(playButton);
        p2.add(pauseButton);
        p2.add(nextButton);
        p2.add(musicListButton);
        controlPanel.add(p0);
        controlPanel.add(p1);
        controlPanel.add(p2);
        controlPanel.add(p3);
        //设置面板背景颜色
        p0.setBackground(Const.backgroundColor);
        p1.setBackground(Const.backgroundColor);
        p2.setBackground(Const.backgroundColor);
        p3.setBackground(Const.backgroundColor);

        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Const.backgroundColor);
        JPanel p4 = new JPanel();
        JPanel p5 = new JPanel();
        p4.setBackground(Const.backgroundColor);
        p5.setBackground(Const.backgroundColor);
        p4.add(lyricArea);
        p5.add(gifLabel);
        centerPanel.add(p4, BorderLayout.SOUTH);
        centerPanel.add(p5, BorderLayout.CENTER);

        //主面板使用BorderLayout布局管理器
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(nowMusicArea, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
    }

    /**
     * Add action listener to button
     */
    public void setActionListener(Player player) {
        playButton.addActionListener(e -> player.continueMusic());
        pauseButton.addActionListener(e -> player.pauseMusic());
        prevButton.addActionListener(e -> {
            player.checkPlayBackMode();
            player.switchMusic(Const.PRE);
        });
        nextButton.addActionListener(e -> {
            player.checkPlayBackMode();
            player.switchMusic(Const.NEXT);
        });
        musicListButton.addActionListener(e -> {
            musicListDialog = new MusicListDialog(frame);
            musicListDialog.setList(player);
            musicListDialog.setVisible(true);
        });
    }


    public JTextArea getNowMusicArea() {
        return nowMusicArea;
    }

    public void setNowMusicArea(JTextArea nowMusicArea) {
        this.nowMusicArea = nowMusicArea;
    }

    public JTextArea getLyricArea() {
        return lyricArea;
    }

    public void setLyricArea(JTextArea lyricArea) {
        this.lyricArea = lyricArea;
    }

    public JComboBox<String> getPattern() {
        return pattern;
    }

    public void setPattern(JComboBox<String> pattern) {
        this.pattern = pattern;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public JLabel getCurrentTimeLabel() {
        return currentTimeLabel;
    }

    public void setCurrentTimeLabel(JLabel currentTimeLabel) {
        this.currentTimeLabel = currentTimeLabel;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
}



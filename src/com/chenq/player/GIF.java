package com.chenq.player;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GIF {
    ArrayList<String> gifList = new ArrayList<>();  // GIF图片文件地址
    int currentGIFIndex = 0;  // 当前GIF图片索引

    public GIF() throws IOException {
        readGifFiles();
    }

    //获取gif图片
    private void readGifFiles() throws IOException {
        //获取GIF文件目录路径
        Path dirPath = Paths.get(Const.GIF_PATH);

        //获取GIF文件路径流
        DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*"+Const.GIF_FILE_SUFFIX);
        for (Path filePath : stream) {
            File giffile = new File(filePath.toString());  //GIF文件
            gifList.add(giffile.getAbsolutePath());
        }
    }

    //加载gif图片
    public ImageIcon loadGif() {
        String gifPath = gifList.get(currentGIFIndex);
        return new ImageIcon(gifPath);
    }

    // 改变GIF图片
    public ImageIcon changeGif() {
        currentGIFIndex = (currentGIFIndex + 1) % gifList.size();
        return loadGif();
    }
}

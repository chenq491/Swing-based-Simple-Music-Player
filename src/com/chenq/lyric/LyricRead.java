package com.chenq.lyric;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class LyricRead {

    //读取LRC文件里的歌词
    public static List<LyricLine> parseLrcFile(String filePath) throws IOException {
        List<LyricLine> lyricLines = new ArrayList<>();
        Charset c = StandardCharsets.UTF_8;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath), c)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("[")) {
                    long timestamp = getTimestamp(line);
                    String content = line.substring(line.indexOf("]") + 1).trim();
                    lyricLines.add(new LyricLine(timestamp, content));
                }
            }
        }
        return lyricLines;
    }

    private static long getTimestamp(String line) {
        int start = line.indexOf("[") + 1;
        int end = line.indexOf("]");
        String timeStr = line.substring(start, end);
        String[] times = timeStr.split("[:.]");
        int minutes = Integer.parseInt(times[0]);
        int seconds = Integer.parseInt(times[1]);
        int milliseconds = Integer.parseInt(times[2].substring(0, 2));
        return minutes * 60_000L + seconds * 1000L + milliseconds * 10L;
    }
}


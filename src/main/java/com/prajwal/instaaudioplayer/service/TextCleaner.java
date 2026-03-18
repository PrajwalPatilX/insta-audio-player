package com.prajwal.instaaudioplayer.service;

import java.util.ArrayList;
import java.util.List;

public class TextCleaner {

    public List<String> cleanSongs(String text) {

        String[] lines = text.split("\n");

        List<String> songs = new ArrayList<>();

        for (String line : lines) {

            line = line.trim();

            if (line.isEmpty())
                continue;

            String lower = line.toLowerCase();

            if (lower.contains("original audio"))
                continue;

            if (line.matches(".*\\d+:\\d+.*")) // remove timestamps
                continue;

            if (line.length() < 4)
                continue;

            // remove strange symbols
            line = line.replaceAll("^[^a-zA-Z0-9]+", "");

            // remove small OCR prefixes like "ie", "J", "eA"
            if (line.matches("^[a-zA-Z]{1,2}\\s+.*")) {
                line = line.substring(line.indexOf(" ") + 1);
            }

            // 🆕 remove numbering like "3 " or "2 "
            line = line.replaceAll("^\\d+\\s+", "");

            // remove artist lines
            if (line.contains(",") && line.split(",").length > 2)
                continue;

            songs.add(line);
        }

        return songs;
    }
}
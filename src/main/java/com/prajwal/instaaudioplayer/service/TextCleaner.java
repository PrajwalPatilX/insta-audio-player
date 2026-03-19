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

            // 🔥 REMOVE BAD WORDS (IMPROVED)
            if (
                    lower.contains("original") ||
                            lower.contains("audio") ||
                            lower.contains("status") ||
                            lower.contains("instagram") ||
                            lower.contains("reels")
            )
                continue;

            // ❌ remove timestamps (like 0:30)
            if (line.matches(".*\\d+:\\d+.*"))
                continue;

            // ❌ too short (garbage OCR)
            if (line.length() < 4)
                continue;

            // 🔹 remove strange starting symbols
            line = line.replaceAll("^[^a-zA-Z0-9]+", "");

            // 🔹 remove small OCR prefixes (ie, J, eA)
            if (line.matches("^[a-zA-Z]{1,2}\\s+.*")) {
                line = line.substring(line.indexOf(" ") + 1);
            }

            // 🔹 remove numbering like "1 ", "2 "
            line = line.replaceAll("^\\d+\\s+", "");

            // 🔥 REMOVE SINGLE WORD GARBAGE (VERY IMPORTANT)
            if (line.split(" ").length < 2)
                continue;

            // 🔥 REMOVE TOO MANY COMMAS (artist junk)
            if (line.contains(",") && line.split(",").length > 2)
                continue;

            // 🔹 final clean (remove special chars inside)
            line = line.replaceAll("[^a-zA-Z0-9 ]", "");

            line = line.trim();

            if (line.isEmpty())
                continue;

            songs.add(line);
        }

        return songs;
    }
}
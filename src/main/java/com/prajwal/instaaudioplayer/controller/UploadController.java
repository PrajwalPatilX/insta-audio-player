package com.prajwal.instaaudioplayer.controller;

import com.prajwal.instaaudioplayer.service.OCRService;
import com.prajwal.instaaudioplayer.service.TextCleaner;
import com.prajwal.instaaudioplayer.service.YouTubeService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import org.json.JSONObject;

@CrossOrigin(origins = "*")
@RestController
public class UploadController {

    private final YouTubeService youTubeService;

    public UploadController(YouTubeService youTubeService) {
        this.youTubeService = youTubeService;
    }

    @PostMapping("/upload")
    public Object uploadImage(@RequestParam("image") MultipartFile file) {

        try {

            String uploadDir = System.getProperty("user.dir") + "/uploads/";

            File folder = new File(uploadDir);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            String filename = file.getOriginalFilename();

            File dest = new File(uploadDir + filename);

            file.transferTo(dest);

            // OCR
            OCRService ocrService = new OCRService();
            String extractedText = ocrService.readText(dest.getAbsolutePath());

            // Clean songs
            TextCleaner cleaner = new TextCleaner();
            List<String> songs = cleaner.cleanSongs(extractedText);

            List<Map<String, String>> result = new ArrayList<>();

            for (String song : songs) {

                String youtubeResponse = youTubeService.searchVideo(song);

                JSONObject json = new JSONObject(youtubeResponse);

                String videoId =

                        json.getJSONArray("items")

                        .getJSONObject(0)

                        .getJSONObject("id")

                        .getString("videoId");

                Map<String, String> songData = new HashMap<>();

                songData.put("title", song);

                songData.put("videoId", videoId);

                result.add(songData);

            }

            return result;

        } catch (Exception e) {

            return "Error: " + e.getMessage();
        }
    }
}
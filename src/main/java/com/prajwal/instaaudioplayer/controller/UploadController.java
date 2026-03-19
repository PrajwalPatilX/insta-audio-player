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
            List<String> allSongs = cleaner.cleanSongs(extractedText);
            List<String> songs = allSongs.subList(0, Math.min(2, allSongs.size()));
            System.out.println("CLEANED SONGS: " + songs);

            List<Map<String, String>> result = new ArrayList<>();

            for (String song : songs) {

                System.out.println("Processing song: " + song);

                String videoId = null;

                try {

                    String youtubeResponse = youTubeService.searchVideo(song);

                    System.out.println("API RESPONSE: " + youtubeResponse);

                    if (youtubeResponse != null && youtubeResponse.startsWith("{")) {

                        JSONObject json = new JSONObject(youtubeResponse);

                        if (json.has("items") && json.getJSONArray("items").length() > 0) {

                            JSONObject item = json.getJSONArray("items").getJSONObject(0);

                            if (item.has("id") && item.getJSONObject("id").has("videoId")) {
                                videoId = item.getJSONObject("id").getString("videoId");
                            }
                        }
                    }

                } catch (Exception e) {
                    System.out.println("YouTube failed for: " + song);
                }

                // 🔥 ALWAYS ADD SONG (even if API fails)
                if (videoId == null) {
                    videoId = "dQw4w9WgXcQ"; // fallback video
                }

                Map<String, String> songData = new HashMap<>();
                songData.put("title", song);

                songData.put("videoId", videoId);

                result.add(songData);

            }

            return result;

        } catch (Exception e) {

            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
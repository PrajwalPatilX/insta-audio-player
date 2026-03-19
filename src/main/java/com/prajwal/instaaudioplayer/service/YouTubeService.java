package com.prajwal.instaaudioplayer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class YouTubeService {

    @Value("${youtube.api.key}")
    private String apiKey;

    public String searchVideo(String song) {

        try {

            // 🔥 Better search query
            String query = song + " official audio";
            query = query.replace(" ", "%20");

            // 🔥 Increase results
            String urlString =
                    "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=3&q="
                            + query + "&key=" + apiKey;

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();

            BufferedReader reader;

            // 🔥 Handle error responses properly
            if (responseCode >= 200 && responseCode < 300) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line;
            StringBuilder responseBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }

            reader.close();

            String response = responseBuilder.toString();

            System.out.println("YOUTUBE API RESPONSE: " + response);

            // ✅ Always return valid JSON
            if (response == null || response.trim().isEmpty() || !response.trim().startsWith("{")) {
                return "{}";
            }

            return response;

        } catch (Exception e) {

            e.printStackTrace();

            // ✅ Always safe return
            return "{}";
        }
    }
}
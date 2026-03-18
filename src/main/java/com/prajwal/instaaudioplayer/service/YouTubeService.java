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

            String query = song.replace(" ", "%20");

            String urlString =
                    "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q="
                            + query + "&key=" + apiKey;

            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            return response.toString();

        } catch (Exception e) {

            return "Error: " + e.getMessage();
        }
    }
}
package com.eventbooking.eventbookingapp.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SupabaseService {
    private static final String SUPABASE_URL = System.getenv("SUPABASE_URL");
    private static final String API_KEY = System.getenv("SUPABASE_KEY");
    private static String format;

    private static HttpURLConnection setupConnection(String endpoint, String method) throws IOException {
        URL url = new URL(SUPABASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("apikey", API_KEY);
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoInput(true);
        if (method.equals("POST") || method.equals("PATCH"))
            conn.setDoOutput(true);
        return conn;
    }

    public static String fetchEvents() {
        try {
            HttpURLConnection conn = setupConnection("events?select=*", "GET");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                return br.lines().reduce("", (acc, line) -> acc + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "[]";
        }
    }

    public static String saveEvent(String name, int capacity) {
        String json = String.format(
                "{\"name\":\"%s\", \"capacity\":%d}",
                name, capacity
        );

        try {
            HttpURLConnection conn = setupConnection("events?select=*", "POST");
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            if (code == 201) { 
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    return br.lines().reduce("", (acc, line) -> acc + line);
                }
            } else {
                return "Error: " + code;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public static String saveTicket(String eventId, String name, String email, String ticketCode) {
        String json = format;

        try {
            HttpURLConnection conn = setupConnection("tickets", "POST");
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            if (code == 201 || code == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    return br.lines().reduce("", (acc, line) -> acc + line);
                }
            } else {
                return "Error: " + code;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

}


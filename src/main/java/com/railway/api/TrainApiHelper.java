package com.railway.api;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.railway.model.Train;
import java.util.List;

public class TrainApiHelper {
    private static final String BASE_URL = "http://localhost:5000/api";
    private static final OkHttpClient client = new OkHttpClient().newBuilder().build();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Train> searchTrains(String source, String destination) throws Exception {
        String url = String.format("%s/trains/search?fromStation=%s&toStation=%s",
                                 BASE_URL, source, destination);
        
        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();

        Response response = client.newCall(request).execute();
        
        if (!response.isSuccessful()) {
            throw new Exception("Failed to search trains: " + response.code());
        }

        String jsonResponse = response.body().string();
        return mapper.readValue(jsonResponse, new TypeReference<List<Train>>() {});
    }

    public static void bookTicket(String trainNumber, int ticketCount, String coachType,
                                String username, String gender, int age) throws Exception {
        String jsonBody = String.format(
            "{\n\t\"platformTicket\": {\n\t\t\"trainNumber\": \"%s\",\n\t\t\"ticketsCount\": %d,\n\t\t\"coachType\": \"%s\"\n\t},\n\t\"username\": \"%s\",\n\t\"gender\": \"%s\",\n\t\"age\": %d\n}",
            trainNumber, ticketCount, coachType, username, gender, age
        );

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonBody);
        
        Request request = new Request.Builder()
            .url(BASE_URL + "/platform-tickets")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .build();
        
        Response response = client.newCall(request).execute();
        
        if (!response.isSuccessful()) {
            throw new Exception("Failed to book ticket: " + response.code());
        }
    }
} 
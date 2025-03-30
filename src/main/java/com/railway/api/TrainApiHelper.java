package com.railway.api;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.railway.model.PlatformTicket;
import com.railway.model.Train;
import java.util.List;

public class TrainApiHelper {
    private static final String BASE_URL = "http://localhost:5000/api";
    private static final OkHttpClient client = new OkHttpClient().newBuilder().build();
    private static final ObjectMapper mapper = new ObjectMapper()
        .findAndRegisterModules()  // Register all modules including JPA module
        .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static List<Train> searchTrains(String source, String destination) throws Exception {
        // Dynamically construct the URL with the source and destination
        String url = String.format("%s/trains/search?fromStation=%s&toStation=%s", BASE_URL, source, destination);

        // Create the GET request
        Request request = new Request.Builder()
            .url(url)
            .method("GET", null)
            .build();

        // Execute the request
        Response response = client.newCall(request).execute();

        // Check if the response is successful
        if (!response.isSuccessful()) {
            throw new Exception("Failed to search trains: " + response.code());
        }

        String jsonResponse = response.body().string();
        return mapper.readValue(jsonResponse, new TypeReference<List<Train>>() {});
    }

    public static Long bookTicket(String trainNumber, String coachType,
            String username, String gender, int age) throws Exception {
        String jsonBody = String.format(
                "{\n" +
                        "  \"serialNumber\": 0,\n" +
                        "  \"passenger\": {\n" +
                        "    \"name\": \"%s\",\n" +
                        "    \"age\": %d,\n" +
                        "    \"gender\": \"%s\",\n" +
                        "    \"seatStatus\": \"Confirmed\",\n" +
                        "    \"coachType\": \"%s\"\n" +
                        "  },\n" +
                        "  \"train\": {\n" +
                        "    \"trainNumber\": \"%s\"\n" +
                        "  },\n" +
                        "  \"issueTime\": null\n" +
                        "}",
                username, age, gender, coachType, trainNumber);

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

        // Parse response to get the ticket ID
        String jsonResponse = response.body().string();
        PlatformTicket ticket = mapper.readValue(jsonResponse, PlatformTicket.class);
        return ticket.getId();
    }
}
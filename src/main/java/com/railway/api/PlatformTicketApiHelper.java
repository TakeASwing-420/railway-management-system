package com.railway.api;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.railway.model.PlatformTicket;

public class PlatformTicketApiHelper {
    private static final String BASE_URL = "http://localhost:5000/api";
    private static final OkHttpClient client = new OkHttpClient().newBuilder().build();
    private static final ObjectMapper objectMapper = new ObjectMapper().
    findAndRegisterModules()  // Register all modules including JPA module
    .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static PlatformTicket fetchTicket(Long passengerId) throws Exception {
        String url = String.format("%s/platform-tickets/%d", BASE_URL, passengerId);
        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new Exception("Failed to fetch ticket: " + response.code());
        }
        String responseBody = response.body().string();
        return objectMapper.readValue(responseBody, PlatformTicket.class);
    }
}
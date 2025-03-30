package com.railway.api;

import com.railway.model.PlatformTicket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Arrays;

public class AdminApiHelper {
    private static final String BASE_URL = "http://localhost:5000/api";
    private static final OkHttpClient client = new OkHttpClient().newBuilder().build();
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private static final MediaType mediaType = MediaType.parse("text/plain");
    private static final RequestBody emptyBody = RequestBody.create(mediaType, "");

    public static List<PlatformTicket> getTicketsByTrainNumber(String trainNumber) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/platform-tickets/train/" + trainNumber)
                .method("GET", emptyBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            String responseBody = response.body().string();
            return Arrays.asList(objectMapper.readValue(responseBody, PlatformTicket[].class));
        }
    }

    public static List<PlatformTicket> getTicketsInTimeRange(LocalDateTime startTime, LocalDateTime endTime) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String startTimeStr = startTime.format(formatter);
        String endTimeStr = endTime.format(formatter);

        Request request = new Request.Builder()
                .url(BASE_URL + "/platform-tickets/timerange?startTime=" + startTimeStr + "&endTime=" + endTimeStr)
                .method("GET", emptyBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            String responseBody = response.body().string();
            return Arrays.asList(objectMapper.readValue(responseBody, PlatformTicket[].class));
        }
    }
} 
package com.railway.api;

import com.railway.model.PlatformTicket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import okhttp3.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminApiHelper {
	private static final String BASE_URL = "http://localhost:5000/api";
	private static final OkHttpClient client = new OkHttpClient().newBuilder().build();
	private static final ObjectMapper mapper = new ObjectMapper()
			.findAndRegisterModules() // Register all modules including JPA module
			.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	public static List<PlatformTicket> getTicketsByTrainNumber(String trainNumber) throws IOException {
		Request request = new Request.Builder()
				.url(BASE_URL + "/platform-tickets/train/" + trainNumber)
				.get()
				.build();

		// Execute the request
		Response response = client.newCall(request).execute();
		String jsonResponse = response.body().string();
		return mapper.readValue(jsonResponse, new TypeReference<List<PlatformTicket>>() {
		});

	}

	public static List<PlatformTicket> getTicketsInLastHour() throws IOException {
		// Get current time in IST
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime oneHourBefore = now.minusHours(1);

		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		String startTimeStr = oneHourBefore.format(formatter);
		String endTimeStr = now.format(formatter);

		Request request = new Request.Builder()
				.url(BASE_URL + "/platform-tickets/timerange?startTime=" + startTimeStr + "&endTime=" + endTimeStr)
				.get() // Using get() instead of method("GET", emptyBody)
				.build();

		// Execute the request
		Response response = client.newCall(request).execute();
		String jsonResponse = response.body().string();
		return mapper.readValue(jsonResponse, new TypeReference<List<PlatformTicket>>() {
		});
	}
}
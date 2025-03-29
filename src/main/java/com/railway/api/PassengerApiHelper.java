package com.railway.api;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.railway.model.Passenger;

public class PassengerApiHelper {
	private static final String BASE_URL = "http://localhost:5000/api";
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static Passenger getPassengerDetails(Long id) throws Exception {
		OkHttpClient client = new OkHttpClient().newBuilder()
			.build();
		Request request = new Request.Builder()
			.url(BASE_URL + "/passengers/" + id)
			.get()
			.build();
		Response response = client.newCall(request).execute();
		
		if (!response.isSuccessful()) {
			throw new Exception("Failed to get passenger details: " + response.code());
		}
		
		String responseBody = response.body().string();
		return objectMapper.readValue(responseBody, Passenger.class);
	}
}

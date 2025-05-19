package com.journalapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Weather {
	private Request request;
	private Location location;
	private Current current;

	@Data
	public class Request {
		private String type;
		private String query;
		private String language;
		private String unit;
	}

	@Data
	public class Location {
		private String name;
		private String country;
		private String region;
		private String lat;
		private String lon;
	}

	@Data
	public class Current {
		@JsonProperty("observation_time")
		private String observationTime;
		private int temperature;
	}

}

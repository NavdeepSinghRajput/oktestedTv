package com.tv.oktested.app.page.entity;

import com.google.gson.annotations.SerializedName;

public class GenresItem {

	@SerializedName("genre_slug")
	public String genreSlug;

	@SerializedName("genre_title")
	public String genreTitle;
}
package com.tv.oktested.app.page.model;

import com.google.gson.annotations.SerializedName;

public class HomeStructuteListModel {

        @SerializedName("section_type")
        public String section_type;

        @SerializedName("value")
        public HomeSectionValue value;

        public class HomeSectionValue {

            @SerializedName("slug")
            public String slug;

            @SerializedName("section_title")
            public String section_title;

            @SerializedName("image_path")
            public String image_path;

            @SerializedName("image_link")
            public String image_link;
        }
    }
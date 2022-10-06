package com.tv.oktested.app.page.model;

import com.tv.oktested.model.BaseResponse;

import java.util.ArrayList;

public class GetUserDataresponse extends BaseResponse {

    public String gender;
    public ArrayList<String> subscribe;
    public String lastLogin;
    public String name;
    public String id;
    public String display_name;
    public ArrayList<String> follow;
    public ArrayList<String> favourite;
    public ArrayList<String> favourite_shows;
    public String email;
    public String picture;
//    public Likes likes;
    public String timestamp;
    public String points;
    public String quiz_played;
    public String mobile;
    public String dob;
    public Social social;

    public class Social {

        public Google Google;
        public Facebook Facebook;

        public class Google {
            public String id;
        }

        public class Facebook {
            public String id;
        }
    }
}
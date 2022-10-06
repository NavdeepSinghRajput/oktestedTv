package com.tv.oktested.model;

import com.google.gson.annotations.Expose;

public class GenerateCodeModel {

    @Expose
    Datas data;
    @Expose
    String status;
    @Expose
    String message;

    public Datas getData() {
        return data;
    }

    public void setData(Datas data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Datas {
        @Expose
        String unique_id;
        @Expose
        String code;

        @Expose
        String verification_url;
        @Expose
        String expires_in;
        @Expose
        String interval;
        @Expose
        String login_show_url;

        public String getVerification_url() {
            return verification_url;
        }

        public void setVerification_url(String verification_url) {
            this.verification_url = verification_url;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }

        public String getInterval() {
            return interval;
        }

        public void setInterval(String interval) {
            this.interval = interval;
        }

        public String getLogin_show_url() {
            return login_show_url;
        }

        public void setLogin_show_url(String login_show_url) {
            this.login_show_url = login_show_url;
        }

        public String getUnique_id() {
            return unique_id;
        }

        public void setUnique_id(String unique_id) {
            this.unique_id = unique_id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

}

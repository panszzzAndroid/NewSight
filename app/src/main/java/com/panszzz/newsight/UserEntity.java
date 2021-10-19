package com.panszzz.newsight;

import com.google.gson.JsonArray;

import org.json.JSONArray;

public class UserEntity {
    private String userUid;
    private JsonArray labelList;

    public UserEntity(String userUid, JsonArray labelList) {
        this.userUid = userUid;
        this.labelList = labelList;
    }
}

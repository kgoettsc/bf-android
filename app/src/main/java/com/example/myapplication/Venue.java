package com.example.myapplication;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Venue {

    private String name;
    private List<String> activities;

    public Venue(JsonObject values) {
        this.name = values.get("name")
                    .toString()
                    .replace("\"", "");
        String[] activityList = values.get("activities")
                                .toString()
                                .replace("\"", "")
                                .replace("[", "")
                                .replace("]", "")
                                .split(",");
        this.activities = new ArrayList<>(Arrays.asList(activityList));
    }

    public String getName() {
        return this.name;
    }

    public List<String> getActivities() {
        return this.activities;
    }
}

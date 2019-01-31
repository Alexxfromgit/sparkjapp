package com.alexxrw.sparkjapp;

import com.google.gson.Gson;
import spark.ResponseTransformer;

import java.util.HashMap;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

/**
 * default port :4567
 *
 * Query in chrome console:
 * await (await fetch('/Ben?device=notebook&user[age]=16&user[gender]=male', {method: 'POST', body: JSON.stringify({mood: 'joy})})).json()
 *
 */
public class Starter {

    private static Gson gson = new Gson();

    public static void main(String[] args) {
        staticFiles.location("/public");

        get("/", (req, res) -> "Hello World");
        post("/:name", (req, res) -> {
            String name = req.params("name");
            MoodHolder mood = gson.fromJson(req.body(), MoodHolder.class);

            HashMap<String, String> map = new HashMap<>();
            map.put("name", name);
            map.put("device", req.queryParams("device"));

            map.put("user-age", req.queryMap().get("user").get("age").value());
            map.put("user-gender", req.queryMap().get("user").get("gender").value());
            map.put("mood", mood.getMood());

            return map;
        }, new JsonTransformer());
    }
}

class MoodHolder {
    private String mood;

    public MoodHolder() {
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}

class JsonTransformer implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}

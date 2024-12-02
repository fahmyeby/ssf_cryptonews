package com.example.cryptonews.Service;

import java.io.StringReader;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.cryptonews.Model.Articles;
import com.example.cryptonews.Repo.HashRepo;
import com.example.cryptonews.Util.Util;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

@Service
public class NewsService {
    @Autowired
    HashRepo repo;

    RestTemplate rt = new RestTemplate();

    // formatter for date
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    // method to fetch API data, convert from JSON to object
    public List<Articles> getArticles() {
        String data = rt.getForObject(Util.apiURL, String.class);
        JsonReader jReader = Json.createReader(new StringReader(data));
        JsonObject jObject = jReader.readObject();
        JsonArray jArray = jObject.getJsonArray("Data");
        // loop twice as category is another object of arrays within the array
        List<Articles> article = new ArrayList<>();
        for (int i = 0; i < jArray.size(); i++) {
            JsonObject record = jArray.getJsonObject(i);

            Articles a = new Articles();
            a.setId(record.getInt("ID"));
            a.setTitle(record.getString("TITLE"));
            a.setUrl(record.getString("URL"));
            a.setImageurl(record.getString("IMAGE_URL"));
            a.setBody(record.getString("BODY"));

            // convert published_on from epoch to date
            if (record.containsKey("PUBLISHED_ON")) {
                long epochTime = record.getJsonNumber("PUBLISHED_ON")
                        .longValue();
                a.setPublished_on(formatter.format(Instant.ofEpochSecond(epochTime)));
            }

            // handle CATEGORY_DATA array within the Date array
            JsonArray catArray = record.getJsonArray("CATEGORY_DATA");
            List<String> categories = new ArrayList<>();
            for (int j = 0; j < catArray.size(); j++) {
                JsonObject category = catArray.getJsonObject(j);
                String categoryValue = category.getString("CATEGORY");

                if (!categories.contains(categoryValue)) {
                    categories.add(categoryValue);
                }
            }
            a.setCategories(categories);

            article.add(a);
        }
        return article;
    }

    public void saveArticles(List<String> articleIds) {
        List<Articles> allArticles = getArticles(); // get from API (method above)

        // create map to search articles by id
        Map<Integer, Articles> articleMap = allArticles.stream()
                .collect(Collectors.toMap(Articles::getId, article -> article));

        // save only selected articles to redis
        for (String idString : articleIds) {
            int id = Integer.parseInt(idString);
            Articles article = articleMap.get(id);

            if (article != null) {
                // convert object to json string
                JsonObjectBuilder builder = Json.createObjectBuilder()
                        .add("id", article.getId())
                        .add("title", article.getTitle())
                        .add("url", article.getUrl())
                        .add("imageurl", article.getImageurl())
                        .add("body", article.getBody())
                        .add("published_on", article.getPublished_on());

                // handle category
                JsonArrayBuilder catArrayBuilder = Json.createArrayBuilder();
                for (String category : article.getCategories()) {
                    catArrayBuilder.add(category);
                }
                builder.add("categories", catArrayBuilder);
                String articleJson = builder.build().toString();

                // save to redis
                repo.setMap("articles", idString, articleJson);
            }
        }
    }

    public Articles getArticleById(String id) {
        // check if article exist in redis
        if (!repo.hasKey("articles", id)) {
            return null;
        }

        // get json string from redis
        String articleJson = repo.getValueFromMap("articles", id);

        // parse json string
        try (JsonReader jsonReader = Json.createReader(new StringReader(articleJson))) {
            JsonObject jsonObject = jsonReader.readObject();

            Articles article = new Articles();
            article.setId(jsonObject.getInt("id"));
            article.setTitle(jsonObject.getString("title"));
            article.setUrl(jsonObject.getString("url"));
            article.setImageurl(jsonObject.getString("imageurl"));
            article.setBody(jsonObject.getString("body"));
            article.setPublished_on(jsonObject.getString("published_on"));

            // Handle categories array
            List<String> categories = new ArrayList<>();
            JsonArray categoriesArray = jsonObject.getJsonArray("categories");
            for (JsonValue value : categoriesArray) {
                categories.add(((JsonString) value).getString());
            }
            article.setCategories(categories);

            return article;
        }

    }
}

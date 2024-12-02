package com.example.cryptonews.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cryptonews.Model.Articles;
import com.example.cryptonews.Service.NewsService;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@RestController
@RequestMapping("/api/news")
public class NewsRestController {

    @Autowired NewsService newsService;

    @GetMapping("")
    public ResponseEntity<List<Articles>> getArticles(){
        List<Articles> articles = new ArrayList<>();
        articles = newsService.getArticles();
        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArticle(@PathVariable String id){
        Articles article = newsService.getArticleById(id);

        if(article == null) {
            JsonObject error = Json.createObjectBuilder()
            .add("error", "Cannot find news article: " + id)
            .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(error.toString());
        }
        // If article found, create success response
        JsonObjectBuilder builder = Json.createObjectBuilder()
            .add("id", String.valueOf(article.getId()))
            .add("title", article.getTitle())
            .add("body", article.getBody())
            .add("published_on", article.getPublished_on())
            .add("url", article.getUrl())
            .add("imageurl", article.getImageurl());
            
        // Add categories as a JSON array
        JsonArrayBuilder categoriesBuilder = Json.createArrayBuilder();
        for (String category : article.getCategories()) {
            categoriesBuilder.add(category);
        }
        builder.add("categories", categoriesBuilder);
        
        // Return 200 OK with article data
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(builder.build().toString());
    }
}

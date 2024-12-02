package com.example.cryptonews.Model;

import java.util.List;

public class Articles {
    private Integer id;
    private String published_on;
    private String title;
    private String url;
    private String imageurl;
    private String body;
    private List<String> categories;

    public Articles() {
    }
    public Articles(Integer id, String published_on, String title, String url, String imageurl, String body,
    List<String> categories) {
        this.id = id;
        this.published_on = published_on;
        this.title = title;
        this.url = url;
        this.imageurl = imageurl;
        this.body = body;
        this.categories = categories;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getPublished_on() {
        return published_on;
    }
    public void setPublished_on(String published_on) {
        this.published_on = published_on;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getImageurl() {
        return imageurl;
    }
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public List<String> getCategories() {
        return categories;
    }
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
    @Override
    public String toString() {
        return id + ", " + published_on + ", " + title + ", " + url
                + ", " + imageurl + ", " + body + ", " + categories;
    }

    
    
}

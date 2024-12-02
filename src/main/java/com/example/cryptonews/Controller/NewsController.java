package com.example.cryptonews.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cryptonews.Model.Articles;
import com.example.cryptonews.Service.NewsService;

@Controller
@RequestMapping("/")
public class NewsController {
    @Autowired
    NewsService newsService;

    @GetMapping("")
    public String landingPage(Model model) {
        // get article data
        List<Articles> articles = newsService.getArticles();
        model.addAttribute("articles", articles);
        return "index";
    }

    @PostMapping("/articles")
    public String saveArticles(@RequestParam("save") List<String> selectedArticles) {
        // check if any articles selected
        if (selectedArticles != null) {
            newsService.saveArticles(selectedArticles);
        }
        return "redirect:/";

    }
}

package com.example.myapp;

import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UrlShortenerController {
    @Autowired
    private UrlService urlService;
    @GetMapping("/urlShort")
    public String urlShort() {
        return "urlShort";
    }

    @PostMapping("/shorten")
    public String shortenUrl(@RequestParam("url") @Pattern(regexp = "^(https?:\\/\\/)?([\\da-z.-]+)\\.([a-z.]{2,6})([\\/\\w .-]*)*\\/?$") String longUrl, Model model) {
        String shortUrl = urlService.createShortUrl(longUrl);
        // Pass both original and short URL to the view
        model.addAttribute("longUrl", longUrl);
        model.addAttribute("shortUrl", shortUrl);
        return "result";  // Returns a page that shows the shortened URL (e.g., result.html)
    }

    @GetMapping("/{shortUrl}")
    public void redirectToOriginalUrl(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        String originalUrl = urlService.getOriginalUrl(shortUrl);
        if (originalUrl != null) {
            response.sendRedirect(originalUrl);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}


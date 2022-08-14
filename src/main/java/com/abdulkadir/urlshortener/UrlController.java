package com.abdulkadir.urlshortener;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;

@Controller
public class UrlController {
    private final UrlRepository urlRepository;

    public UrlController(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @GetMapping("/")
    public String showPage(Model model){
        model.addAttribute("url",new Url());
        return "main";
    }

    @PostMapping("/")
    public String showPage(@ModelAttribute("url") Url url, Model model) {
        String hashText = "";
        try {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            StringBuilder sb = new StringBuilder(timeStamp);
            sb.append(url.getPureUrl());
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(sb.toString().getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            hashText = hashtext.substring(0, 7);
            url.setHashUrl(hashText); //urls can be conflict
            url.setClickCount(0);
            urlRepository.insert(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        hashText="localhost:8080/"+hashText;
        model.addAttribute("hashURL", hashText);
        model.addAttribute("clickCount",url.getClickCount());
        return "urlSuccess";
    }

    @GetMapping("{path}")
    public ResponseEntity<Object> method(@PathVariable("path")String path) throws URISyntaxException {
        if (path.isEmpty())return null;
        Url url = urlRepository.findByHashUrl(path).orElse(new Url("1", "https://github.com/AbdulkadirKoyuncu", "56",0));
        url.setClickCount(url.getClickCount()+1);
        urlRepository.save(url);
        URI uri = new URI(url.getPureUrl());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    // Count for one Url
    @RequestMapping("/{path}/count")
    public String countPage(@PathVariable("path")String path, Model model){
        Url url = urlRepository.findByHashUrl(path).orElse(new Url("1", "https://github.com/AbdulkadirKoyuncu", "56",0));;
        model.addAttribute("countClick",url.getClickCount());
        return "countSuccess";
    }

    //Count for all Urls
    @GetMapping("/urls")
    public String showUrls(Model model){
        model.addAttribute("urls",urlRepository.findAll());
        return "allUrls";
    }
}
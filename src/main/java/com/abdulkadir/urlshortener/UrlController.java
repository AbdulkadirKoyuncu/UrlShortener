package com.abdulkadir.urlshortener;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @PostMapping("/")  //              / ne? ..............................
    public String showPage(@ModelAttribute("url") Url url, Model model) {
        String hashText = "";
        try {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            StringBuilder sb = new StringBuilder(timeStamp);
            sb.append(url.getPureUrl());
            MessageDigest m = MessageDigest.getInstance("MD5"); // MD5 nasıl açlışıyor
            m.update(sb.toString().getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            hashText = hashtext.substring(0, 7);
            url.setHashUrl(hashText); //urls can be conflict
            urlRepository.insert(url);
        } catch (Exception e) {
            e.printStackTrace();     //                Bu hata ne?   ..............................................
        }
        hashText="localhost:8080/"+hashText;
        model.addAttribute("hashURL", hashText);
        return "success";
    }

    @GetMapping("/{path}")    // path?
    public ResponseEntity<Object> method(@PathVariable("path")String path) throws URISyntaxException {
        Url url = urlRepository.findByHashUrl(path).orElse(new Url("1", "https://github.com/AbdulkadirKoyuncu", "56"));
        //System.out.println(url.getHashUrl());
        URI uri = new URI(url.getPureUrl());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
}
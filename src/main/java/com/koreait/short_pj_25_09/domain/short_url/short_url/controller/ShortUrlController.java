package com.koreait.short_pj_25_09.domain.short_url.short_url.controller;

import com.koreait.short_pj_25_09.domain.short_url.short_url.entity.Surl;
import com.koreait.short_pj_25_09.domain.short_url.short_url.service.ShortUrlService;
import com.koreait.short_pj_25_09.global.exception.GlobalException;
import com.koreait.short_pj_25_09.global.rsData.RsData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ShortUrlController {

    private final ShortUrlService shortUrlService;


    @GetMapping("/add")
    @ResponseBody
    public RsData<Surl> add(String body, String url){
        return shortUrlService.add(body, url);
    }

    @GetMapping("/s/{body}/**")
    @ResponseBody
    public RsData<Surl> add(
            @PathVariable String body,
            HttpServletRequest req
    ) {
        String url = req.getRequestURI();

        if(req.getQueryString() != null) {
            url += "?" + req.getQueryString();
        }

        String[] urlBits = url.split("/",4);

        url = urlBits[3];

       return shortUrlService.add(body, url);
    }

    @GetMapping("/g/{id}")
    public String go(
            @PathVariable long id
    ){

        Surl surl = shortUrlService.findById(id).orElseThrow(GlobalException.E404::new);

        shortUrlService.increaseCount(surl);

        return "redirect:"+surl.getUrl();
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Surl> getAll(){

        return shortUrlService.findAll();
    }
}

package com.koreait.short_pj_25_09;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ShortUrlController {

    private List<Surl> surls = new ArrayList<>();
    private long surlLastId;

    @GetMapping("/add")
    @ResponseBody
    public Surl add(String body, String url) {
        Surl surl = Surl.builder()
                .id(++surlLastId)
                .body(body)
                .url(url)
                .build();

        surls.add(surl);

        return surl;
    }
}

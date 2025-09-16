package com.koreait.short_pj_25_09;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
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

    @GetMapping("/s/{body}/**")
    @ResponseBody
    public Surl add(
            @PathVariable String body,
            HttpServletRequest req
    ) {
        String url = req.getRequestURI();

        if(req.getQueryString() != null) {
            url += "?" + req.getQueryString();
        }

        String[] urlBits = url.split("/",4);

        System.out.println("Arrays.toString(urlBits): " + Arrays.toString(urlBits));

        url = urlBits[3];

        Surl surl = Surl.builder()
                .id(++surlLastId)
                .body(body)
                .url(url)
                .build();

        surls.add(surl);

        return surl;
    }

    @GetMapping("/g/{id}")
    public String go(
            @PathVariable long id
    ){
        Surl surl = surls.stream()
                .filter(_surl -> _surl.getId() == id)
                .findFirst()
                .orElse(null);

        if(surl == null) throw new RuntimeException("%d번 데이터 없음".formatted(id));

        surl.increaseCount();

        return "redirect:"+surl.getUrl();
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Surl> getAll(){
        return surls;
    }
}

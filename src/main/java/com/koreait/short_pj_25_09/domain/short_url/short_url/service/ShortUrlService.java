package com.koreait.short_pj_25_09.domain.short_url.short_url.service;

import com.koreait.short_pj_25_09.domain.short_url.short_url.entity.Surl;
import com.koreait.short_pj_25_09.domain.short_url.short_url.repository.ShortUrlRepository;
import com.koreait.short_pj_25_09.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShortUrlService { // 비즈니스 로직 담당

    private final ShortUrlRepository shortUrlRepository;

    public List<Surl> findAll() {
        return shortUrlRepository.findAll();
    }

    @Transactional
    public RsData<Surl> add(String body, String url){
        Surl surl = Surl.builder()
                .body(body)
                .url(url)
                .build();

        shortUrlRepository.save(surl);

        return RsData.of("%d번 URL 등록".formatted(surl.getId()), surl);
    }

    public Optional<Surl> findById(Long id) {
        return shortUrlRepository.findById(id);
    }

    @Transactional
    public void increaseCount(Surl surl) {
        surl.increaseCount();
    }

}

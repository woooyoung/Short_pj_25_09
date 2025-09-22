package com.koreait.short_pj_25_09.domain.short_url.short_url.repository;

import com.koreait.short_pj_25_09.domain.short_url.short_url.entity.Surl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository  extends JpaRepository<Surl, Long> {
}

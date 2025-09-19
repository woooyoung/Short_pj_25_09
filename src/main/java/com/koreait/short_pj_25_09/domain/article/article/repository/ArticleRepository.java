package com.koreait.short_pj_25_09.domain.article.article.repository;

import com.koreait.short_pj_25_09.domain.article.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {


    List<Article> findAllByIdInOrderByTitleDescIdAsc(List<Long> ids);


    List<Article> findByTitleContaining(String keyword);          // 양쪽 % 자동

    List<Article> findByTitleContainingIgnoreCase(String keyword); // 대소문자 무시


    Optional<Article> findByTitleAndBody(String title, String body);

}

package com.koreait.short_pj_25_09.global.initData;

import com.koreait.short_pj_25_09.domain.article.article.entity.Article;
import com.koreait.short_pj_25_09.domain.article.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// !prod == dev or test
@Profile("!prod")
@Configuration
@RequiredArgsConstructor
public class NotProd {

    @Lazy
    @Autowired
    private NotProd self;
    // this를 통한 객체 내부에서의 메서드 호출은 @Transactional을 작동시키지 않아
    // 외부객체에 의한 메서드 호출은 @Transactional가 작동해
    // @LAZY, @Autowired 조합은 this의 외부 호출 모드 버전인 self를 얻을 수 있어
    // self를 통한 메서드 호출은 @Transactional가 가능해

    private final ArticleRepository articleRepository;

    @Bean // 개발자가 new 하지 않아도 스프링부트가 직접 관리하는 객체
    public ApplicationRunner initDataNotProd() {
        return args -> {
            self.work1();
            self.work2();
        };
    }

    @Transactional
    public void work1() {
        if (articleRepository.count() > 0) return;

//        articleRepository.deleteAll();

        Article article1 = Article.builder()
                .title("제목1")
                .body("내용1").build();

        Article article2 = Article.builder()
                .title("제목2")
                .body("내용2").build();

        System.out.println(article1.getId());
        System.out.println(article2.getId());

        articleRepository.save(article1);
        articleRepository.save(article2);

        article2.setTitle("제목 2-2");

        articleRepository.delete(article1);
    }

    @Transactional
    public void work2() {

        // List : 0 ~ N
        // Optional : 0 ~ 1

        Optional<Article> opArticle = articleRepository.findById(2L);

        List<Article> articles = articleRepository.findAll();
    }
}

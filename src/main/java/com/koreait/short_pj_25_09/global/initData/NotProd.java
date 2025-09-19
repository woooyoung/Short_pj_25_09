package com.koreait.short_pj_25_09.global.initData;

import com.koreait.short_pj_25_09.domain.article.article.entity.Article;
import com.koreait.short_pj_25_09.domain.article.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

// !prod == dev or test
@Profile("!prod")
@Configuration
@RequiredArgsConstructor
public class NotProd {

    private final ArticleRepository articleRepository;

    @Bean // 개발자가 new 하지 않아도 스프링부트가 직접 관리하는 객체
    public ApplicationRunner initDataNotProd() {
        return args -> {
            System.out.println("NotProd initDataNotProd1");
            System.out.println("NotProd initDataNotProd2");
            System.out.println("NotProd initDataNotProd3");

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

        };
    }
}

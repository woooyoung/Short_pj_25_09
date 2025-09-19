package com.koreait.short_pj_25_09.global.initData;

import com.koreait.short_pj_25_09.domain.article.article.entity.Article;
import com.koreait.short_pj_25_09.domain.article.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

// !prod == dev or test
@Profile("!prod")
@Configuration
@RequiredArgsConstructor
public class NotProd {

    private final ArticleRepository articleRepository;

    @Bean // 개발자가 new 하지 않아도 스프링부트가 직접 관리하는 객체
    public ApplicationRunner initDataNotProd() {
        return new ApplicationRunner() {
            @Override
            @Transactional
            public void run(ApplicationArguments args) throws Exception {
//                if (articleRepository.count() > 0) return;

                articleRepository.deleteAll();

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
        };
    }
}

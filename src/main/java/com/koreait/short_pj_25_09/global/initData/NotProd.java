package com.koreait.short_pj_25_09.global.initData;

import com.koreait.short_pj_25_09.domain.article.article.entity.Article;
import com.koreait.short_pj_25_09.domain.article.article.service.ArticleService;
import com.koreait.short_pj_25_09.domain.member.member.entity.Member;
import com.koreait.short_pj_25_09.domain.member.member.service.MemberService;
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

    private final ArticleService articleService;
    private final MemberService memberService;

    @Bean // 개발자가 new 하지 않아도 스프링부트가 직접 관리하는 객체
    public ApplicationRunner initDataNotProd() {
        return args -> {
            self.work1();
            self.work2();
        };
    }

    @Transactional
    public void work1() {
        if (articleService.count() > 0) return;

        Member member1 = memberService.join("user1","1234","유저 1").getData();
        Member member2 = memberService.join("user2","1234","유저 2").getData();

//        try{
//            RsData<Member> joinRd = memberService.join("user2","1234","유저 2");
//        } catch (GlobalException e){
//            System.out.println("e.getRsData().getMsg(): " + e.getRsData().getMsg());
//            System.out.println(" e.getRsData().getResultCode(): " + e.getRsData().getResultCode());
//        }

        Article article1 = articleService.write(member1,"제목1","내용1").getData();
        Article article2 = articleService.write(member1,"제목2","내용2").getData();

        Article article3 = articleService.write(member2,"제목3","내용3").getData();
        Article article4 = articleService.write(member2,"제목4","내용4").getData();

        article2.setTitle("제목 2-2");

        articleService.delete(article1);
    }

    @Transactional
    public void work2() {

        // List : 0 ~ N
        // Optional : 0 ~ 1

        Optional<Article> opArticle = articleService.findById(2L);

        List<Article> articles = articleService.findAll();
    }
}

package com.koreait.short_pj_25_09.domain.short_url.short_url.controller;

import com.koreait.short_pj_25_09.domain.short_url.short_url.entity.Surl;
import com.koreait.short_pj_25_09.domain.short_url.short_url.service.ShortUrlService;
import com.koreait.short_pj_25_09.global.exception.GlobalException;
import com.koreait.short_pj_25_09.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@Tag(name = "short-url-controller", description = "단축 URL 생성/조회/리다이렉트 API")
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    /**
     * 쿼리스트링 기반 단축 URL 생성
     * 예: /add?body=abc123&url=https://example.com
     */
    @Operation(
            summary = "단축 URL 생성(간편형)",
            description = """
                    단축 코드(body)와 원본 URL(url)을 쿼리 파라미터로 받아 단축 URL을 생성합니다.
                    - 이미 존재하면 동일 리소스를 반환합니다.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = false,
                    content = @Content(mediaType = "application/json")
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "생성/조회 성공",
                    content = @Content(schema = @Schema(implementation = RsData.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "resultCode": "S-1",
                                      "msg": "success",
                                      "data": {
                                        "id": 1,
                                        "body": "abc123",
                                        "url": "https://example.com",
                                        "count": 0
                                      }
                                    }
                                    """))),
    })
    @GetMapping("/add")
    @ResponseBody
    public RsData<Surl> add(
            @Parameter(description = "단축 코드", example = "abc123")
            @RequestParam String body,
            @Parameter(description = "원본 URL", example = "https://example.com")
            @RequestParam String url
    ) {
        return shortUrlService.add(body, url);
    }

    /**
     * 와일드카드 경로 기반 단축 URL 생성
     * 예: /s/abc123/https://example.com/path?x=1 → body=abc123, url=이후 전체
     */
    @Operation(
            summary = "단축 URL 생성(와일드카드 경로형)",
            description = """
                    /s/{body}/** 형태로 들어온 전체 경로를 원본 URL로 간주하여 단축 URL을 생성합니다.
                    프록시/게이트웨이에서 간편히 붙여 사용하기 위한 형태입니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "생성/조회 성공",
                    content = @Content(schema = @Schema(implementation = RsData.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "resultCode": "S-1",
                                      "msg": "success",
                                      "data": {
                                        "id": 2,
                                        "body": "def456",
                                        "url": "https://example.com/path?x=1",
                                        "count": 0
                                      }
                                    }
                                    """)))
    })
    @GetMapping("/s/{body}/**")
    @ResponseBody
    public RsData<Surl> add(
            @Parameter(description = "단축 코드", example = "def456")
            @PathVariable String body,
            HttpServletRequest req
    ) {
        // 요청 URI + 쿼리스트링을 합쳐 실제 원본 URL을 추출
        String url = req.getRequestURI();
        if (req.getQueryString() != null) {
            url += "?" + req.getQueryString();
        }
        // "/s/{body}/" 이후 전체를 잘라냄
        // split("/", 4): ["", "s", "{body}", "{이후_전체}"]
        String[] urlBits = url.split("/", 4);
        url = urlBits.length >= 4 ? urlBits[3] : "";

        return shortUrlService.add(body, url);
    }

    /**
     * 단축 URL로 이동(리다이렉트)
     * 예: /g/1 → 저장된 원본 URL로 302 리다이렉트
     */
    @Operation(
            summary = "단축 URL 이동(리다이렉트)",
            description = "단축 URL ID로 조회하여 원본 URL로 리다이렉트합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "리다이렉트 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 단축 URL이 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    { "message": "Not Found" }
                                    """)))
    })
    @GetMapping("/g/{id}")
    public String go(
            @Parameter(description = "단축 URL ID", example = "1")
            @PathVariable long id
    ) {
        Surl surl = shortUrlService.findById(id).orElseThrow(GlobalException.E404::new);
        shortUrlService.increaseCount(surl);
        return "redirect:" + surl.getUrl();
    }

    /**
     * 전체 단축 URL 목록 조회(디버그/관리용)
     */
    @Operation(
            summary = "단축 URL 전체 조회",
            description = "저장된 모든 단축 URL 엔티티 목록을 반환합니다. (운영에서는 접근 제어 권장)"
    )
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = Surl.class)))
    @GetMapping("/all")
    @ResponseBody
    public List<Surl> getAll() {
        return shortUrlService.findAll();
    }
}

package swm_nm.morandi.domain.auth.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swm_nm.morandi.domain.auth.constants.SecurityConstants;
import swm_nm.morandi.domain.auth.response.TokenDto;
import swm_nm.morandi.domain.auth.service.LoginService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@RestController
@RequestMapping("/oauths")
@RequiredArgsConstructor
public class OAuthController {


    @Value("${app.domain}")
    private String domain;

    @Value("${app.redirectUri}")
    private String redirectUri;

    private final LoginService loginService;

    private final SecurityConstants securityConstants;

    //Authorization Code도 모두 가져오는 경우에 사용하는 callback api
    @GetMapping("/{type}/callback")
    public ResponseEntity<String> oauthLogin(@PathVariable String type, @RequestParam String code, HttpServletResponse response) {
        String accessToken = loginService.login(type, code, securityConstants.FOR_SERVICE).getAccessToken();

        Cookie jwtCookie = new Cookie("accessToken", accessToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setDomain(domain);; // 최상위 도메인 설정
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60); //쿠키 24시간
//        jwtCookie.setSecure(true); // secure flag (HTTPS)
        response.addCookie(jwtCookie);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUri)).build();
    }

    //개발자 모드에서는 redirect를 하지 않고 access Token을 가져오는 api
    @GetMapping("/{type}/callback/dev")
    public TokenDto oauthLoginforDevelop(@PathVariable String type, @RequestParam String code) {
        return loginService.login(type, code,securityConstants.FOR_DEVELOPER);
    }




    //만약 이미 access Toekn을 가지고 있는 경우에는 이 api를 사용
    @GetMapping("/{type}/login")
    public TokenDto login(@PathVariable String type, @RequestParam String accessToken) {
        return loginService.OAuthJoinOrLogin(type, accessToken);
    }


}

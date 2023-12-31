package swm_nm.morandi.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import swm_nm.morandi.domain.auth.constants.OAuthConstants;

@Controller
@RequestMapping("/oauths")
@RequiredArgsConstructor
public class OAuthURIController {

    private final OAuthConstants oAuthConstants;

    @ResponseBody
    @GetMapping("/google")
    public String googleRedirect() {
        //이것도 service로 빼기
        return oAuthConstants.GOOGLE_REDIRECT_URL;

    }

    //백엔드 개발 시 사용하는 API
    @GetMapping("/google/dev")
    public String googleRedirectforDevelop() {
        return "redirect:" + oAuthConstants.GOOGLE_REDIRECT_URL_DEV;

    }
    @ResponseBody
    @GetMapping("/github")
    public String githubRedirect() {
        return oAuthConstants.GITHUB_REDIRECT_URL;
    }
    @GetMapping("/github/dev")
    public String githubRedirectforDevelop() {
        return oAuthConstants.GITHUB_REDIRECT_URL;
    }

    @ResponseBody
    @GetMapping("/naver")
    public String naverRedirect() {
        return oAuthConstants.NAVER_REDIRECT_URL;
    }
    @GetMapping("/naver/dev")
    public String naverRedirectforDevelop() {
        return oAuthConstants.NAVER_REDIRECT_URL_DEV;
    }

}
package com.yesol.manddang_1.controller;

import com.yesol.manddang_1.service.UserService;
import com.yesol.manddang_1.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final UserService userService;

    //메인화면 display
    @RequestMapping("/main")
    public String goMainPage(Model model, HttpServletRequest request) {
        model.addAttribute("info", "none" );
        return "main";
    }

    //로그인 성공 및 세션 정보가 없는 상태에서 /main/user 호출시 redirect
    @GetMapping("/main/user")
    public String loginSuccess(Model model, Authentication authentication) {
        if(authentication == null){
            return "redirect:/login";
        }else{
            User userVo = (User) authentication.getPrincipal();
            model.addAttribute("info", userVo.getUserId() );
            return "main";
        }
    }

    //회원가입 display
    @RequestMapping("/join")
    public String goJoinPage(HttpServletRequest request) {
        return "join";
    }

    //회원가입(직접호출불가)
    @RequestMapping(value = "/user/join", method = RequestMethod.POST)
    public ResponseEntity<?> userJoin(@RequestBody User user, HttpSession sess){
        String resultString = userService.save(user, sess);
        ResponseEntity res = new ResponseEntity<>(resultString, HttpStatus.CREATED);
        return res;
    }

    //로그인 display
    @RequestMapping("/login")
    public String goLoginPage() {
        return "login";
    }

    //로그인 실패(직접호출불가)
    @GetMapping("/login_fail")
    public String loginFail(Model model) {
        return "redirect:/login?message=false";
    }

    //관리자 테스트페이지 display
    @RequestMapping("/adminManage")
    public String goAdminManage(HttpServletRequest request) {
        return "adminManage";
    }

    //찜목록 가기
    @GetMapping("/jjim/user")
    public String jjimlist(Model model, Authentication authentication) {
        User userVo = (User) authentication.getPrincipal();
        model.addAttribute("info", userVo.getUserId() );
        return "/jjim";
    }
    
}

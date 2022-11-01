package com.yesol.manddang_1.controller;

import com.yesol.manddang_1.service.UserService;
import com.yesol.manddang_1.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;

    //메인화면 display
    @RequestMapping("/main")
    public ModelAndView goMainPage(Model model, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/main");
        model.addAttribute("info", "none" );      //유저 아이디
        return mav;
    }

    //로그인 성공
    @GetMapping("/main/user")
    public ModelAndView loginSuccess(Model model, Authentication authentication) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/main");
        //Authentication 객체를 통해 유저 정보를 가져올 수 있다.
        User userVo = (User) authentication.getPrincipal();  //userDetail 객체를 가져옴
        model.addAttribute("info", userVo.getUserId() );      //유저 아이디
        return mav;
    }

    //회원가입 display
    @RequestMapping("/join")
    public ModelAndView goJoinPage(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("join");
        return mav;
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
    public ModelAndView goLoginPage(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }


    //로그인 실패(직접호출불가)
    @GetMapping("/login_fail")
    public String loginFail() {
        return "login_fail";
    }
    


    
}

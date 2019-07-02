package com.manager.interceptor;

import com.manager.model.Token;
import com.manager.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class Defender extends HandlerInterceptorAdapter {
    @Autowired
    TokenRepository tokenRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("defender default");
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            System.out.println("check cookie");
                for(Cookie cookie : cookies){
                    if(cookie.getName().equals("token")){
                        System.out.println("tim thay cookie token");
                        Token token = tokenRepository.getTokenByCode(cookie.getValue());
                        System.out.println(token);
                        if(token == null){
                            System.out.println("thang token null");
                            response.sendRedirect("/api/v1/notLoggedIn");
                            return true;
                        }
                        System.out.println("thang token khong null");
                        System.out.println("down");
                        HttpSession session = request.getSession();
                        session.setAttribute("token", cookie.getValue());
                        System.out.println("OK");
                        return true;
                    }
                    break;
                }
        }
        System.out.println("cookie bang null");
        response.sendRedirect("/api/v1/notLoggedIn");
        return true;
    }
}

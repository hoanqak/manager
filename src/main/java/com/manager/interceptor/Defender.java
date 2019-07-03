package com.manager.interceptor;

import com.manager.model.Token;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class Defender extends HandlerInterceptorAdapter {
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String code = request.getHeader("access_Token");
        System.out.println(code);
        Token token = tokenRepository.getTokenByCode(code);
        if(token != null && userRepository.getUserById(token.getId()) != null){
            return true;
        }
        response.sendRedirect("/api/v1/notLoggedIn");
        return true;
    }
}

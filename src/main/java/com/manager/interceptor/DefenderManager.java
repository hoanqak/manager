package com.manager.interceptor;

import com.manager.model.Token;
import com.manager.model.User;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class DefenderManager extends HandlerInterceptorAdapter {

    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String code = request.getHeader("access_Token");
        Token token = tokenRepository.getTokenByCode(code);
        if(token != null){
            User user = userRepository.getUserById(token.getId());
            if(user != null && user.getRole() == 2){
                return true;
            }
            else{
                response.sendRedirect("/api/v1/yourNotManager");
                return true;
            }
        }
        response.sendRedirect("api/v1/notLoggedIn");
        return true;
    }
}

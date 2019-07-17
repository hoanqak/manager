package com.manager.interceptor;

import com.manager.model.Token;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

@Component
public class Defender extends HandlerInterceptorAdapter {
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;
    @Value("${server.servlet.context-path}")
    String path;
    Logger logger = Logger.getLogger(Defender.class.getName());
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String code = request.getHeader("token");
        logger.info("Context Path: " + path);
        Token token = tokenRepository.getTokenByCode(code);
        if (token != null && userRepository.getUserById(token.getId()) != null) {
            return true;
        }
        response.sendRedirect(path + "/api/v1/notLoggedIn");
        return true;
    }
}

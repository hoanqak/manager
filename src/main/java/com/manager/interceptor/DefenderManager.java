package com.manager.interceptor;

import com.manager.model.Token;
import com.manager.model.User;
import com.manager.repository.TokenRepository;
import com.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
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
        System.out.println("Defender manager");
        Cookie cookies[] = request.getCookies();
        System.out.println("Cookie: " + cookies);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + ": " + cookie.getValue());
                if (cookie.getName().equals("token")) {
                    Token token = tokenRepository.getTokenByCode(cookie.getValue());
                    System.out.println(token.getId());
                    if (token != null) {
                        User user = userRepository.findById(token.getId()).get();
                        if (user != null) {
                            //i is role employee
                            System.out.println(user.getEmail());
                            if (user.getRole() == 3) {
                                return true;
                            } else {
                                request.getRequestDispatcher("/v1/api/yourNotManager").include(request, response);
                                return true;
                            }
                        }
                    }
                    break;
                }
            }

        }
        System.out.println("null");
        response.sendRedirect("/api/v1/notLoggedIn");
        return true;
    }
}

package com.manager.config;
import com.manager.interceptor.Defender;
import com.manager.interceptor.DefenderAdmin;
import com.manager.interceptor.DefenderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import  org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer{
    @Autowired
    Defender defender;
    @Autowired
    DefenderAdmin defenderAdmin;
    @Autowired
    DefenderManager defenderManager;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String excludePath[] ={"/**/login","/**/logout", "/**/notLoggedIn","/**/forgotPassword", "/**/notAdmin", "/swagger-ui.html/**", "/**/admin/**", "/**/manager/**", "**/yourNotManager"};
        registry.addInterceptor(defender).addPathPatterns("/**").excludePathPatterns(excludePath);
        registry.addInterceptor(defenderAdmin).addPathPatterns("/**/admin/**").addPathPatterns("/**/admin");
        registry.addInterceptor(defenderManager).addPathPatterns("/**/manager/**");
    }
}

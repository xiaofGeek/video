package com.video.common.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements  WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    // 加自定义拦截器JwtInterceptor，设置拦截规则
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/account/login") //注意登录接口要放行，因为登录不需要token，登录时生成token，登录后调用其他接口才需要token
                .excludePathPatterns("/account/register") //放行注册接口，人人都可以注册用户
                .excludePathPatterns("/upload/**") //放行文件上传接口
                .excludePathPatterns("/rotation/list") // 暂时放行
                .excludePathPatterns("/movie/*")
                .excludePathPatterns("/movie/export") // 排除新的导出接口
                .excludePathPatterns("/comment/*")
                .excludePathPatterns("/episode/*")
        ;
    }

    /**
     * cpolar配置跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://xxxx.cpolar.cn") // 允许前端 cpolar 地址
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
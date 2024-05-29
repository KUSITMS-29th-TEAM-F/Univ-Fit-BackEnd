package backend.univfit.global.config;


import backend.univfit.global.Interceptor.AccessTokenInterceptor;
import backend.univfit.global.argumentResolver.MemberInfoArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 *  사전 인증, 인가 처리 구현
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AccessTokenInterceptor accessTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessTokenInterceptor)
                .order(1)
                .addPathPatterns("/onboards/**", "/announcements/calandar/**", "/members/**", "/apply-list/**","/announcements/**")
                .excludePathPatterns("/announcements/{announcementId}/required-documents/{requiredDocumentId}", "/home/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberInfoArgumentResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173","http://localhost:3000", "https://univ-fit.vercel.app", "https://univ-fit.com", "https://www.univ-fit.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowCredentials(true);
    }





}

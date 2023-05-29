package fiftyfive.administration.usermanagement.utility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomInterceptor(tokenInterceptor))
                .addPathPatterns("/v1/**");
    }

    private record CustomInterceptor(TokenInterceptor tokenInterceptor) implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            String path = request.getServletPath();
            HttpMethod method = HttpMethod.valueOf(request.getMethod());

            // Exclude specific HTTP methods for /v1/users
            if ((method.matches(String.valueOf(HttpMethod.POST)) && path.startsWith("/v1/users"))) {
                return true; // Skip interceptor for excluded methods
            }

            return tokenInterceptor.preHandle(request, response, handler);
        }

        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
            tokenInterceptor.postHandle(request, response, handler, modelAndView);
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            tokenInterceptor.afterCompletion(request, response, handler, ex);
        }
    }

}
package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Value("${model.path}")
    private String modelPath;

    @Value("${model.filename}")
    private String modelFilename;

    @Value("${preprocessing.params.path}")
    private String preprocessingParamsPath;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Cấu hình CORS để frontend có thể gọi API
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }

    @Bean
    public String getModelFullPath() {
        return modelPath + modelFilename;
    }

    @Bean
    public String getPreprocessingParamsPath() {
        return preprocessingParamsPath;
    }
}
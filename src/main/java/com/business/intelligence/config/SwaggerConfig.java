package com.business.intelligence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by yanshi on 2017/3/15.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .pathMapping("/")
                .select()
                .paths(PathSelectors.regex("/data.*"))
                .build();
    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder().title("data sync service")
                .contact(new Contact("niujiaxi", "", "niujiaxi@gomeholdings.com"))
                .description("this is a data sync service api")
                .version("1.0")
                .build();
    }
}

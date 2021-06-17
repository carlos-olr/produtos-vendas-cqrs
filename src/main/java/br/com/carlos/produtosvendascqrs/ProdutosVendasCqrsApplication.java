package br.com.carlos.produtosvendascqrs;

import java.util.Collections;
import java.util.function.Predicate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableAutoConfiguration
public class ProdutosVendasCqrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProdutosVendasCqrsApplication.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2) //
                .select() //
                .apis(RequestHandlerSelectors.any()) //
                .paths(PathSelectors.any())
                .paths(Predicate.not(PathSelectors.regex("/error.*")))
                .build() //
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("API Doc Gerenciador de Pedidos", //
                "API para utilização da aplicação produtos-vendas-cqrs que serve para o gerenciamento de "
                        + "produtos e pedidos utilizando microsserviços (CQRS)."
                        + "\n"
                        + "https://github.com/carlos-olr/produtos-vendas-cqrs", //
                "API V1.0",  //
                null, //
                new Contact("Carlos Oliveira", "https://www.linkedin.com/in/carlosolr/", "carlos.olribeiro@gmail.com"), //
                null, //
                null, Collections.emptyList());
    }

}

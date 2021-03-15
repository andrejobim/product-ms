package br.com.compasso.uol.config;

import com.google.common.base.Predicates;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableAutoConfiguration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
        "br.com.compasso.uol.repo"
})
@EntityScan(basePackages = {
        "br.com.compasso.uol.entity"
})
@ComponentScan(basePackages = {
        "br.com.compasso.uol.controller",
        "br.com.compasso.uol.service"
})
@EnableSwagger2
public class ApplicationConfig  {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.compasso.uol.controller"))
                .paths(PathSelectors.any())
//                .paths(Predicates.not(PathSelectors.regex("/")))
                .build()
                .apiInfo(apiEndPointsInfo());
    }


    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder()
                .title("MicroServiço para Catalogo de Produtos")
                .description("Microserviço para criar, alterar, visualizar e excluir um determinado produto, " +
                        " além de visualizar a lista de produtos atuais disponíveis. " +
                        " Também é possível realizar a busca de produtos filtrando por name, description e price.")
                .contact(new Contact("André Jobim", "https://soulcodesoft.com.br", "contato@soulcodesoft.com.br"))
                .version("1.0.0")
                .build();
    }

}

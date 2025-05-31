package dqcs.dataqualityservice.config.swagger;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Data Quality Service API")
                        .description("Документация REST API сервиса качества данных")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Ларионова Кристина")
                                .email("krist1na.larion0va@yandex.ru")
                        )
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Репозиторий проекта")
                        .url("https://github.com/lrnvkrt/data_quality_service")
                )
                .tags(
                        List.of(
                                new Tag().name("Validation"),
                                new Tag().name("Expectation"),
                                new Tag().name("Datasource"),
                                new Tag().name("Analytics")
                        )
                )
                ;
    }
}

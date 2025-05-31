package dqcs.dataqualityservice.config.clickhouse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource.clickhouse")
public class ClickhouseDataSourceConfig {
    @Bean
    @Qualifier("clickhouseDataSourceConfig")
    public DataSource clickhouseDataSource(
            @Value("${spring.datasource.clickhouse.url}") String url,
            @Value("${spring.datasource.clickhouse.username}") String username,
            @Value("${spring.datasource.clickhouse.password}") String password,
            @Value("${spring.datasource.clickhouse.driver-class-name}") String driverClassName
    ) {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }

    @Bean
    @Qualifier("clickHouseJdbcTemplate")
    public JdbcTemplate clickhouseJdbcTemplate(@Qualifier("clickhouseDataSourceConfig") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

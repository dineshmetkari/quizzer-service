/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.vault.annotation.VaultPropertySource;
import org.springframework.vault.config.EnvironmentVaultConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CompositeFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.Filter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@RestController
@Import(EnvironmentVaultConfiguration.class)
@VaultPropertySource(value = "secret/quizzer")
@EnableOAuth2Client
@EnableSwagger2
public class Application extends WebSecurityConfigurerAdapter {
    private final static Logger LOG = LoggerFactory.getLogger(Application.class);

    private final OAuth2ClientContext oauth2ClientContext;

    @Autowired
    public Application(@Qualifier("oauth2ClientContext") OAuth2ClientContext oauth2ClientContext) {
        this.oauth2ClientContext = oauth2ClientContext;
    }

    public static void main(final String... args) {
        SpringApplication.run(Application.class, args);
    }

    private Filter ssoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(ssoFilter(facebook(), "/login/facebook"));
        filters.add(ssoFilter(github(), "/login/github"));
        filter.setFilters(filters);
        return filter;
    }

    private Filter ssoFilter(ClientResources client, String path) {
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);
        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        filter.setRestTemplate(template);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(
                client.getResource().getUserInfoUri(), client.getClient().getClientId()
        );
        tokenServices.setRestTemplate(template);
        filter.setTokenServices(tokenServices);
        return filter;
    }

    @Bean
    @ConfigurationProperties("github")
    public ClientResources github() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("facebook")
    public ClientResources facebook() {
        return new ClientResources();
    }

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and().antMatcher("/**")
                .authorizeRequests()
                .antMatchers(
                        "/", "/login**", "/webjars/**",
                        "/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs"
                ).permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
                .and().logout().logoutSuccessUrl("/start.html").permitAll()
                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers("/polls/**", "/certificates/**", "/votes/**")
                .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
        ;
    }

    @EnableCassandraRepositories
    @Configuration
    static class ConfigurationCassandra extends AbstractCassandraConfiguration {
        @Value("${spring.data.cassandra.contact-points}")
        private String contactPoints;

        @Value("${spring.data.cassandra.keyspace-name}")
        private String keyspaceName;

        @Override
        public String getContactPoints() {
            return contactPoints;
        }

        @Override
        protected String getKeyspaceName() {
            return keyspaceName;
        }
    }

    @EnableCouchbaseRepositories
    @Configuration
    static class ConfigurationCouchbase extends AbstractCouchbaseConfiguration {
        @Value("${couchbase.cluster.bucket}")
        private String bucketName;

        @Value("${couchbase.cluster.password}")
        private String password;

        @Value("${couchbase.cluster.ip}")
        private String ip;

        @Override
        protected List<String> getBootstrapHosts() {
            return Collections.singletonList(this.ip);
        }

        @Override
        protected String getBucketName() {
            return this.bucketName;
        }

        @Override
        protected String getBucketPassword() {
            return this.password;
        }
    }
}

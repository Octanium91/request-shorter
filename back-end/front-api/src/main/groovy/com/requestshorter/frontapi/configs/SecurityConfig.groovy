package com.requestshorter.frontapi.configs

import groovy.util.logging.Slf4j
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

        http
                .authorizeRequests()
                .antMatchers('/api/open/**', '/api/shortContent/create', '/api/shortContent/statistic/count', '/api/click/statistic', '/inner/*').permitAll()
                .anyRequest().authenticated()

        http
                .formLogin()
                .loginProcessingUrl('/api/login')
                .successHandler { req, res, e -> res.setStatus(200) }
                .failureHandler { req, res, e ->
                    res.setStatus(401)
                }

                .and()
                .logout()
                .logoutUrl('/api/logout')
                .logoutSuccessHandler { req, res, e -> res.setStatus(200) }
    }
}

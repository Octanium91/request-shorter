package com.requestshorter.frontapi.configs

import groovy.util.logging.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        new BCryptPasswordEncoder()
    }

    @Bean
    SessionRegistry sessionRegistry() {
        new SessionRegistryImpl()
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(new UserDetailsService() {

                    @Override
                    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//                        log.info("Find user by name $username")
                        userRepository
                                .findByEmail(email)
                                .map {
                                    if (it.blocked) {
                                        throw new UsernameNotFoundException("User ${email} is blocked!")
                                    } else {
                                        new User(email, it.passwordHash, it.roles.collect({ role -> new SimpleGrantedAuthority(role) }))
                                    }
                                }
                                .orElseThrow { throw new UsernameNotFoundException("User by email ${email} not found") }
                    }
                })
                .passwordEncoder(passwordEncoder())
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

        http
                .sessionManagement()
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry())
                .expiredUrl('/auth/login')

        http
                .authorizeRequests()
                .antMatchers('/api/shortContent/create', '/api/shortContent/statistic/count', '/api/click/statistic', '/inner/*').permitAll()
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

package com.requestshorter.frontapi.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.servlet.http.HttpSession
import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener

@Configuration
class HttpSessionConfig {

    private static final Map<String, HttpSession> sessions = new HashMap<>();

    List<HttpSession> getActiveSessions() {
        return new ArrayList<>(sessions.values())
    }

    @Bean
    HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {
            @Override
            void sessionCreated(HttpSessionEvent hse) {
                sessions.put(hse.getSession().getId(), hse.getSession())
            }

            @Override
            void sessionDestroyed(HttpSessionEvent hse) {
                sessions.remove(hse.getSession().getId())
            }
        }
    }
}

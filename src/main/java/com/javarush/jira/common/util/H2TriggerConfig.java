package com.javarush.jira.common.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Конфигурация для инициализации триггеров в H2
 */
@Configuration
public class H2TriggerConfig {

    /**
     * Инициализирует триггеры для H2 перед запуском Liquibase
     * Bean создается только для профиля "test"
     */
    @Bean
    @Profile("test") // Активируется только для тестового профиля
    public Boolean initH2Triggers(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        try {
            // Регистрируем Java-класс для использования в триггере
            jdbcTemplate.execute(
                    "CREATE ALIAS IF NOT EXISTS USER_BELONG_UNIQUENESS_CHECK FOR " +
                            "\"com.javarush.jira.common.util.UserBelongUniquenessCheck\"");

            // Удаляем триггер если он уже существует
            jdbcTemplate.execute(
                    "DROP TRIGGER IF EXISTS tr_user_belong_uniq_check");

            // Создаем триггер заново, используя зарегистрированный класс
            jdbcTemplate.execute(
                    "CREATE TRIGGER tr_user_belong_uniq_check " +
                            "BEFORE INSERT, UPDATE ON USER_BELONG " +
                            "FOR EACH ROW " +
                            "CALL \"USER_BELONG_UNIQUENESS_CHECK\"");

            return true;
        } catch (Exception e) {
            // Логируем ошибку, но не прерываем старт приложения
            System.err.println("Failed to initialize H2 triggers: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

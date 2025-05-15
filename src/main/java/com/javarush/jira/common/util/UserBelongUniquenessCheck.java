package com.javarush.jira.common.util;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Триггер для проверки уникальности активных записей в таблице USER_BELONG.
 * Проверка уникальности комбинации (OBJECT_ID, OBJECT_TYPE, USER_ID, USER_TYPE_CODE) для записей с NULL в ENDPOINT.
 */
public class UserBelongUniquenessCheck implements Trigger {

    @Override
    public void init(Connection conn, String schemaName, String triggerName,
                     String tableName, boolean before, int type) {
        // Инициализация триггера
    }

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow)
            throws SQLException {
        // Проверка запускается только если ENDPOINT равен NULL (активная запись)
        if (newRow[5] == null) { // индекс 5 соответствует столбцу ENDPOINT
            Long objectId = (Long) newRow[0]; // OBJECT_ID
            Short objectType = (Short) newRow[1]; // OBJECT_TYPE
            Long userId = (Long) newRow[2]; // USER_ID
            String userTypeCode = (String) newRow[3]; // USER_TYPE_CODE

            // SQL для проверки существования дублирующихся активных записей
            String sql = "SELECT COUNT(*) FROM USER_BELONG " +
                    "WHERE OBJECT_ID = ? AND OBJECT_TYPE = ? " +
                    "AND USER_ID = ? AND USER_TYPE_CODE = ? " +
                    "AND ENDPOINT IS NULL";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, objectId);
                stmt.setShort(2, objectType);
                stmt.setLong(3, userId);
                stmt.setString(4, userTypeCode);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException(
                                "Нарушение уникального ограничения: активная запись с " +
                                        "OBJECT_ID=" + objectId + ", OBJECT_TYPE=" + objectType + ", " +
                                        "USER_ID=" + userId + ", USER_TYPE_CODE='" + userTypeCode + "' " +
                                        "уже существует в таблице USER_BELONG");
                    }
                }
            }
        }
    }

    @Override
    public void close() {
        // Очистка ресурсов при необходимости
    }

    @Override
    public void remove() {
        // Удаление триггера
    }
}

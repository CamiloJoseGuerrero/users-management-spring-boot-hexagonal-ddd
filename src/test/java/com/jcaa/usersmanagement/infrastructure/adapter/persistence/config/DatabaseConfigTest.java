package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DatabaseConfig")
class DatabaseConfigTest {

  @Test
  @DisplayName("buildJdbcUrl() genera URL de MySQL con el constructor de compatibilidad")
  void shouldBuildMySqlUrl() {
    // Arrange
    final DatabaseConfig config = new DatabaseConfig("localhost", 3306, "crud_usuarios", "root", "x");

    // Act
    final String url = config.buildJdbcUrl();

    // Assert
    assertEquals(
        "jdbc:mysql://localhost:3306/crud_usuarios?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
        url);
  }

  @Test
  @DisplayName("buildJdbcUrl() genera URL de PostgreSQL con sslmode")
  void shouldBuildPostgreSqlUrl() {
    // Arrange
    final DatabaseConfig config =
        new DatabaseConfig(
            DatabaseEngine.POSTGRESQL, "db.host", 5432, "postgres", "user", "x", "require");

    // Act
    final String url = config.buildJdbcUrl();

    // Assert
    assertEquals("jdbc:postgresql://db.host:5432/postgres?sslmode=require", url);
  }

  @Test
  @DisplayName("DatabaseEngine.fromString() ignora mayusculas y espacios; rechaza valores invalidos")
  void shouldParseEngine() {
    // Act & Assert
    assertAll(
        () -> assertEquals(DatabaseEngine.POSTGRESQL, DatabaseEngine.fromString(" PostgreSQL ")),
        () -> assertEquals(DatabaseEngine.MYSQL, DatabaseEngine.fromString("mysql")),
        () -> assertThrows(IllegalArgumentException.class, () -> DatabaseEngine.fromString("oracle")));
  }
}

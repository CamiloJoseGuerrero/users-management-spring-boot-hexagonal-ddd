package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

public record DatabaseConfig(
    DatabaseEngine engine,
    String host,
    int port,
    String databaseName,
    String username,
    String password,
    String sslMode) {

  /** Constructor de compatibilidad: MySQL sin modo SSL explícito. */
  public DatabaseConfig(
      final String host,
      final int port,
      final String databaseName,
      final String username,
      final String password) {
    this(DatabaseEngine.MYSQL, host, port, databaseName, username, password, null);
  }

  public String buildJdbcUrl() {
    return String.format(engine.urlTemplate(), host, port, databaseName, sslMode);
  }
}

package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import java.util.Locale;
import java.util.Objects;

/** Motores de base de datos soportados por los adaptadores de persistencia. */
public enum DatabaseEngine {
  MYSQL("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"),
  POSTGRESQL("jdbc:postgresql://%s:%d/%s?sslmode=%s");

  private final String urlTemplate;

  DatabaseEngine(final String urlTemplate) {
    this.urlTemplate = urlTemplate;
  }

  public String urlTemplate() {
    return urlTemplate;
  }

  public static DatabaseEngine fromString(final String value) {
    Objects.requireNonNull(value, "Database engine cannot be null");
    return DatabaseEngine.valueOf(value.trim().toUpperCase(Locale.ROOT));
  }
}

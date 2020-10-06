package com.dimaslanjaka.tools.Helpers.database;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

public class SQLBuilder {
	private final static SQLBuilder INSTANCE = new SQLBuilder();
	public static StringBuilder sql;

	public static SQLBuilder update(String tableName, String[] columnName, String[] values) throws Exception {
		StringBuilder builder = new StringBuilder("UPDATE");
		builder.append(" ").append(tableName).append(" SET ");
		if (columnName.length > values.length) {
			throw new Exception("Column name and values length not same length");
		} else if (columnName.length < values.length) {
			throw new Exception("Column name and values length not same length");
		}
		for (int i = 0; i < columnName.length; i++) {
			String key = columnName[i];
			String value = values[i];
			builder.append(key).append(" = ").append("'").append(value).append("'").append(" ");
			if (columnName.length - 1 != i) {
				builder.append(", ");
			}
		}
		sql = builder;
		return INSTANCE;
	}

	public static SQLBuilder whereEquals(String[] keys, String[] values) throws Exception {
		sql.append(" WHERE ");
		if (keys.length > values.length) {
			throw new Exception("Column name and values length not same length");
		} else if (keys.length < values.length) {
			throw new Exception("Column name and values length not same length");
		}
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			String value = values[i];
			sql.append(key).append("=").append("'").append(value).append("'");
			if (keys.length - 1 != i) {
				sql.append(" AND ");
			}
		}
		return INSTANCE;
	}

	@NonNull
	@NotNull
	@Override
	public String toString() {
		return sql.toString().trim();
	}
}

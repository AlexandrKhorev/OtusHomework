package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> entityClassMetaData;
    private final String tableName;
    private final String idField;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
        this.tableName = entityClassMetaData.getName();
        this.idField = entityClassMetaData.getIdField().getName();
    }

    @Override
    public String getSelectAllSql() {
        return String.format("SELECT * FROM %s", tableName);
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("SELECT * FROM %s WHERE %s = ?", tableName, idField);
    }

    @Override
    public String getInsertSql() {
        return String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, getColumnNames(), getValues());
    }

    @Override
    public String getUpdateSql() {
        return String.format("UPDATE %s SET %s WHERE %s = ?", tableName, getUpdateFields(), idField);
    }

    private String getColumnNames() {
        return mapAndJoinList(entityClassMetaData.getFieldsWithoutId(), Field::getName);
    }

    private String getValues() {
        return mapAndJoinList(entityClassMetaData.getFieldsWithoutId(), field -> "?");
    }

    private String getUpdateFields() {
        return mapAndJoinList(entityClassMetaData.getFieldsWithoutId(), field -> field.getName() + " = ?");
    }

    private String mapAndJoinList(List<Field> fields, Function<? super Field, ? extends String> function) {
        return fields.stream().map(function).collect(Collectors.joining(", "));
    }
}

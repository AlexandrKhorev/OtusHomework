package ru.otus.jdbc.mapper;

import ru.otus.annotations.Id;
import ru.otus.jdbc.exceptions.EntityClassMetaDataException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;
    private Constructor<T> constructor;
    private Field id;
    private final List<Field> fieldsWithoutId = new ArrayList<>();
    private final List<Field> allFields = new ArrayList<>();

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }


    @Override
    public String getName() {
        // Возвращает имя таблицы
        return clazz.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        // Ищем пустой публичный конструктор
        if (constructor == null) {
            for (var publicConstructor : clazz.getConstructors()) {
                System.out.println(publicConstructor);
                if (publicConstructor.getParameterCount() == 0) {
                    // Если нашелся, сохраняем, чтобы в следующий раз не использовать рефлексию снова
                    constructor = (Constructor<T>) publicConstructor;
                }
            }
            // Если такой конструктор не найден, выбрасываем исключение
            if (constructor == null) {
                throw new EntityClassMetaDataException("Missing empty public constructor from class: " + clazz);
            }
        }
        return constructor;
    }

    @Override
    public Field getIdField() {
        // Ленивая инициализация полей, метод с рефлексией initFields вызовется один раз
        if (id == null) {
            initFields();
        }
        return id;
    }

    @Override
    public List<Field> getAllFields() {
        if (allFields.isEmpty()) {
            allFields.addAll(getFieldsWithoutId());
            allFields.add(getIdField());
        }
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (fieldsWithoutId.isEmpty()) {
            initFields();
        }
        return fieldsWithoutId;
    }

    private void initFields() {
        // Инициализация полей. Добавляем все поля без аннотации Id в fieldsWithoutId, с аннотацией приравниваем к полю id.
        // Если после цикла id остался null, выбрасываем исключение.
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                id = field;
            } else {
                fieldsWithoutId.add(field);
            }
        }
        if (id == null) {
            throw new EntityClassMetaDataException("Field with annotation '@Id' not found in class: " + clazz);
        }
    }
}

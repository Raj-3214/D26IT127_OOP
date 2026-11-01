package service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.annotation.Id;

public class Repository<T> {
    private final Map<String, T> entities = new HashMap<>();

    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        String id = findId(entity);
        entities.put(id, entity);
        return entity;
    }

    public T findById(String id) {
        return entities.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(entities.values());
    }

    public boolean delete(String id) {
        return entities.remove(id) != null;
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        for (T item : items) {
            save(item);
        }
    }

    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Values cannot be null or empty");
        }

        double total = 0;
        for (N value : values) {
            if (value == null) {
                throw new IllegalArgumentException("Values cannot contain null");
            }
            total += value.doubleValue();
        }
        return total / values.length;
    }

    private String findId(T entity) {
        Class<?> currentClass = entity.getClass();
        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Id.class)) {
                    continue;
                }
                field.setAccessible(true);
                try {
                    Object value = field.get(entity);
                    if (value == null) {
                        throw new IllegalArgumentException("@Id field cannot be null");
                    }
                    return value.toString();
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Unable to read @Id field", e);
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        throw new IllegalArgumentException("Entity has no field annotated with @Id");
    }
}

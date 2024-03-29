package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        processConfig(initialConfigClass);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        // Если добавлено несколько компонентов с одним интерфейсом, при попытке достать такой по интерфейсу или классу, выбрасывается исключение
        var component = appComponents.stream()
                .filter(c -> componentClass.isAssignableFrom(c.getClass()))
                .toList();
        if (component.size() != 1) {
            throw new AppComponentException(String.format("The config class contains 0 or more than 1 component: %s", componentClass));
        }
        return (C) component.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        // Достаем компонент по имени
        var component = appComponentsByName.get(componentName);
        if (component == null){
            throw new AppComponentException(String.format("The config class don't contains component with name %s", componentName));
        }
        return (C) component;
    }

    private void processConfig(Class<?> configClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        checkConfigClass(configClass);

        // Создаем объект конфига для создания компонентов
        var config = configClass.getConstructor().newInstance();

        // Берем методы из класса-конфига, сортируем по order
        var sortedMethods = Arrays.stream(configClass.getDeclaredMethods())
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .toList();

        for (var method : sortedMethods) {
            createComponent(method, config);
        }
    }

    private void createComponent(Method method, Object config) throws InvocationTargetException, IllegalAccessException {

        var componentName = method.getAnnotation(AppComponent.class).name();
        // Если компонент с таким именем уже есть, выбрасываем исключение
        if (appComponentsByName.containsKey(componentName)) {
            throw new AppComponentException("A component with the same name already exists");
        }
        var arguments = getArguments(method);

        // Создаем объект и сохраняем
        Object component = method.invoke(config, arguments.toArray());
        appComponents.add(component);
        appComponentsByName.put(componentName, component);
    }

    private List<Object> getArguments(Method method) {
        var argsForCreateComponents = new ArrayList<>();
        var methodParameters = method.getParameterTypes();

        for (var param : methodParameters) {
            // Если компонент не создан, выбросится исключение.
            argsForCreateComponents.add(getAppComponent(param));
        }
        return argsForCreateComponents;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}

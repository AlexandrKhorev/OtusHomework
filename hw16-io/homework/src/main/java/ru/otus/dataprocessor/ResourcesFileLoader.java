package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;
    private final Gson gson = new Gson();

    private final Type listMeasurementsType = new TypeToken<List<Measurement>>() {
    }.getType();

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        try (var reader = new InputStreamReader(ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName))) {
            return gson.fromJson(reader, listMeasurementsType);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}

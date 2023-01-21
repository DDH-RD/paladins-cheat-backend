package dev.luzifer.json;

import com.google.gson.Gson;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

@UtilityClass
public class JsonUtil {

    private final Gson GSON = new Gson();

    public String toJson(Object object) {
        return GSON.toJson(object);
    }

    public void toFile(Object object, File file) {
        try(Writer writer = new FileWriter(file)) {
            GSON.toJson(object, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromFile(File file, Class<T> clazz) {
        try(Reader reader = new FileReader(file)) {
            return GSON.fromJson(reader, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public <T> T fromJson(String json, Type type) {
        return GSON.fromJson(json, type);
    }

}


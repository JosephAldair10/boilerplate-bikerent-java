package com.trio.java.bikerentapi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.Objects;

public final class ObjectMapperHelper {

    private static ObjectMapperHelper INSTANCE = new ObjectMapperHelper();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ObjectMapperHelper() {
    }

    public static ObjectMapperHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ObjectMapperHelper();
        }

        return INSTANCE;
    }

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public <T> T converFileToObject(String filename, Class<T> classType) {
        if (filename == null) {
            throw new IllegalArgumentException("File name must be specified");
        }

        T object = null;
        try {
            object = objectMapper.readValue(getFileContent(filename), classType);
        } catch (IOException ex) {
            throw new IllegalArgumentException("File content is not compatible");
        }

        return object;
    }

    public <T> T converContentToObject(String value, Class<T> classType) {
        if (value == null) {
            throw new IllegalArgumentException("File name must be specified");
        }

        T object = null;
        try {
            object = objectMapper.readValue(value, classType);
        } catch (IOException ex) {
            throw new IllegalArgumentException("File content is not compatible");
        }

        return object;
    }

    public String getFileContent(String fileName) {
        try {
            return new String(Objects.requireNonNull(
                    this.getClass().getClassLoader().getResourceAsStream(fileName))
                    .readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error getting file content", e);
        }
    }

    public String convertObjectToString(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object must not be null");
        }

        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to String", e);
        }
    }
}

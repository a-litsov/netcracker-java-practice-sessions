package com.netcracker.adlitsov.users;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UsersFileManager {
    private static final String USERS_DIR_PATH = System.getProperty("java.io.tmpdir") + File.separator + "adlitsov-java-7";
    private static final String USERS_FULL_PATH = USERS_DIR_PATH + File.separator + "users.json";

    private File createUsersFile() {
        File usersFile = null;
        try {
            usersFile = new File(USERS_DIR_PATH);
            if (!usersFile.exists()) {
                usersFile.mkdir();
            }
            usersFile = new File(USERS_FULL_PATH);
            if (!usersFile.exists()) {
                usersFile.createNewFile();
            }
        } catch (IOException|SecurityException e) {
            System.err.println("Cannot create " + USERS_FULL_PATH + " file!");
            e.printStackTrace();
        }
        return usersFile;
    }

    public void saveUsersInfo(Map<String, String> users) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            File booksFile = createUsersFile();
            mapper.writeValue(booksFile, users);
        } catch (IOException e) {
            System.err.println("Cannot save users to file!");
            e.printStackTrace();
        }
    }

    public Map<String, String> loadUsers() {
        Map<String, String> users = new HashMap<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        File usersFile = new File(USERS_FULL_PATH);
        if (!usersFile.exists()) {
            createUsersFile();
            return new HashMap<>();
        }

        try {
            users = mapper.readValue(usersFile, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            System.err.println("Cannot load users!");
            users.clear();
            e.printStackTrace();
        }
        return users;
    }
}

package com.netcracker.adlitsov.users;

import java.util.*;
import java.util.stream.Collectors;

public class UsersInfoStorage {
    private static UsersInfoStorage storage;

    private UsersFileManager fileManager = new UsersFileManager();
    private Map<String, String> users = new HashMap<>();

    private UsersInfoStorage() {
        users = fileManager.loadUsers();
    }

    public static UsersInfoStorage getInstance() {
        return (storage == null) ? (storage = new UsersInfoStorage()) : storage;
    }

    public synchronized void addUser(String userName, String userPassword) {
        users.put(userName, userPassword);
        fileManager.saveUsersInfo(users);
    }

    public synchronized void deleteUser(String userName) {
        users.remove(userName);
        fileManager.saveUsersInfo(users);
    }

    public boolean verify(String userName, String userPassword) {
        return Objects.equals(users.get(userName), userPassword);
    }

    public boolean isUserExist(String userName) {
        return users.containsKey(userName);
    }
}

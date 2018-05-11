package com.netcracker.adlitsov.users;

import java.util.*;

public class UsersInfoStorage {
    private static final Boolean IS_BANNED = false;

    private static UsersInfoStorage storage;

    private UsersFileManager fileManager = new UsersFileManager();
    private Map<String, UserInfo> users = new HashMap<>();

    private UsersInfoStorage() {
        users = fileManager.loadUsers();
    }

    public static UsersInfoStorage getInstance() {
        return (storage == null) ? (storage = new UsersInfoStorage()) : storage;
    }

    public synchronized void addUser(String userName, String userPassword) {
        users.put(userName, new UserInfo(userPassword, IS_BANNED));
        fileManager.saveUsersInfo(users);
    }

    public synchronized void changeUserPass(String userName, String newPass) {
        users.get(userName).setPassword(newPass);
    }

    public synchronized void deleteUser(String userName) {
        users.remove(userName);
        fileManager.saveUsersInfo(users);
    }

    public boolean verify(String userName, String userPassword) {
        return Objects.equals(users.get(userName).getPassword(), userPassword);
    }

    public boolean isUserExist(String userName) {
        return users.containsKey(userName);
    }

    public void banUser(String userName) {
        final boolean banned = true;
        if (isUserExist(userName)) {
            users.get(userName).setBanned(banned);
            fileManager.saveUsersInfo(users);
        }
    }

    public void unbanUser(String userName) {
        final boolean banned = false;
        if (isUserExist(userName)) {
            users.get(userName).setBanned(false);
            fileManager.saveUsersInfo(users);
        }
    }

    public boolean isUserBanned(String userName) {
        return users.get(userName).isBanned();
    }
}

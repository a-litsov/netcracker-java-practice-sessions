package com.netcracker.adlitsov.users;

public class UserInfo {
    private String password;
    private boolean isBanned;

    public UserInfo() {
    }

    public UserInfo(String password, boolean isBanned) {
        this.password = password;
        this.isBanned = isBanned;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}

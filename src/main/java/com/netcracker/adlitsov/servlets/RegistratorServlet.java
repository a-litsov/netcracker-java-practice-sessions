package com.netcracker.adlitsov.servlets;

import com.netcracker.adlitsov.users.UsersInfoStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegistratorServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        UsersInfoStorage users = UsersInfoStorage.getInstance();

        if (!users.isUserExist(login)) {
            users.addUser(login, password);
            resp.getWriter().append("Hello ").append(login)
                            .append(", you registered with password:").append(password);
        } else {
            resp.getWriter().append("We sorry but user with similar login already exists.");
        }
    }
}

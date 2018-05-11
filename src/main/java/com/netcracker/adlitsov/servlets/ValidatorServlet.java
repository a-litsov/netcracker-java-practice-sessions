package com.netcracker.adlitsov.servlets;

import com.netcracker.adlitsov.users.UsersInfoStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ValidatorServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        UsersInfoStorage users = UsersInfoStorage.getInstance();

        if (users.isUserExist(login)) {
            if (users.verify(login, password)) {
                resp.getWriter().append("Hello ").append(login).append(", you entered correct password.");
            } else {
                resp.getWriter().append("Entered password is incorrect. Try again (after 5 incorrect tries you'll be locked).");
            }
        } else {
            resp.sendRedirect("register.html");
        }
    }
}

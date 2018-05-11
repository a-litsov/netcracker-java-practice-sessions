package com.netcracker.adlitsov.servlets;

import com.netcracker.adlitsov.users.UsersInfoStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ValidatorServlet extends HttpServlet {

    private static final String INCORRECT_PREFIX = "Incorrect login as: ";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        UsersInfoStorage users = UsersInfoStorage.getInstance();

        HttpSession session = req.getSession(true);
        if (login.isEmpty() && password.isEmpty()) {
            String storedLogin = (String)session.getAttribute("login");
            if (storedLogin != null && users.isUserExist(storedLogin)) {
                resp.getWriter().append("Hello ").append(storedLogin).append(", you're logged via session.");
                return;
            }
        }

        if (users.isUserExist(login)) {
            if (users.verify(login, password)) {
                resp.getWriter().append("Hello ").append(login).append(", you entered correct password.");
                session.setAttribute("login", login);
            } else {
                resp.getWriter().append("Entered password is incorrect. Try again (after 5 incorrect tries you'll be locked).");
                Integer count = (Integer)session.getAttribute(INCORRECT_PREFIX + login);
                if (count == null) {
                    count = 1;
                }
                session.setAttribute(INCORRECT_PREFIX + login, ++count);
                resp.getWriter().append("Current count: ").append(count.toString());
            }
        } else {
            resp.sendRedirect("register.html");
        }
    }


}

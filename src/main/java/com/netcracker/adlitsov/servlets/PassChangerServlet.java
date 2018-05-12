package com.netcracker.adlitsov.servlets;

import com.netcracker.adlitsov.users.UsersInfoStorage;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class PassChangerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String prevPassword = req.getParameter("prevPassword");
        String password = req.getParameter("password");

        UsersInfoStorage users = UsersInfoStorage.getInstance();

        if (users.isUserExist(login) && users.verify(login, prevPassword)) {
            users.changeUserPass(login, password);
            users.unbanUser(login);
            HttpSession session = req.getSession();
            String incorrectLogin = ValidatorServlet.INCORRECT_PREFIX + login;
            session.removeAttribute(incorrectLogin);
            resp.getWriter().append(login).append(", your new password is: ").append(password);
        } else {
            resp.getWriter().append("We sorry but user with that login doesn't exist ")
                            .append("or entered pass isn't correct.");
        }
    }
}

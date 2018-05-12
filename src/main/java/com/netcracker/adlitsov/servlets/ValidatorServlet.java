package com.netcracker.adlitsov.servlets;

import com.netcracker.adlitsov.users.UsersInfoStorage;
import com.sun.deploy.net.HttpRequest;
import javafx.util.Pair;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class ValidatorServlet extends HttpServlet {

    public static final String INCORRECT_PREFIX = "Incorrect login as: ";
    private static final int TRIES_UNTIL_BAN = 5;
    private static final boolean USE_COOKIES = true;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        boolean remember = req.getParameter("remember") != null;

        UsersInfoStorage users = UsersInfoStorage.getInstance();

        if (login.isEmpty() && password.isEmpty()) {
            Pair<String, String> userData = loadUserInfo(req);
            String storedLogin = userData.getKey();
            String storedPass = userData.getValue();
            if (storedLogin != null && storedPass != null) {
                if (!users.isUserBanned(storedLogin)) {
                    if (users.verify(storedLogin, storedPass)) {
                        resp.getWriter().append("Hello ").append(storedLogin).append(", you're logged via remembered data.");
                    } else {
                        incorrectLogin(req, resp, login, password, users);
                    }
                } else {
                    resp.sendRedirect("change-pass.html");
                }
                return;
            }
        }

        if (users.isUserExist(login)) {
            if (users.isUserBanned(login)) {
                resp.sendRedirect("change-pass.html");
                return;
            }

            if (users.verify(login, password)) {
                if (remember) {
                    storeUserInfo(req, resp, login, password);
                }
                resp.getWriter().append("Hello ").append(login).append(", you entered correct password.");
            } else {
                incorrectLogin(req, resp, login, password, users);
            }
        } else {
            resp.sendRedirect("register.html");
        }
    }

    private void incorrectLogin(HttpServletRequest req, HttpServletResponse resp, String login, String password,
                                UsersInfoStorage users) throws IOException {
        resp.getWriter().append("Entered password is incorrect. Try again (after ")
            .append(TRIES_UNTIL_BAN + " incorrect tries account will be locked).");

        HttpSession session = req.getSession(true);
        Integer count = (Integer) session.getAttribute(INCORRECT_PREFIX + login);
        if (count == null) {
            count = 1;
        } else if (++count >= TRIES_UNTIL_BAN) {
            users.banUser(login);
            session.removeAttribute(INCORRECT_PREFIX + login);
        }
        session.setAttribute(INCORRECT_PREFIX + login, count);
        resp.getWriter().append("Current tries count: ").append(count.toString());
    }

    private Pair<String, String> loadUserInfo(HttpServletRequest req) {
        if (USE_COOKIES) {
            Cookie[] cookies = req.getCookies();
            if (cookies == null) {
                return new Pair<>(null, null);
            }
            String login = null, pass = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("login")) {
                    login = cookie.getValue();
                }
                if (cookie.getName().equals("password")) {
                    pass = cookie.getValue();
                }
            }
            return new Pair<>(login, pass);
        } else {
            HttpSession session = req.getSession(true);

            String storedLogin = (String) session.getAttribute("login");
            String storedPass = (String) session.getAttribute("password");

            return new Pair<>(storedLogin, storedPass);
        }
    }

    private void storeUserInfo(HttpServletRequest req, HttpServletResponse resp, String login, String pass) {
        if (USE_COOKIES) {
            resp.addCookie(new Cookie("login", login));
            resp.addCookie(new Cookie("password", pass));
        } else {
            HttpSession session = req.getSession(true);
            session.setAttribute("login", login);
            session.setAttribute("password", pass);
        }
    }
}

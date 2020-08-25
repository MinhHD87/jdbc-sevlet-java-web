package com.laptrinhjavaweb.contronller.web;

import com.laptrinhjavaweb.model.UserModel;
import com.laptrinhjavaweb.service.ICategoryService;
import com.laptrinhjavaweb.service.INewService;
import com.laptrinhjavaweb.service.IUserService;
import com.laptrinhjavaweb.utils.FormUtil;
import com.laptrinhjavaweb.utils.SessionUtil;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

@WebServlet(urlPatterns = {"/trang-chu", "/dang-nhap", "/thoat"})
public class HomeController extends HttpServlet {

    /**
     *
     */
    @Inject
    private ICategoryService categoryService;
    @Inject
    private INewService newService;
    @Inject
    private IUserService userService;
    private static final long serialVersionUID = 5116970293077067835L;
    ResourceBundle resourceBundle = ResourceBundle.getBundle("message");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action != null && action.equals("login")) {
            String message = req.getParameter("message");
            String alert = req.getParameter("alert");
            if (message != null && alert != null) {
                req.setAttribute("message", resourceBundle.getString(message));
                req.setAttribute("alert", alert);
            }
            RequestDispatcher rd = req.getRequestDispatcher("/views/login.jsp");
            rd.forward(req, resp);
        } else if (action != null && action.equals("logout")) {
            SessionUtil.getInstance().removeValue(req, "USERMODEL");
            resp.sendRedirect(req.getContextPath() + "/trang-chu");
        } else {
            RequestDispatcher rd = req.getRequestDispatcher("/views/web/home.jsp");
            rd.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action != null && action.equals("login")) {
            UserModel model = (UserModel) FormUtil.toModel(UserModel.class, req);
            model = userService.findByUserNameAndPasswordAndStatus(model.getUserName(), model.getPassword(), 1);
            if (model != null) {
                SessionUtil.getInstance().putValue(req, "USERMODEL", model);
                if
                (model.getRole().getCode().equals("USER")) {
                    System.out.println(model.getUserName());
                    resp.sendRedirect(req.getContextPath() + "/trang-chu");
                } else if (model.getRole().getCode().equals("ADMIN")) {
                    resp.sendRedirect(req.getContextPath() + "/admin-home");
                }

            } else {
                resp.sendRedirect(req.getContextPath() + "/dang-nhap?action=login&message=username_password_invalid&alert=danger");
            }

        }

    }
}

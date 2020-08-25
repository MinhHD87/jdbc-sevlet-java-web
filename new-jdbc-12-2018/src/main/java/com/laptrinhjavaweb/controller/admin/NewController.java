package com.laptrinhjavaweb.controller.admin;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laptrinhjavaweb.constant.SystemConstant;
import com.laptrinhjavaweb.model.NewModel;
import com.laptrinhjavaweb.paging.PageRequest;
import com.laptrinhjavaweb.paging.Pageble;
import com.laptrinhjavaweb.service.ICategoryService;
import com.laptrinhjavaweb.service.INewService;
import com.laptrinhjavaweb.sort.Sorter;
import com.laptrinhjavaweb.utils.FormUtil;

@WebServlet(urlPatterns = {"/admin-new"})
public class NewController extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 5116970293077067835L;

    @Inject
    private INewService newService;
    @Inject
    private ICategoryService categoryService;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NewModel model = FormUtil.toModel(NewModel.class, req);
        String view = "";
        if (model.getType().equals(SystemConstant.LIST)) {
            Pageble pageble = new PageRequest(model.getPage(), model.getMaxPageItem(),
                    new Sorter(model.getSortName(), model.getSortBy()));
            model.setListResult(newService.findAll(pageble));
            model.setTotalItem(newService.getTotalItem());
            model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getMaxPageItem()));
            view = "/views/admin/new/list.jsp";

        } else if (model.getType().equals(SystemConstant.EDIT)) {
            if (model.getId() != null) {
                model = newService.findOne(model.getId());

            }
            req.setAttribute("categories", categoryService.findAll());
            view = "/views/admin/new/edit.jsp";
        }

        if (req.getParameter("message") != null) {
            String messageResponse = "";
            String alert = "";
            String message = req.getParameter("message");
            if (message.equals("insert_success")) {
                messageResponse = "Insert Success";
                alert = "success";

            } else if (message.equals("update_success")) {
                messageResponse = "Update Success";
                alert = "success";
            } else if (message.equals("delete_success")) {
                messageResponse = "Delete Success";
                alert = "success";
            } else if (message.equals("error_system")) {
                messageResponse = "Error Success";
                alert = "danger";
            }
            req.setAttribute("messageResponse", messageResponse);
            req.setAttribute("alert", alert);
        }
        req.setAttribute(SystemConstant.MODEL, model);
        RequestDispatcher rd = req.getRequestDispatcher(view);
        rd.forward(req, resp);


    }
}

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.capedwarf.todolist.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.jboss.capedwarf.todolist.dao.TasksDAO;
import org.jboss.capedwarf.todolist.domain.Task;
import org.jboss.capedwarf.todolist.html.HtmlHelper;
import org.jboss.capedwarf.todolist.html.HtmlPage;
import org.jboss.capedwarf.todolist.log.AuditLog;
import org.jboss.capedwarf.todolist.log.DsAuditLog;
import org.jboss.capedwarf.todolist.queue.QueueHelper;

/**
 * Servlet implementation class Index
 */
//@WebServlet({ "/index" })
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private String redirect;

    private AuditLog auditLog;
    private TasksDAO tasksDAO;
    private UserService userService;

    public Index() {
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        String contextPath = config.getServletContext().getContextPath();
        if (contextPath.startsWith("/")) {
            redirect = contextPath;
        } else {
            redirect = "/" + contextPath;
        }

        auditLog = new DsAuditLog();
        tasksDAO = new TasksDAO();
        userService = UserServiceFactory.getUserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = userService.getCurrentUser();
        if (user != null) {
            String from = request.getParameter("from");
            if (from != null) {
                doFetch(request, response, user);
            } else {
                doTasks(request, response, user);
            }
        } else {
            doLogin(request, response);
        }
    }

    protected void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginURL = userService.createLoginURL(redirect);
        response.sendRedirect(loginURL);
    }

    protected void doFetch(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        HtmlPage htmlPage = new HtmlPage(request.getContextPath());
        htmlPage.addToBody(HtmlHelper.getTitle("ToDo LiSt - AuDiT LoG"));
        htmlPage.addToBody(getLogut(request));
        htmlPage.addToBody(HtmlHelper.auditLogsAsHtml(auditLog.fetch(Long.parseLong(request.getParameter("from")))));

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println(htmlPage.getHtml());
    }

    protected void doTasks(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
	    String q = request.getParameter("q");
	    if (q == null) {
	        q = "";
	    }
	    String postedMessage = request.getParameter("message");
	    String markDoneTask = request.getParameter("markDone");
	    String markNotDoneTask = request.getParameter("markNotDone");
	    String removeTask = request.getParameter("remove");

	    if (postedMessage != null && postedMessage.length() > 0) {
            Task task = new Task(postedMessage);

            tasksDAO.addTask(task);

            String datetime = request.getParameter("datetime");
            if (datetime != null && datetime.length() > 0) {
                Locale locale = request.getLocale();
                QueueHelper.getInstance().addTask(task, datetime, locale);
            }

            auditLog.log(user, task.getId(), "add");
        }

	    if (markDoneTask != null && !markDoneTask.isEmpty()) {
	        tasksDAO.markTaskDone(markDoneTask);
            auditLog.log(user, markDoneTask, "mark-done");
	    }

	    if (markNotDoneTask != null && !markNotDoneTask.isEmpty()) {
	        tasksDAO.markTaskNotDone(markNotDoneTask);
            auditLog.log(user, markNotDoneTask, "mark-not-done");
	    }

	    if (removeTask != null && !removeTask.isEmpty()) {
	        tasksDAO.delete(removeTask);
            auditLog.log(user, removeTask, "remove");
	    }

        HtmlPage htmlPage = new HtmlPage(request.getContextPath());
        htmlPage.addToBody(HtmlHelper.getTitle("ToDo LiSt - ViEw"));
        htmlPage.addToBody(getLogut(request));
	    htmlPage.addInputForm(q);
	    htmlPage.addToBody(getToDoList(tasksDAO, q));

	    response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println(htmlPage.getHtml());
	}

    private String getLogut(HttpServletRequest request) {
        return HtmlHelper.getLogout(redirect);
    }

    private String getToDoList(TasksDAO tasksDAO, String q) {
        List<Task> tasks = tasksDAO.getTaskList(q);
        return HtmlHelper.taskListAsHtml(tasks, q);
    }

}

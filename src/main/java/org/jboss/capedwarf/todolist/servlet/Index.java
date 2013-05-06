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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.capedwarf.todolist.dao.TasksDAO;
import org.jboss.capedwarf.todolist.domain.Task;
import org.jboss.capedwarf.todolist.html.HtmlHelper;
import org.jboss.capedwarf.todolist.html.HtmlPage;

/**
 * Servlet implementation class Index
 */
//@WebServlet({ "/index" })
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Index() {
        super();
    }

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    System.out.println("Requested from: " + request.getRemoteAddr());

        TasksDAO tasksDAO = new TasksDAO();

	    PrintWriter out = response.getWriter();

	    String q = request.getParameter("q");
	    if (q == null) {
	        q = "";
	    }
	    String postedMessage = request.getParameter("message");
	    String markDoneTask = request.getParameter("markDone");
	    String markNotDoneTask = request.getParameter("markNotDone");
	    String removeTask = request.getParameter("remove");

	    if (postedMessage != null && !postedMessage.equals("")) {
	        tasksDAO.addTask(new Task(postedMessage));
	    }

	    if (markDoneTask != null && !markDoneTask.isEmpty()) {
	        tasksDAO.markTaskDone(markDoneTask);
	    }

	    if (markNotDoneTask != null && !markNotDoneTask.isEmpty()) {
	        tasksDAO.markTaskNotDone(markNotDoneTask);
	    }

	    if (removeTask != null && !removeTask.isEmpty()) {
	        tasksDAO.delete(removeTask);
	    }

        HtmlPage htmlPage = new HtmlPage(request.getContextPath());
        htmlPage.addToBody(HtmlHelper.getTitle("ToDo LiSt"));
	    htmlPage.addInputForm(q);
	    htmlPage.addToBody(getToDoList(tasksDAO, q));

	    response.setContentType("text/html");
	    out.println(htmlPage.getHtml());
	}

    private String getToDoList(TasksDAO tasksDAO, String q) {
        List<Task> tasks = tasksDAO.getTaskList(q);
        return HtmlHelper.taskListAsHtml(tasks, q);
    }

}

package org.jboss.sample.todolist.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.sample.todolist.dao.TasksDAO;
import org.jboss.sample.todolist.domain.Task;
import org.jboss.sample.todolist.html.HtmlHelper;
import org.jboss.sample.todolist.html.HtmlPage;

/**
 * Servlet implementation class Index
 */
@WebServlet({ "/index" })
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Index() {
        super();
    }

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

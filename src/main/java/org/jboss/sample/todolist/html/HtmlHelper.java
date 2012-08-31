package org.jboss.sample.todolist.html;

import java.util.List;

import org.jboss.sample.todolist.domain.Task;

/**
 * @author Matej Lazar
 */
public class HtmlHelper {

    public static String taskListAsHtml(List<Task> tasks, String q) {
        String html = "\n";
        html += "<table class=\"table table-hover\">\n";

        for (Task task : tasks) {
            String onClickDone="onclick=\"document.location='?q=" + q + "&" + (task.isTaskDone() ? "markNotDone" : "markDone") + "=" + task.getId() + "'\"";
            String onClickRemove="onclick=\"document.location='?q=" + q + "&remove=" + task.getId() + "'\"";

            html += "<tr>\n";
            html += "<td>\n";
            html += "<label class=\"checkbox\">\n";
            html += "<input type=\"checkbox\"" + (task.isTaskDone() ? "checked=\"checked\"" : "");
            html += " " + onClickDone + "/>\n";
            html += "<span class=\"" + (task.isTaskDone() ? "taskDone" : "taskNotDone") + "\">" +  task.getMessage() + "</span>\n";
            html += "</label>\n";
            html += "</td>\n";
            html += "<td width=\"20px\">\n";
            html += "<i class=\"icon-remove\" " + onClickRemove + "/>\n";
            html += "</td>\n";
            html += "</tr>\n";
        }
        html += "</table>\n";
        html += "\n";
        return html;
    }

    public static String getTitle(String title) {
        String html = "\n";
        html += "<h1>" + title + "</h1>";

        return html;
    }

}

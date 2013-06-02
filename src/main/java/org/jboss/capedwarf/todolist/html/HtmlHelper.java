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

package org.jboss.capedwarf.todolist.html;

import java.util.List;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.jboss.capedwarf.todolist.domain.Task;

/**
 * @author Matej Lazar
 */
public class HtmlHelper {

    public static String taskListAsHtml(List<Task> tasks, String q) {
        String html = "\n";
        html += "<table class=\"table table-hover\">\n";

        for (Task task : tasks) {
            String onClickDone = "onclick=\"document.location='?q=" + q + "&" + (task.isTaskDone() ? "markNotDone" : "markDone") + "=" + task.getId() + "'\"";
            String onClickRemove = "onclick=\"document.location='?q=" + q + "&remove=" + task.getId() + "'\"";

            html += "<tr>\n";
            html += "<td>\n";
            html += "<label class=\"checkbox\">\n";
            html += "<input type=\"checkbox\"" + (task.isTaskDone() ? "checked=\"checked\"" : "");
            html += " " + onClickDone + "/>\n";
            html += "<span class=\"" + (task.isTaskDone() ? "taskDone" : "taskNotDone") + "\">" + task.getMessage() + "</span>\n";
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

    public static String getLogout(String redirect, String baseContext) {
        UserService userService = UserServiceFactory.getUserService();
        return "\n<a href=\"" + baseContext + userService.createLogoutURL(redirect) + "\" style=\"float:right\">Logout</a><p/>\n";
    }

    public static String auditLogsAsHtml(Object[][] logs) {
        String html = "\n";
        html += "<table class=\"table table-hover\">\n";

        html += "<tr><td>UserId</td><td>TaskId</td><td>Action</td></tr>\n";
        for (Object[] row : logs) {
            html += "<tr>\n";
            for (Object elt : row) {
                html += "<td>\n";
                html += String.valueOf(elt) + "\n";
                html += "</td>\n";
            }
            html += "</tr>\n";
        }
        html += "</table>\n";
        html += "\n";
        return html;
    }
}

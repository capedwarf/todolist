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

/**
 * @author Matej Lazar
 */
public class HtmlPage {

    private String body = "";
    private final String ctxPath;

    public HtmlPage(String ctxPath) {
        this.ctxPath = ctxPath;
    }

    public String getHtml() {
        return "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
            "<title>ToDo List</title>\n" +
            "<link href=\"" + ctxPath + "/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
            "<link href=\"" + ctxPath + "/css/style.css\" rel=\"stylesheet\">\n" +
            "</head>\n" +
            "<body>\n" +
            "<script src=\"" + ctxPath + "/js/bootstrap.min.js\"></script>\n" +
            "<div class=\"container\">\n" +
            body + "\n" +
            "</div>\n" +
            "</body>\n" +
            "</html>\n";
    }

    public void addToBody(String html) {
        this.body += html;
    }

    public void addInputForm(String q) {
        this.body += "<form method=\"GET\">\n" +
            "<label>New Task</label>\n" +
            "<div class=\"input-append\">\n" +
            "<input type=\"text\" name=\"message\" placeholder=\"Enter new task ...\"/>\n" +
            "<button type=\"submit\" class=\"btn\">Add</button>\n" +
            "<input type=\"text\" name=\"datetime\" placeholder=\"Pick time ... dd/MM/yy hh:mm\"/>\n" +
            "</div>\n" +
            "<div class=\"input-append\">\n" +
            "<label>Filter</label>\n" +
            "<input type=\"text\" name=\"q\" value=\"" + q + "\"/>\n" +
            "<button type=\"submit\" class=\"btn\">Apply</button>\n" +
            "<button type=\"submit\" class=\"btn\" onclick=\"this.form.q.value='';this.form.sumit();\">Remove</button>\n" +
            "</div>\n" +
            "</form>\n";
    }
}

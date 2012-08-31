package org.jboss.sample.todolist.html;

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
        String html="<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "<title>ToDo List</title>\n" +
                "<link href=\"" +  ctxPath + "/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                "<link href=\"" +  ctxPath + "/css/style.css\" rel=\"stylesheet\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<script src=\"" +  ctxPath + "/js/bootstrap.min.js\"></script>\n" +
                "<div class=\"container\">\n" +
                body + "\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n";
        return html;
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
        		"</div>\n" +
        		"<div class=\"input-append\">\n" +
        		"<label>Filter</label>\n" +
        		"<input type=\"text\" name=\"q\" value=\""+ q + "\"/>\n" +
        		"<button type=\"submit\" class=\"btn\">Apply</button>\n" +
        		"<button type=\"submit\" class=\"btn\" onclick=\"this.form.q.value='';this.form.sumit();\">Remove</button>\n" +
        		"</div>\n" +
        		"</form>\n";
    }
}

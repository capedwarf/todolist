package org.jboss.capedwarf.todolist.queue;

import java.io.IOException;

import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import org.jboss.capedwarf.todolist.dao.TasksDAO;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class MailTask implements DeferredTask {
    private String id;
    private String email;
    private String message;

    public MailTask(String id, String email, String message) {
        this.id = id;
        this.email = email;
        this.message = message;
    }

    @Override
    public void run() {
        final TasksDAO tasksDAO = new TasksDAO();
        if (tasksDAO.taskIsDone(id) == false) {
            tasksDAO.markTaskDone(id);

            MailService mailService = MailServiceFactory.getMailService();
            try {
                mailService.send(new MailService.Message("noreply@capedwarf.org", email, "ToDo LiSt - NoTiFiCaTiOn", message));
            } catch (IOException e) {
                System.err.println("Error sending mail: " + e.getMessage());
            }
        } else {
            System.out.println("Task [" + id + "] already marked as done or removed.");
        }
    }

    @Override
    public String toString() {
        return email + " --> " + message;
     }
}

package org.jboss.capedwarf.todolist.queue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.jboss.capedwarf.todolist.domain.Task;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class QueueHelper {
    private static QueueHelper INSTANCE;

    private static final Logger log = Logger.getLogger(QueueHelper.class.getName());

    private QueueHelper() {
    }

    public static synchronized QueueHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new QueueHelper();
        }
        return INSTANCE;
    }

    public void addTask(Task task, String datetime) {
        try {
            Date date = new SimpleDateFormat("dd/MM/yy hh:mm").parse(datetime);

            Queue queue = QueueFactory.getDefaultQueue();
            UserService userService = UserServiceFactory.getUserService();

            MailTask dt = new MailTask(task.getId(), userService.getCurrentUser().getEmail(), task.getMessage());
            TaskOptions options = TaskOptions.Builder.withPayload(dt).etaMillis(date.getTime());

            queue.add(options);

            log.info("Added mail task: " + dt + " @ " + date);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}

package org.jboss.capedwarf.todolist.log;

import com.google.appengine.api.users.User;
import org.jboss.capedwarf.todolist.domain.Task;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface AuditLog {
    void log(User user, String taskId, String action);
    Object[][] fetch(long from);
}

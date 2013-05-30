package org.jboss.capedwarf.todolist.log;

import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DsAuditLog implements AuditLog {
    private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    public void log(User user, String taskId, String action) {
        Entity entity = new Entity("AuditLog");
        entity.setProperty("userId", user.getUserId());
        entity.setProperty("taskId", taskId);
        entity.setProperty("action", action);
        entity.setProperty("ts", System.currentTimeMillis());
        ds.put(entity);
    }

    public Object[][] fetch(long from) {
        Query query = new Query("AuditLog")
            .addProjection(new PropertyProjection("userId", String.class))
            .addProjection(new PropertyProjection("taskId", String.class))
            .addProjection(new PropertyProjection("action", String.class))
            .setFilter(new Query.FilterPredicate("ts", Query.FilterOperator.GREATER_THAN_OR_EQUAL, from));
        PreparedQuery pq = ds.prepare(query);
        List<Entity> entities = pq.asList(FetchOptions.Builder.withDefaults());
        Object[][] results = new Object[entities.size()][3];
        for (int i = 0; i < entities.size(); i++) {
            results[i][0] = entities.get(i).getProperty("userId");
            results[i][1] = entities.get(i).getProperty("taskId");
            results[i][2] = entities.get(i).getProperty("action");
        }
        return results;
    }
}

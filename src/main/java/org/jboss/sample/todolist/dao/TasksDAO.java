package org.jboss.sample.todolist.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.sample.todolist.domain.Task;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.ScoredDocument;

/**
 * @author Matej Lazar
 */
public class TasksDAO extends AbstractDAO {

    private static final String ID = "id";
    private static final String TASK_DONE = "taskDone";
    private static final String MESSAGE = "message";

    public TasksDAO() {
        super();
    }

    public void addTask(Task task) {
        String id = task.getId();
        if (id == null) {
            id = generateId();
        }
        getTestIndex().add(newDocument(id,
                newField(ID).setText(id),
                newField(TASK_DONE).setText(task.isTaskDone() ? "true" : "false"),
                newField(MESSAGE).setText(task.getMessage())));
    }

    public List<Task> getTaskList(String q) {
        if (q != null && !q.equals("")) {
            return queryTasks(q);
        } else {
            return getAllTasks();
        }
    }

    public void markTaskDone(String id) {
        Task task = getTaskById(id);
        task.setTaskDone();
        addTask(task);
    }

    public void markTaskNotDone(String id) {
        Task task = getTaskById(id);
        task.setTaskNotDone();
        addTask(task);
    }

    public void delete(String id) {
        getTestIndex().remove(id);
    }

    private Task getTaskById(String id) {
        ScoredDocument doc = getTaskDoc(id);
        Task task = createTask(doc);
        return task;
    }

    private ScoredDocument getTaskDoc(String id) {
        Collection<ScoredDocument> docs = getTestIndex().search(ID + ":" + id).getResults();
        if (docs.size() != 1) {
            throw new RuntimeException("Invalid id specified.");
        }

        ScoredDocument doc = docs.iterator().next();
        return doc;
    }


    private List<Task> queryTasks(String q) {
        List<Task> result = new ArrayList<Task>();

        Collection<ScoredDocument> searchResult = getTestIndex().search(MESSAGE + ":" + q).getResults();

        for (ScoredDocument scoredDocument : searchResult) {
            result.add(createTask(scoredDocument));
        }
        return result;
    }

    private List<Task> getAllTasks() {
        List<Task> result = new ArrayList<Task>();
        List<Document> searchResult = getTestIndex().listDocuments(defaultListRequest()).getResults();;

        for (Document document : searchResult) {
            result.add(createTask(document));
        }
        return result;
    }

    private Task createTask(Document document) {
        String id = document.getOnlyField(ID).getText();
        String taskDone = document.getOnlyField(TASK_DONE).getText();
        String message = document.getOnlyField(MESSAGE).getText();
        Task task = new Task(id, message);
        task.setTaskDone(taskDone);
        return task;
    }




}

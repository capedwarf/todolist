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

package org.jboss.capedwarf.todolist.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.ScoredDocument;
import org.jboss.capedwarf.todolist.domain.Task;

/**
 * @author Matej Lazar
 */
public class TasksDAO extends AbstractDAO {

    private static final String ID = "id";
    private static final String USERNAME = "username";
    private static final String TASK_DONE = "taskDone";
    private static final String MESSAGE = "message";

    public TasksDAO() {
        super();
    }

    public void addTask(Task task) {
        String id = task.getId();
        if (id == null) {
            id = generateId();
            task.setId(id);
        }

        getIndex().put(newDocument(
            id,
            newField(ID).setText(id),
            newField(TASK_DONE).setText(task.isTaskDone() ? "true" : "false"),
            newField(MESSAGE).setText(task.getMessage()))
        );
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
        getIndex().delete(id);
    }

    public Task getTaskById(String id) {
        ScoredDocument doc = getTaskDoc(id);
        return createTask(doc);
    }

    public boolean taskExists(String id) {
        return getTaskDocs(id).isEmpty() == false;
    }

    public boolean taskIsDone(String id) {
        if (taskExists(id)) {
            Task task = getTaskById(id);
            return task.isTaskDone();
        } else {
            return true;
        }
    }

    private Collection<ScoredDocument> getTaskDocs(String id) {
        return getIndex().search(ID + ":" + id).getResults();
    }

    private ScoredDocument getTaskDoc(String id) {
        Collection<ScoredDocument> docs = getTaskDocs(id);
        if (docs.size() != 1) {
            throw new RuntimeException("Invalid id specified.");
        }
        return docs.iterator().next();
    }

    private List<Task> queryTasks(String q) {
        List<Task> result = new ArrayList<Task>();

        Collection<ScoredDocument> searchResult = getIndex().search(MESSAGE + ":" + q).getResults();

        for (ScoredDocument scoredDocument : searchResult) {
            result.add(createTask(scoredDocument));
        }
        return result;
    }

    private List<Task> getAllTasks() {
        List<Task> result = new ArrayList<Task>();
        List<Document> searchResult = getIndex().getRange(defaultListRequest()).getResults();

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

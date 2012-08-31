package org.jboss.sample.todolist.dao;

import com.google.appengine.api.search.Consistency;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.ListRequest;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceFactory;

/**
 * @author Matej Lazar
 */
public abstract class AbstractDAO {

    private SearchService service;

    public AbstractDAO() {
        service = SearchServiceFactory.getSearchService();
    }

    protected Field.Builder newField(String fieldName) {
        return Field.newBuilder().setName(fieldName);
    }

    protected Document newDocument(Field.Builder... fields) {
        return newDocument(null, fields);
    }

    protected Document newDocument(String id, Field.Builder... fields) {
        Document.Builder builder = Document.newBuilder();
        if (id != null) {
            builder.setId(id);
        }
        for (Field.Builder field : fields) {
            builder.addField(field);
        }
        return builder.build();
    }

    protected Index getTestIndex() {
        return getIndex("testIndex");
    }

    protected Index getIndex(String name) {
        return getIndex(name, Consistency.GLOBAL);
    }

    protected Index getIndex(String name, Consistency consistency) {
        IndexSpec indexSpec = getIndexSpec(name, consistency);
        return service.getIndex(indexSpec);
    }

    protected IndexSpec getIndexSpec(String name, Consistency consistency) {
        return IndexSpec.newBuilder().setName(name).setConsistency(consistency).build();
    }

    protected ListRequest defaultListRequest() {
        return ListRequest.newBuilder().build();
    }

    protected String generateId() {
        return System.currentTimeMillis() + "";
    }

}

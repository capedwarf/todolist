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

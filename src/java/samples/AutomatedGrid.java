/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * ZKTest - Free ZeroKode testing library.
 * Copyright (C) 2011 Telesoft Consulting GmbH http://www.telesoft-consulting.at
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; If not, see http://www.gnu.org/licenses/
 * 
 * Telesoft Consulting GmbH
 * Gumpendorferstraße 83-85
 * House 1, 1st Floor, Office No.1
 * 1060 Vienna, Austria
 * http://www.telesoft-consulting.at/
 */
package samples;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class AutomatedGrid extends Grid {

    public AutomatedGrid() {
        setModel(new ListModelList());
    }

    private static class ObjectRowRenderer implements RowRenderer {

        private final List<Method> _getters;

        public ObjectRowRenderer(List<Method> getters) {
            _getters = getters;
        }

        @Override
        public void render(Row row, Object data) throws Exception {
            for (Method method : _getters) {
                Object value = method.invoke(data, new Object[]{});
                Label lbl = new Label(value == null ? "" : value.toString());
                row.appendChild(lbl);
            }
        }
    }

    private static class ArrayRowRenderer implements RowRenderer {

        @Override
        public void render(Row row, Object data) throws Exception {
            for (Object value : (Object[]) data) {
                Label lbl = new Label(value == null ? "" : value.toString());
                row.appendChild(lbl);
            }
        }
    }

    private static class DefaultRowRenderer implements RowRenderer {

        @Override
        public void render(Row row, Object data) throws Exception {
            row.appendChild(new Label(data.toString()));
        }
    }

    private List<Method> resolveGetters(Class clazz) {
        ArrayList<Method> getters = new ArrayList<Method>();
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if ((method.getModifiers() & Modifier.PUBLIC) > 0 && method.getName().startsWith("get") && method.getParameterTypes().length == 0 && method.getReturnType() != void.class) {
                getters.add(method);
            }
        }
        return getters;
    }

    public void changeSource(List source) throws Exception {
        if (getColumns() != null) {
            getColumns().detach();
        }
        ((ListModelList) getModel()).clear();

        if (source.isEmpty()) {
            return;
        }

        RowRenderer renderer = null;
        List<Column> columns = new ArrayList<Column>();

        Object firstItem = source.get(0);
        if (firstItem instanceof Object[]) {
            columns = createColumns(((Object[]) firstItem).length);
            renderer = new ArrayRowRenderer();
        } else {
            List<Method> getters = resolveGetters(firstItem.getClass());
            if (getters.isEmpty()) {
                columns = createColumns(firstItem);
                renderer = new DefaultRowRenderer();
            } else {
                columns = createColumns(getters);
                renderer = new ObjectRowRenderer(getters);
            }
        }

        appendChild(new Columns());
        for (Column col : columns) {
            getColumns().appendChild(col);
        }
        setRowRenderer(renderer);
        ((ListModelList) getModel()).addAll(source);
    }

    private List<Column> createColumns(Object item) throws Exception {
        List<Column> ret = new ArrayList<Column>();
        Column col = new Column(item.getClass().getSimpleName());
        col.setSort("auto");
        ret.add(col);
        return ret;
    }

    private List<Column> createColumns(List<Method> getters) throws Exception {
        List<Column> ret = new ArrayList<Column>();
        for (Method getter : getters) {
            String name = getter.getName().substring(3);
            Column column = new Column(name);
            column.setSort("auto(" + getter.getName() + ")");
            ret.add(column);
        }
        return ret;
    }

    private List<Column> createColumns(int arrayLength) throws Exception {
        List<Column> ret = new ArrayList<Column>();
        for (int i = 0; i < arrayLength; i++) {
            String name = "position " + i;
            Column column = new Column(name);
            column.setSort("auto(" + i + ")");
            ret.add(column);
        }
        return ret;
    }
}

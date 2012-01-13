/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * Free ZeroKode testing example.
 * Copyright (C) 2011 Telesoft Consulting GmbH http://www.telesoft-consulting.at
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; If not, see http://www.gnu.org/licenses/
 * 
 * Telesoft Consulting GmbH
 * Gumpendorferstraße 83-85
 * House 1, 1st Floor, Office No.1
 * 1060 Vienna, Austria
 * http://www.telesoft-consulting.at/
 */
package accounts.database;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class Usergroup {

    private static final List<Usergroup> TABLE = new ArrayList<Usergroup>();

    static {
        restore();
    }

    public static void restore() {
        TABLE.clear();
        TABLE.add(new Usergroup("ADMIN", "Administrator"));
        TABLE.add(new Usergroup("USER", "User"));
        TABLE.add(new Usergroup("DEMOUSER", "Demo User"));
    }
    private String _id = null;
    private String _name = null;

    private Usergroup(String id, String name) {
        _id = id;
        _name = name;
    }

    public Usergroup() {
    }

    public static void deleteById(String id) {
        for (Usergroup item : TABLE) {
            if (item._id.equals(id)) {
                TABLE.remove(item);
                return;
            }
        }
    }

    public static Usergroup getById(String id) {
        for (Usergroup item : TABLE) {
            if (item._id.equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static List<Usergroup> findLike(String filter) {
        if (filter == null || filter.isEmpty()) {
            return findAll();
        }
        List<Usergroup> ret = new ArrayList<Usergroup>();
        for (Usergroup item : TABLE) {
            if (item._id.contains(filter) || item._name.contains(filter)) {
                ret.add(item);
            }
        }
        return ret;
    }

    public static List<Usergroup> findAll() {
        return new ArrayList<Usergroup>(TABLE);
    }

    /**
     * @return the _id
     */
    public String getId() {
        return _id;
    }

    /**
     * @param id the _id to set
     */
    public void setId(String id) {
        this._id = id;
    }

    /**
     * @return the _name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param name the _name to set
     */
    public void setName(String name) {
        this._name = name;
    }

    public void save() {
        TABLE.add(this);
    }

    public void delete() {
        deleteById(_id);
    }
}

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
public class User {

    private static final List<User> TABLE = new ArrayList<User>();

    static {
        restore();
    }

    public static void restore() {
        TABLE.clear();
        TABLE.add(new User("admin", "System Admin", "ADMIN"));
        TABLE.add(new User("me", "My Account", "ADMIN"));
        TABLE.add(new User("user1", "First user", "USER"));
        TABLE.add(new User("user2", "Second user", "USER"));
        TABLE.add(new User("user3", "Third user", "USER"));
        TABLE.add(new User("demo", "Demo user", "DEMOUSER"));

    }

    private User(String id, String name, String usergroup) {
        _id = id;
        _name = name;
        _usergroup = Usergroup.getById(usergroup);
    }

    public User() {
    }
    private String _id = null;
    private String _name = null;
    private Usergroup _usergroup = null;

    public static void deleteById(String id) {
        for (User item : TABLE) {
            if (item._id.equals(id)) {
                TABLE.remove(item);
                return;
            }
        }
    }

    public static User getById(String id) {
        for (User item : TABLE) {
            if (item._id.equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static List<User> findLike(String filter, Usergroup usergroup) {
        List<User> ret = new ArrayList<User>();
        for (User item : TABLE) {
            // Check filter if specified
            if (filter != null && !filter.isEmpty() && !item._id.contains(filter) && !item._name.contains(filter)) {
                continue;
            }
            // check usergroup if specified
            if (usergroup != null && !item.getUsergroup().getId().equals(usergroup.getId())) {
                continue;
            }
            ret.add(item);
        }
        return ret;
    }

    public static List<User> findAll() {
        return new ArrayList<User>(TABLE);
    }

    /**
     * @return the userID
     */
    public String getID() {
        return _id;
    }

    /**
     * @param userID the userID to set
     */
    public void setID(String id) {
        _id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this._name = name;
    }

    /**
     * @return the usergroup
     */
    public Usergroup getUsergroup() {
        return _usergroup;
    }

    /**
     * @param usergroup the usergroup to set
     */
    public void setUsergroup(Usergroup usergroup) {
        this._usergroup = usergroup;
    }

    public void save() {
        TABLE.add(this);
    }

    public void delete() {
        deleteById(_id);
    }
}

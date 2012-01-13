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
package accounts;

import accounts.database.User;
import accounts.database.Usergroup;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class UserEditor extends Window implements AfterCompose {

    private Textbox _id = null;
    private Textbox _name = null;
    private Combobox _usergroup = null;
    private boolean _editMode = false;

    public void init(Usergroup usergroup) {
        if (usergroup == null) {
            return;
        }
        _usergroup.setValue(usergroup.getId());
    }

    public void init(User user) {
        if (user == null) {
            return;
        }
        _editMode = true;
        _id.setValue(user.getID());
        _id.setDisabled(true);

        _name.setValue(user.getName());
        _usergroup.setValue(user.getUsergroup().getId());
    }

    public void close() {
        detach();
    }

    public void save() {
        if (_editMode) {
            User item = User.getById(_id.getValue());
            item.setName(_name.getValue());
            item.setUsergroup((Usergroup) _usergroup.getSelectedItem().getValue());
        } else {
            User item = new User();
            item.setID(_id.getValue());
            item.setName(_name.getValue());
            item.setUsergroup((Usergroup) _usergroup.getSelectedItem().getValue());
            item.save();
        }
        close();
    }

    @Override
    public void afterCompose() {
        _id = (Textbox) getFellow("_id");
        _name = (Textbox) getFellow("_name");
        _usergroup = (Combobox) getFellow("_usergroup");

        for (Usergroup usergroup : Usergroup.findAll()) {
            Comboitem item = new Comboitem(usergroup.getId());
            item.setValue(usergroup);
            _usergroup.appendChild(item);
        }
    }

    public static void show(Object initvalue) throws InterruptedException {
        UserEditor ret = (UserEditor) Executions.createComponents("/accounts/UserEditor.zul", null, null);
        if (initvalue instanceof User) {
            ret.init((User) initvalue);
        } else if (initvalue instanceof Usergroup) {
            ret.init((Usergroup) initvalue);
        }
        ret.doModal();
    }
}

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

import accounts.database.Usergroup;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class UsergroupEditor extends Window implements AfterCompose {

    private Textbox _id = null;
    private Textbox _name = null;
    private boolean _editMode = false;
    private Usergroup _changed = null;

    public void init(Usergroup usergroup) {
        if (usergroup == null) {
            return;
        }
        _editMode = true;
        _id.setValue(usergroup.getId());
        _id.setDisabled(true);

        _name.setValue(usergroup.getName());
    }

    public void save() {
        if (_editMode) {
            _changed = Usergroup.getById(_id.getValue());
            _changed.setName(_name.getValue());
        } else {
            _changed = new Usergroup();
            _changed.setId(_id.getValue());
            _changed.setName(_name.getValue());
            _changed.save();
        }
        close();
    }

    public void close() {
        detach();
    }

    public Usergroup getChanged() {
        return _changed;
    }

    @Override
    public void afterCompose() {
        _id = (Textbox) getFellow("_id");
        _name = (Textbox) getFellow("_name");
    }

    public static UsergroupEditor show(Usergroup initvalue) throws InterruptedException {
        UsergroupEditor ret = (UsergroupEditor) Executions.createComponents("/accounts/UsergroupEditor.zul", null, null);
        ret.init((Usergroup) initvalue);
        ret.doModal();
        return ret;
    }
}

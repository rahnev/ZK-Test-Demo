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
package accounts.emulators;

import accounts.Users;
import org.tsc.emulation.GuiEnvironment;
import org.tsc.emulation.components.Button;
import org.tsc.emulation.components.Grid;
import org.tsc.emulation.components.Label;
import org.tsc.emulation.components.Textbox;
import org.tsc.emulation.components.Tree;
import org.tsc.emulation.components.Window;
import org.tsc.tools.ExecutionEmulator;
import org.junit.Ignore;
import org.tsc.emulation.exceptions.EmulationException;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
@Ignore
public class UsersEmulator extends Window<Users> {

    private Button _edit = null;
    private Button _delete = null;
    private Button _add = null;
    private Button _btnFilter = null;
    private Button _addUser = null;
    private Textbox _filter = null;
    private Label _detail = null;
    private Tree _usergroupTree = null;
    private Grid _userGrid = null;

    public UsersEmulator(Users instance) throws Exception {
        super(instance);
    }

    public UsersEmulator() throws Exception {
        this((Users) ExecutionEmulator.createComponent("accounts/Users.zul", null, null));
    }

    public static UsersEmulator getIfCreated() throws Exception {
        Users instance = (Users) GuiEnvironment.getComponent(Users.class);
        if (instance == null) {
            return null;
        }
        return new UsersEmulator(instance);
    }

    public Button getEdit() throws EmulationException {
        if (_edit == null) {
            _edit = new Button(INSTANCE, "_edit");
        }
        return _edit;
    }

    public Button getDelete() throws EmulationException {
        if (_delete == null) {
            _delete = new Button(INSTANCE, "_delete");
        }
        return _delete;
    }

    public Button getAdd() throws EmulationException {
        if (_add == null) {
            _add = new Button(INSTANCE, "_add");
        }
        return _add;
    }

    public Button getBtnFilter() throws EmulationException {
        if (_btnFilter == null) {
            _btnFilter = new Button(INSTANCE, "_btnFilter");
        }
        return _btnFilter;
    }

    public Button getAddUser() throws EmulationException {
        if (_addUser == null) {
            _addUser = new Button(INSTANCE, "_addUser");
        }
        return _addUser;
    }

    public Textbox getFilter() throws EmulationException {
        if (_filter == null) {
            _filter = new Textbox(INSTANCE, "_filter");
        }
        return _filter;
    }

    public Label getDetail() throws EmulationException {
        if (_detail == null) {
            _detail = new Label(INSTANCE, "_detail");
        }
        return _detail;
    }

    public Tree getUsergroupTree() throws EmulationException {
        if (_usergroupTree == null) {
            _usergroupTree = new Tree(INSTANCE, "_usergroupTree");
        }
        return _usergroupTree;
    }

    public Grid getUserGrid() throws EmulationException {
        if (_userGrid == null) {
            _userGrid = new Grid(INSTANCE, "_userGrid");
        }
        return _userGrid;
    }
}

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

import accounts.UserEditor;
import org.tsc.emulation.GuiEnvironment;
import org.tsc.emulation.components.Button;
import org.tsc.emulation.components.Combobox;
import org.tsc.emulation.components.Textbox;
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
public class UserEditorEmulator extends Window<UserEditor> {

    private Textbox _id = null;
    private Textbox _name = null;
    private Combobox _usergroup = null;
    private Button _save = null;
    private Button _cancel = null;

    public UserEditorEmulator(UserEditor instance) throws Exception {
        super(instance);
    }

    public UserEditorEmulator() throws Exception {
        this((UserEditor) ExecutionEmulator.createComponent("accounts/UserEditor.zul", null, null));
    }

    public static UserEditorEmulator getIfCreated() throws Exception {
        UserEditor instance = (UserEditor) GuiEnvironment.getComponent(UserEditor.class);
        if (instance == null) {
            return null;
        }
        return new UserEditorEmulator(instance);
    }

    public Textbox getId() throws EmulationException {
        if (_id == null) {
            _id = new Textbox(INSTANCE, "_id");
        }
        return _id;
    }

    public Textbox getName() throws EmulationException {
        if (_name == null) {
            _name = new Textbox(INSTANCE, "_name");
        }
        return _name;
    }

    public Combobox getUsergroup() throws EmulationException {
        if (_usergroup == null) {
            _usergroup = new Combobox(INSTANCE, "_usergroup");
        }
        return _usergroup;
    }

    public Button getSave() throws EmulationException {
        if (_save == null) {
            _save = new Button(INSTANCE, "_save");
        }
        return _save;
    }

    public Button getCancel() throws EmulationException {
        if (_cancel == null) {
            _cancel = new Button(INSTANCE, "_cancel");
        }
        return _cancel;
    }
}

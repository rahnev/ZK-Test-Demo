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

import accounts.MainPage;
import org.tsc.emulation.GuiEnvironment;
import org.tsc.emulation.components.Menubar;
import org.tsc.emulation.components.Window;
import org.tsc.emulation.exceptions.EmulationException;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
@org.junit.Ignore
public class MainPageEmulator extends Window<MainPage> {

    private Menubar _menubar;

    public MainPageEmulator(MainPage instance) throws Exception {
        super(instance);
    }

    public static MainPageEmulator getIfCreated() throws Exception {
        MainPage instance = (MainPage) GuiEnvironment.getComponent(MainPage.class);
        if (instance == null) {
            return null;
        }
        return new MainPageEmulator(instance);
    }

    public Menubar getMenu() throws EmulationException {
        if (_menubar == null) {
            _menubar = new Menubar(INSTANCE, "_menu");
        }
        return _menubar;
    }
}

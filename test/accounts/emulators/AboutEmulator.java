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

import accounts.About;
import org.tsc.emulation.GuiEnvironment;
import org.tsc.emulation.components.Button;
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
public class AboutEmulator extends Window<About> {

    public AboutEmulator(About instance) throws Exception {
        super(instance);
    }

    public AboutEmulator() throws Exception {
        this((About) ExecutionEmulator.createComponent("accounts/About.zul", null, null));
    }

    public static AboutEmulator getIfCreated() throws Exception {
        About instance = (About) GuiEnvironment.getComponent(About.class);
        if (instance == null) {
            return null;
        }
        return new AboutEmulator(instance);
    }

    public Button getCloseButton() throws EmulationException {
        return new Button(INSTANCE, "_close");
    }

    public Button getDetailButton() throws EmulationException {
        return new Button(INSTANCE, "_detail");
    }
}

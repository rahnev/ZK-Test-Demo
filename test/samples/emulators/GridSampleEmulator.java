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
package samples.emulators;

import org.tsc.emulation.Client;
import org.tsc.emulation.components.Grid;
import org.tsc.emulation.components.Radiogroup;
import org.tsc.emulation.components.Window;
import org.tsc.emulation.exceptions.EmulationException;
import org.tsc.tools.ExecutionEmulator;
import samples.GridSample;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
@org.junit.Ignore
public class GridSampleEmulator extends Window<GridSample> {

    public GridSampleEmulator() throws EmulationException {
        this((GridSample) ExecutionEmulator.createComponent("/samples/GridSample.zul", null, null));
    }

    public GridSampleEmulator(GridSample instance) {
        super(instance);
    }

    public static GridSampleEmulator getIfCreated(Client client) {
        GridSample instance = (GridSample) client.getComponent(GridSample.class);
        if (instance == null) {
            return null;
        }
        return new GridSampleEmulator(instance);
    }

    public Grid getGrid() {
        return new Grid(INSTANCE, "grid");
    }

    public Radiogroup getRadio() {
        // window -> vbox -> hbox -> radiogroup
        return new Radiogroup((org.zkoss.zul.Radiogroup) INSTANCE.getFirstChild().getFirstChild().getFirstChild());
    }
}

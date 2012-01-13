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
package tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tsc.emulation.Client;
import org.tsc.emulation.GuiEnvironment;
import org.tsc.emulation.components.Textbox;
import org.tsc.emulation.components.Window;
import org.tsc.emulation.exceptions.EmulationException;
import org.tsc.tools.ComponentWrapperUtil;
import org.tsc.tools.FileUtil;
import samples.emulators.GridSampleEmulator;
import samples.emulators.MyTextboxEmulator;
import static org.junit.Assert.*;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class EmulatorUsageTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        GuiEnvironment.create("/demo/web", "index.html");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        GuiEnvironment.destroy();
    }

    @Before
    public void setUp() throws EmulationException {
    }

    @After
    public void tearDown() throws Exception {
        GuiEnvironment.restore();
    }

    @Test
    public void testWithEmulatorComponent() throws Exception {
        GridSampleEmulator instance = new GridSampleEmulator();

        assertEquals(FileUtil.getFileContent("./test_resources/emulator/emptyGrid.txt"), instance.getGrid().printString());

        instance.getRadio().checkByLabel("Array");

        assertEquals(FileUtil.getFileContent("./test_resources/emulator/array-1-20.txt"), instance.getGrid().printString());

        instance.getRadio().checkByLabel("Integer");
        assertEquals(FileUtil.getFileContent("./test_resources/emulator/integer-1-20.txt"), instance.getGrid().printString());

        instance.getRadio().checkByLabel("UUID");
        assertEquals(FileUtil.getFileContent("./test_resources/emulator/uuid-1-20.txt"), instance.getGrid().printString());
    }

    @Test
    public void testWithoutControlEmulator() throws EmulationException {
        Client client = new Client();
        client.create("/samples/SendRequest.zul");

        Window<org.zkoss.zul.Window> emulator = new Window((org.zkoss.zul.Window) client.getMainControl());

        ((Textbox) emulator.getFellow("textbox")).setValue("test");
        assertEquals("test", emulator.INSTANCE.getTitle());
    }

    @Test
    public void testCustomComponentEmulator() throws EmulationException {
        Client client = new Client();
        client.create("/samples/SendRequest.zul");

        ComponentWrapperUtil.registerCustomWrapper(org.zkoss.zul.Textbox.class, MyTextboxEmulator.class);
        try {

            Window<org.zkoss.zul.Window> emulator = (Window) ComponentWrapperUtil.wrapComponent(client.getMainControl());

            ((MyTextboxEmulator) emulator.getFellow("textbox")).setValue("test");
            assertEquals("Simple Window", (emulator.INSTANCE).getTitle());
        } finally {
            ComponentWrapperUtil.unregisterCustomWrapper(org.zkoss.zul.Textbox.class);
        }
    }
}

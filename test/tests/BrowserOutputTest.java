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

import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tsc.emulation.Client;
import org.tsc.emulation.GuiEnvironment;
import org.tsc.emulation.exceptions.EmulationException;
import org.tsc.emulation.servlet.TestResponse;
import org.tsc.tools.EventExecution;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import static org.junit.Assert.*;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class BrowserOutputTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        GuiEnvironment.create("/demo/web");
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
    public void testLayoutServletOutput() throws ServletException, IOException, EmulationException {
        Client client = new Client();
        TestResponse response = (TestResponse) client.create("/samples/SendRequest.zul");

        // get the string response ot the servlet
        String result = response.getWriterString();
        assertTrue(result.contains("['zul.inp.Textbox','"));
    }

    @Test
    public void testUpdateServletOutput() throws EmulationException {
        Client client = new Client();
        client.create("/samples/SendRequest.zul");
        Window window = (Window) client.getMainControl();
        Textbox textbox = (Textbox) window.getFellow("textbox");
        TestResponse response = (TestResponse) EventExecution.executeChange(textbox, "test1");
        assertEquals("{\"rs\":[[\"setAttr\",[\"" + window.getUuid() + "\",\"title\",\"test1\"]]],\"rid\":3}", response.getWriterString());
    }
}

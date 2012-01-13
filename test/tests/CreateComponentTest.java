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
import org.tsc.emulation.exceptions.EmulationException;
import org.tsc.tools.ExecutionEmulator;
import org.tsc.emulation.servlet.TestUpdateServlet;
import org.tsc.tools.EventExecution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;
import static org.junit.Assert.*;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class CreateComponentTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        GuiEnvironment.create("/demo/web", "index.html");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        GuiEnvironment.destroy();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        GuiEnvironment.restore();
    }

    @Test
    public void testCreateComponentWithExecutionEmulator() throws EmulationException {
        // Create SendRequest with ExecutionEmulator
        Window instance = (Window) ExecutionEmulator.createComponent("samples/SendRequest.zul", null, null);

        EventExecution.executeChange(instance.getFellow("textbox"), "test1");
        assertEquals("test1", instance.getTitle());
    }

    @Test
    public void testCreateComponentWithExecutions() throws EmulationException {
        Client activeClient = TestUpdateServlet.getActiveClient();

        // enter the event listener mode
        TestUpdateServlet.enter(GuiEnvironment.getDefaultClient());

        // create SendRequest with Executions
        Window instance = (Window) Executions.createComponents("samples/SendRequest.zul", null, null);

        // exit event listener
        TestUpdateServlet.free();
        if (activeClient != null) {
            TestUpdateServlet.enter(activeClient);
        }

        EventExecution.executeChange(instance.getFellow("textbox"), "test1");
        assertEquals("test1", instance.getTitle());
    }
}

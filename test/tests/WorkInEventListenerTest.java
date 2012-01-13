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

import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tsc.emulation.Client;
import org.tsc.emulation.GuiEnvironment;
import org.tsc.emulation.componentutils.GridUtil;
import org.tsc.emulation.exceptions.EmulationException;
import org.tsc.emulation.servlet.TestUpdateServlet;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import samples.AutomatedGrid;
import static org.junit.Assert.*;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class WorkInEventListenerTest {

    Client _client = null;
    Textbox _textbox = null;
    Window _mainControl = null;

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
        _client = new Client();
        _client.create("/samples/SendRequest.zul");
        _mainControl = (Window) _client.getMainControl();
        _textbox = (Textbox) _mainControl.getFellow("textbox");
    }

    @After
    public void tearDown() throws Exception {
        GuiEnvironment.restore();
    }

    @Test(expected = java.lang.IllegalStateException.class)
    public void testSmartUpdateOutOfEventListener() throws EmulationException {
        // smart update cause IllegalStateException
        _textbox.setValue("test1");
    }

    @Test
    public void testSmartUpdateInEventListener() throws EmulationException {
        // activate the event listener
        TestUpdateServlet.enter(_client);
        // now there no exeption
        _textbox.setValue("test2");
        //leave the event listener mode for this thread
        TestUpdateServlet.free();
    }

    @Test
    public void testPendingEventsInExecutionQueue() throws EmulationException, Exception {
        TestUpdateServlet.enter(_client);// enter the event listener

        // Create the instance of the object
        AutomatedGrid instance = new AutomatedGrid();
        instance.setPage(_client.getPage());
        _client.ping(); // finalize execution of the event listener and get back

        assertEquals("", GridUtil.printString(instance, true));
        List<Integer> list = Arrays.asList(new Integer[]{2, 5, 9, 0});
        instance.changeSource(list);// Build the header, set the row renderer and update the model

        // the grid rows are created but the renderer is not called because 
        // the events wait in the Execution queue
        assertEquals("Integer", GridUtil.printString(instance, true));

        // finalize the event listener execution and enter it again
        TestUpdateServlet.enter(_client);

        // now the grid is loaded and all items are rendered
        assertEquals("Integer\n2\n5\n9\n0", GridUtil.printString(instance, true));
    }
}

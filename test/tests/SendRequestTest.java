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
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tsc.emulation.Client;
import org.tsc.emulation.GuiEnvironment;
import org.tsc.emulation.exceptions.EmulationException;
import org.tsc.emulation.servlet.TestRequest;
import org.tsc.emulation.servlet.TestResponse;
import org.tsc.emulation.servlet.TestUpdateServlet;
import org.tsc.tools.EventExecution;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import static org.junit.Assert.*;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class SendRequestTest {

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

    @Test
    public void testSendRequestViaUpdateServlet() throws ServletException, IOException {

        // prepare request parameters
        String desktopId = _client.getDesktop().getId();
        String componentId = _textbox.getUuid();
        String command = Events.ON_CHANGE;
        // create the request parameter values
        Map values = new HashMap();
        values.put("value", "test1");

        // create the request and responce
        TestRequest request = new TestRequest(_client, desktopId, componentId, command, values);
        TestResponse response = new TestResponse(_client);

        // check window title before request
        assertEquals("Simple Window", _mainControl.getTitle());

        // send request 
        TestUpdateServlet.get(request, response);

        // check window title after request
        assertEquals("test1", _mainControl.getTitle());
    }

    @Test
    public void testSendRequestViaClient() throws EmulationException {

        // prepare request parameter values
        Map values = new HashMap();
        values.put("value", "test2");

        // check window title before request
        assertEquals("Simple Window", _mainControl.getTitle());
        // send request 
        _client.executeCommand(_textbox, Events.ON_CHANGE, values);
        // check window title after request
        assertEquals("test2", _mainControl.getTitle());
    }

    @Test
    public void testSendRequestViaEventExecution() throws EmulationException {
        // check window title before request
        assertEquals("Simple Window", _mainControl.getTitle());
        // send request 
        EventExecution.executeChange(_textbox, "test3");
        // check window title after request
        assertEquals("test3", _mainControl.getTitle());
    }

    @Test
    public void testSendRequestViaWrapper() throws EmulationException {
        // create the textbox emulator
        org.tsc.emulation.components.Textbox emulator = new org.tsc.emulation.components.Textbox(_textbox);
        // alternative create method
        org.tsc.emulation.components.Textbox alternativeEmulator = new org.tsc.emulation.components.Textbox(_mainControl, "textbox");

        // check window title before request
        assertEquals("Simple Window", _mainControl.getTitle());
        // send request
        emulator.setValue("test4");
        // check window title after request
        assertEquals("test4", _mainControl.getTitle());

        // send request over the alternative emulator
        alternativeEmulator.setValue("test5");
        // check window title after request
        assertEquals("test5", _mainControl.getTitle());
    }
}

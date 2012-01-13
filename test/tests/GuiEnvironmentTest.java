package tests;

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
import org.zkoss.zhtml.Text;
import org.zkoss.zhtml.A;
import org.zkoss.zhtml.Html;
import org.tsc.emulation.servlet.TestLayoutServlet;
import org.tsc.emulation.servlet.TestUpdateServlet;
import javax.servlet.ServletException;
import org.junit.Test;
import org.tsc.emulation.Client;
import org.tsc.emulation.GuiEnvironment;
import org.tsc.emulation.exceptions.EmulationException;
import org.tsc.tools.ReflectionUtil;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.http.DHtmlLayoutServlet;
import static org.junit.Assert.*;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class GuiEnvironmentTest {

    @Test
    public void testCreateAndDestroy() throws ServletException, EmulationException {
        // Check if no instance of GuiEnvironment and Servlets available
        assertNull(GuiEnvironment.WEB_PATH);
        assertNull(ReflectionUtil.getStaticField(GuiEnvironment.class, "SINGLETONE"));
        assertNull(ReflectionUtil.getStaticField(TestLayoutServlet.class, "SINGLETONE"));
        assertNull(ReflectionUtil.getStaticField(TestUpdateServlet.class, "SINGLETONE"));

        // Create the GuiEnvironment
        GuiEnvironment.create("/demo/web");

        // Check if the GuiEnvironment and both Servlets are created
        assertEquals("/demo/web", GuiEnvironment.WEB_PATH);
        assertNotNull(ReflectionUtil.getStaticField(GuiEnvironment.class, "SINGLETONE"));
        DHtmlLayoutServlet layoutServlet = (DHtmlLayoutServlet) ReflectionUtil.getStaticField(TestLayoutServlet.class, "SINGLETONE");
        DHtmlUpdateServlet updateServlet = (DHtmlUpdateServlet) ReflectionUtil.getStaticField(TestUpdateServlet.class, "SINGLETONE");
        assertNotNull(layoutServlet);
        assertNotNull(updateServlet);
        // check if servlets initialized (both have module manager in servlet context)
        assertNotNull(layoutServlet.getServletConfig().getServletContext().getAttribute("javax.zkoss.zk.ui.WebManager"));
        assertNotNull(updateServlet.getServletConfig().getServletContext().getAttribute("javax.zkoss.zk.ui.WebManager"));


    }

    @Test
    public void testGuiEnvironmentDestroy() throws ServletException, EmulationException {
        // Create the GuiEnvironment
        GuiEnvironment.create("/demo/web");

        // Create the cleint emulator
        Client client = new Client();
        client.create("index.html");

        // check if the client desktop active
        assertEquals(true, client.getDesktop().isAlive());
        // check if the client registered in GuiEnvironment
        assertNotNull(GuiEnvironment.getClient(client.getDesktop()));

        // Destory the GuiEnvironment and Servlets 
        GuiEnvironment.destroy();

        // check if the client desktop destroyed
        assertEquals(false, client.getDesktop().isAlive());

        // Check if no instance of GuiEnvironment and Servlets available
        assertNull(GuiEnvironment.WEB_PATH);
        assertNull(ReflectionUtil.getStaticField(GuiEnvironment.class, "SINGLETONE"));
        assertNull(ReflectionUtil.getStaticField(TestLayoutServlet.class, "SINGLETONE"));
        assertNull(ReflectionUtil.getStaticField(TestUpdateServlet.class, "SINGLETONE"));

    }

    @Test
    public void testGuiEnvironmentRestore() throws ServletException, EmulationException {
        // Create the GuiEnvironment
        GuiEnvironment.create("/demo/web", "index.html");

        // Create the cleint emulator
        Client client = new Client();
        client.create("index.html");

        Desktop defaultClientDesktop = GuiEnvironment.getDefaultClient().getDesktop();
        Desktop otherClientDesktop = client.getDesktop();


        // restore the environment
        GuiEnvironment.restore();

        // check if the default client registered
        assertNotNull(GuiEnvironment.getClient(defaultClientDesktop));
        // check if the client unregistered
        assertNull(GuiEnvironment.getClient(otherClientDesktop));

        // check if the default client desktop alive
        assertEquals(true, defaultClientDesktop.isAlive());
        // check if the client desktop destroyed
        assertEquals(false, otherClientDesktop.isAlive());
    }

    @Test
    public void testClientCreateAndDestory() throws ServletException, EmulationException {
        // Create the GuiEnvironment
        GuiEnvironment.create("/demo/web", "index.html");

        try {
            // Create the cleint emulator
            Client client = new Client();
            client.create("index.html");

            // check if the client desktop created
            assertEquals(true, client.getDesktop().isAlive());
            // check if the client registered in GuiEnvironment
            assertSame(client, GuiEnvironment.getClient(client.getDesktop()));


            // destory the client
            client.exit();
            // check if the client unregistered
            assertNull(GuiEnvironment.getClient(client.getDesktop()));
            // check if the client desktop destroyed
            assertEquals(false, client.getDesktop().isAlive());

        } finally {
            GuiEnvironment.destroy();
        }
    }

    @Test
    public void testMainControlAccess() throws EmulationException, ServletException {
        // create the test environment
        GuiEnvironment.create("/demo/web");

        // create client with request to the index.html
        Client client = new Client();
        client.create("index.html");

        // get access to the ZK presentation of the index.html page
        Html index = (Html) client.getMainControl();

        // test the index.html structure
        assertNotNull(index.getFellow("accounts"));
        assertNotNull(index.getFellow("chat"));
        A linkAccounts = (A) index.getFellow("accounts");
        A linkChat = (A) index.getFellow("chat");

        assertEquals(1, linkAccounts.getChildren().size());
        assertEquals("Account Area", ((Text) linkAccounts.getChildren().get(0)).getValue());

        assertEquals(1, linkChat.getChildren().size());
        assertEquals("Chat", ((Text) linkChat.getChildren().get(0)).getValue());
    }
}

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
package accounts;

import accounts.emulators.AboutEmulator;
import accounts.emulators.MainPageEmulator;
import org.tsc.emulation.GuiEnvironment;
import org.tsc.emulation.components.MessageBoxEmulator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;
import static org.junit.Assert.*;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class AboutTest {

    public AboutTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        GuiEnvironment.create("/demo/web", "accounts/MainPage.zul");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        GuiEnvironment.destroy();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() throws Exception {
        GuiEnvironment.restore();
    }

    /**
     * Test of close method, of class About.
     */
    @Test
    public void testCreated() throws Exception {
        AboutEmulator instance = new AboutEmulator();
        assertNotNull(instance);
        assertEquals(false, instance.isClosed());
        instance.getCloseButton().click();
        assertEquals(true, instance.isClosed());

    }

    @Test
    public void testAccessViaMainPage() throws Exception {
        MainPageEmulator mainPage = MainPageEmulator.getIfCreated();
        assertNotNull(mainPage);
        mainPage.getMenu().getMenuitem("Tools", "About").click();

        AboutEmulator instance = AboutEmulator.getIfCreated();
        assertNotNull(instance);

        // check title
        assertEquals("About", instance.INSTANCE.getTitle());

        // check label
        Vbox vbox = (Vbox) instance.INSTANCE.getChildren().get(0);
        Label lbl = (Label) vbox.getChildren().get(0);
        assertEquals("ZK JUnit test Demo", lbl.getValue());

        // test close via button
        assertEquals(false, instance.isClosed());
        instance.getCloseButton().click();
        assertEquals(true, instance.isClosed());

        // create new instance
        mainPage.getMenu().getMenuitem("Tools", "About").click();
        instance = AboutEmulator.getIfCreated();

        // test close via X
        assertEquals(false, instance.isClosed());
        instance.close();
        assertEquals(true, instance.isClosed());
    }

    @Test
    public void testDetail() throws Exception {
        AboutEmulator instance = new AboutEmulator();

        instance.getDetailButton().click();

        MessageBoxEmulator msgbox = MessageBoxEmulator.get();
        assertEquals("Details", msgbox.getTitle());
        assertEquals("contact details", msgbox.getMessage());
        msgbox.getOK().click();
        assertEquals(true, msgbox.isClosed());

        instance.close();
    }
}

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

import org.tsc.emulation.GuiEnvironment;
import accounts.emulators.UsergroupEditorEmulator;
import accounts.database.User;
import accounts.database.Usergroup;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tsc.emulation.servlet.TestUpdateServlet;
import static org.junit.Assert.*;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class UsergroupEditorTest {

    public UsergroupEditorTest() {
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
        User.restore();
        Usergroup.restore();
    }

    @Test
    public void testDefaultState() throws Exception {
        UsergroupEditorEmulator instance = new UsergroupEditorEmulator();
        assertEquals("", instance.getId().getRawText());
        assertEquals("", instance.getName().getRawValue());

        assertEquals(true, instance.getId().isEditable());
        assertEquals(true, instance.getName().isEditable());

        assertEquals(false, instance.getSave().isDisabled());
        assertEquals(false, instance.getCancel().isDisabled());
    }

    @Test
    public void testConstraints() throws Exception {
        UsergroupEditorEmulator instance = new UsergroupEditorEmulator();
        assertEquals(false, instance.isClosed());
        instance.getSave().click();
        assertEquals(false, instance.isClosed()); // not closed. No empty constraint on id and name

        instance.getId().setValue("TEST");
        instance.getSave().click();
        assertEquals(false, instance.isClosed()); // not closed. No empty constraint on name

        instance.getId().setValue("");
        instance.getName().setValue("test name");
        instance.getSave().click();
        assertEquals(false, instance.isClosed()); // not closed. No empty constraint on name

        instance.getId().setValue("TEST");
        instance.getSave().click();
        assertEquals(true, instance.isClosed()); // item saved. window closed

        assertNotNull(Usergroup.getById("TEST"));
    }

    @Test
    public void testEditMode() throws Exception {
        UsergroupEditorEmulator instance = new UsergroupEditorEmulator();

        TestUpdateServlet.enter(GuiEnvironment.getDefaultClient());
        instance.INSTANCE.init(Usergroup.getById("USER"));
        TestUpdateServlet.leave(GuiEnvironment.getDefaultClient());

        assertEquals("USER", instance.getId().getValue());
        assertEquals("User", instance.getName().getValue());

        assertEquals(false, instance.getId().isEditable());
        assertEquals(true, instance.getName().isEditable());

        instance.getName().setValue("User test");
        assertEquals("User", Usergroup.getById("USER").getName());
        instance.getSave().click();
        assertEquals("User test", Usergroup.getById("USER").getName());
        assertEquals(true, instance.isClosed());
    }
}

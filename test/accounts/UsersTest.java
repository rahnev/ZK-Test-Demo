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

import accounts.emulators.UserEditorEmulator;
import accounts.emulators.UsergroupEditorEmulator;
import accounts.emulators.MainPageEmulator;
import accounts.emulators.UsersEmulator;
import accounts.database.User;
import accounts.database.Usergroup;
import org.tsc.emulation.GuiEnvironment;
import org.tsc.emulation.components.MessageBoxEmulator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class UsersTest {

    public UsersTest() {
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
    public void testInitialLoad() throws Exception {
        UsersEmulator instance = new UsersEmulator();
        assertEquals(false, instance.getAdd().isDisabled());
        assertEquals(false, instance.getAddUser().isDisabled());
        assertEquals(true, instance.getDelete().isDisabled());
        assertEquals(true, instance.getEdit().isDisabled());
        assertEquals(false, instance.getBtnFilter().isDisabled());

        assertEquals("", instance.getFilter().getValue());
        String detailString = "All User Groups";
        String treeString = ""
                + "Usergroups\n"
                + "    Administrator\n"
                + "    User\n"
                + "    Demo User\n";

        String gridString = ""
                + "ID,Name,Usergroup,\n"
                + "admin,System Admin,ADMIN (Administrator),\n"
                + "me,My Account,ADMIN (Administrator),\n"
                + "user1,First user,USER (User),\n"
                + "user2,Second user,USER (User),\n"
                + "user3,Third user,USER (User),\n"
                + "demo,Demo user,DEMOUSER (Demo User),";

        assertEquals(detailString, instance.getDetail().getValue());
        assertEquals(gridString, instance.getUserGrid().printString());
        assertEquals(treeString, instance.getUsergroupTree().printString());
    }

    @Test
    public void testChangeTreeSelection() throws Exception {
        UsersEmulator instance = new UsersEmulator();
        instance.getUsergroupTree().getItemAtPath("Usergroups", "User").select();
        String treeString = ""
                + "Usergroups\n"
                + "    Administrator\n"
                + "   *User\n"
                + "    Demo User\n";

        String gridString = ""
                + "ID,Name,\n"
                + "user1,First user,\n"
                + "user2,Second user,\n"
                + "user3,Third user,";

        assertEquals(false, instance.getDelete().isDisabled());
        assertEquals(false, instance.getEdit().isDisabled());
        assertEquals("Usergoup : USER (User)", instance.getDetail().getValue());
        assertEquals(treeString, instance.getUsergroupTree().printString());
        assertEquals(gridString, instance.getUserGrid().printString());

        instance.getUsergroupTree().getItemAtPath("Usergroups", "Administrator").select();
        treeString = ""
                + "Usergroups\n"
                + "   *Administrator\n"
                + "    User\n"
                + "    Demo User\n";
        gridString = ""
                + "ID,Name,\n"
                + "admin,System Admin,\n"
                + "me,My Account,";
        assertEquals(true, instance.getDelete().isDisabled());//can't delete administrator group
        assertEquals(false, instance.getEdit().isDisabled());
        assertEquals("Usergoup : ADMIN (Administrator)", instance.getDetail().getValue());
        assertEquals(treeString, instance.getUsergroupTree().printString());
        assertEquals(gridString, instance.getUserGrid().printString());
    }

    @Test
    public void testAddUserGroup() throws Exception {
        MainPageEmulator mainPage = MainPageEmulator.getIfCreated();
        mainPage.getMenu().getMenuitem("Config", "Users").click();
        UsersEmulator instance = UsersEmulator.getIfCreated();
        assertNotNull(instance);

        instance.getAdd().click();

        UsergroupEditorEmulator editor = UsergroupEditorEmulator.getIfCreated();
        editor.getId().setValue("TEST");
        editor.getName().setValue("TEST NAME");
        editor.getSave().click();
        assertEquals(true, editor.isClosed()); // all constrains are ok
        assertNotNull(Usergroup.getById("TEST"));


        String treeString = ""
                + "Usergroups\n"
                + "    Administrator\n"
                + "    User\n"
                + "    Demo User\n"
                + "   *TEST NAME\n";

        String gridString = ""
                + "ID,Name,";

        assertEquals(false, instance.getDelete().isDisabled());
        assertEquals(false, instance.getEdit().isDisabled());
        assertEquals("Usergoup : TEST (TEST NAME)", instance.getDetail().getValue());
        assertEquals(treeString, instance.getUsergroupTree().printString());
        assertEquals(gridString, instance.getUserGrid().printString());
    }

    @Test
    public void testEditUser() throws Exception {
        UsersEmulator instance = new UsersEmulator();
        instance.getUserGrid().getRow(3).getButton("edit").click();

        UserEditorEmulator editor = UserEditorEmulator.getIfCreated();

        assertEquals("user2", editor.getId().getValue());
        assertEquals(false, editor.getId().isEditable());

        editor.getName().setValue("Changed Name");
        editor.getSave().click();
        assertEquals(true, editor.isClosed()); // constraints ok

        String treeString = ""
                + "Usergroups\n"
                + "    Administrator\n"
                + "    User\n"
                + "    Demo User\n";

        String gridString = ""
                + "ID,Name,Usergroup,\n"
                + "admin,System Admin,ADMIN (Administrator),\n"
                + "me,My Account,ADMIN (Administrator),\n"
                + "user1,First user,USER (User),\n"
                + "user2,Changed Name,USER (User),\n" // THE CHANGE
                + "user3,Third user,USER (User),\n"
                + "demo,Demo user,DEMOUSER (Demo User),";

        assertEquals(gridString, instance.getUserGrid().printString());
        assertEquals(treeString, instance.getUsergroupTree().printString());
    }

    @Test
    public void testDeleteUser() throws Exception {
        UsersEmulator instance = new UsersEmulator();
        instance.getUserGrid().getRow(3).getButton("delete").click();

        MessageBoxEmulator dialog = MessageBoxEmulator.get();
        assertEquals("Are you shure to delete user user2?", dialog.getMessage());
        // no delete
        dialog.getNO().click();


        String treeString = ""
                + "Usergroups\n"
                + "    Administrator\n"
                + "    User\n"
                + "    Demo User\n";

        String gridString = ""
                + "ID,Name,Usergroup,\n"
                + "admin,System Admin,ADMIN (Administrator),\n"
                + "me,My Account,ADMIN (Administrator),\n"
                + "user1,First user,USER (User),\n"
                + "user2,Second user,USER (User),\n" // not deleted
                + "user3,Third user,USER (User),\n"
                + "demo,Demo user,DEMOUSER (Demo User),";

        assertEquals(gridString, instance.getUserGrid().printString());
        assertEquals(treeString, instance.getUsergroupTree().printString());


        // delete this time
        instance.getUserGrid().getRow(3).getButton("delete").click();
        dialog = MessageBoxEmulator.get();
        assertEquals("Are you shure to delete user user2?", dialog.getMessage());
        dialog.getYES().click();


        treeString = ""
                + "Usergroups\n"
                + "    Administrator\n"
                + "    User\n"
                + "    Demo User\n";

        gridString = ""
                + "ID,Name,Usergroup,\n"
                + "admin,System Admin,ADMIN (Administrator),\n"
                + "me,My Account,ADMIN (Administrator),\n"
                + "user1,First user,USER (User),\n"
                //+ "user2,Second user,USER (User),\n" // deleted
                + "user3,Third user,USER (User),\n"
                + "demo,Demo user,DEMOUSER (Demo User),";

        assertEquals(gridString, instance.getUserGrid().printString());
        assertEquals(treeString, instance.getUsergroupTree().printString());
    }

    @Test
    public void testAddUser() throws Exception {
        UsersEmulator instance = new UsersEmulator();
        instance.getAddUser().click();

        UserEditorEmulator editor = UserEditorEmulator.getIfCreated();

        editor.getId().setValue("newDemo");
        editor.getName().setValue("New Demo User");
        editor.getUsergroup().setValue("DEMOUSER");
        editor.getSave().click();
        assertEquals(true, editor.isClosed());


        String gridString = ""
                + "ID,Name,Usergroup,\n"
                + "admin,System Admin,ADMIN (Administrator),\n"
                + "me,My Account,ADMIN (Administrator),\n"
                + "user1,First user,USER (User),\n"
                + "user2,Second user,USER (User),\n"
                + "user3,Third user,USER (User),\n"
                + "demo,Demo user,DEMOUSER (Demo User),\n"
                + "newDemo,New Demo User,DEMOUSER (Demo User),"; // The new item

        assertEquals(gridString, instance.getUserGrid().printString());
    }
}

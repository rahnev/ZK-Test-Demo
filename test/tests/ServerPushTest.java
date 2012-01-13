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
import org.tsc.emulation.GuiEnvironment;
import org.tsc.emulation.Client;
import org.zkoss.zul.Window;
import tw.com.cruisy.chat.ChatEmulator;
import tw.com.cruisy.chat.ParalelChatter;
import static org.junit.Assert.*;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class ServerPushTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        GuiEnvironment.create("/demo/web", "chat/chat.zul");
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
    public void testSingleThreadExternalBroadcast() throws Exception {
        ChatEmulator instance = new ChatEmulator((Window) GuiEnvironment.getMainControl());
        assertTrue(instance.loginVisible());

        instance.login("me");
        instance.waitForLogin("me");
        assertFalse(instance.loginVisible());
        assertEquals("~~~Welcome me~~~\n", instance.getText());

        instance.getChatRoom().broadcast("none", "hello");
        instance.waitToBroadcastAllMessages();
        assertEquals("~~~Welcome me~~~\nhello\n", instance.getText());

        instance.logout();
        instance.waitForLogout("me");
        assertTrue(instance.loginVisible());
        assertEquals("", instance.getText());
    }

    @Test
    public void testParalelWithSingleThread() throws Exception {
        Client mainClient = GuiEnvironment.getDefaultClient();
        Client otherClient = new Client();
        otherClient.create("/chat/chat.zul");
        ChatEmulator main = new ChatEmulator((Window) mainClient.getMainControl());
        ChatEmulator other = new ChatEmulator((Window) otherClient.getMainControl());

        main.login("main");
        assertEquals("~~~Welcome main~~~\n", main.getText());

        // second thread login
        other.login("other");
        assertEquals("~~~Welcome main~~~\n~~~other has joined this chatroom~~~\n", main.getText());
        assertEquals("~~~Welcome other~~~\n", other.getText());

        // main chatter send message
        main.sendMessage("hello other");
        assertEquals("~~~Welcome main~~~\n~~~other has joined this chatroom~~~\nmain:hello other\n", main.getText());
        assertEquals("~~~Welcome other~~~\nmain:hello other\n", other.getText());

        // other send message
        other.sendMessage("hi");
        assertEquals("~~~Welcome main~~~\n~~~other has joined this chatroom~~~\nmain:hello other\nother:hi\n", main.getText());
        assertEquals("~~~Welcome other~~~\nmain:hello other\nother:hi\n", other.getText());

        // main leave the chat
        main.logout();
        assertEquals("", main.getText());
        assertEquals("~~~Welcome other~~~\nmain:hello other\nother:hi\n~~~main has left the chat room~~~\n", other.getText());

        // other leave the chat
        other.logout();
        assertEquals("", other.getText());

    }

    @Test
    public void testParalelWithMultipleThreads() throws Exception {
        ParalelChatter mainThread = new ParalelChatter();
        mainThread.start();

        ParalelChatter otherThread = new ParalelChatter();
        otherThread.start();

        mainThread.login("main");
        assertEquals("~~~Welcome main~~~\n", mainThread.getEmulator().getText());

        // second thread login
        otherThread.login("other");
        assertEquals("~~~Welcome main~~~\n~~~other has joined this chatroom~~~\n", mainThread.getEmulator().getText());
        assertEquals("~~~Welcome other~~~\n", otherThread.getEmulator().getText());

        // main chatter send message
        mainThread.sendMessage("hello other");
        assertEquals("~~~Welcome main~~~\n~~~other has joined this chatroom~~~\nmain:hello other\n", mainThread.getEmulator().getText());
        assertEquals("~~~Welcome other~~~\nmain:hello other\n", otherThread.getEmulator().getText());

        // other send message
        otherThread.sendMessage("hi");
        assertEquals("~~~Welcome main~~~\n~~~other has joined this chatroom~~~\nmain:hello other\nother:hi\n", mainThread.getEmulator().getText());
        assertEquals("~~~Welcome other~~~\nmain:hello other\nother:hi\n", otherThread.getEmulator().getText());

        // main leave the chat
        mainThread.logout();
        assertEquals("", mainThread.getEmulator().getText());
        assertEquals("~~~Welcome other~~~\nmain:hello other\nother:hi\n~~~main has left the chat room~~~\n", otherThread.getEmulator().getText());

        otherThread.logout();

        mainThread.exit();
        otherThread.exit();
    }
}

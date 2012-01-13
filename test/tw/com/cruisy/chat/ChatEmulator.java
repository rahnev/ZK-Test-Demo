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
package tw.com.cruisy.chat;

import java.util.Collection;
import junit.framework.AssertionFailedError;
import org.junit.Ignore;
import org.tsc.emulation.components.Button;
import org.tsc.emulation.components.Div;
import org.tsc.emulation.components.Label;
import org.tsc.emulation.components.Textbox;
import org.tsc.emulation.components.Vbox;
import org.tsc.emulation.exceptions.EmulationException;
import org.tsc.tools.EventExecution;
import org.tsc.tools.ReflectionUtil;
import org.zkoss.zul.Window;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
@Ignore
public class ChatEmulator extends org.tsc.emulation.components.Window {

    private Div loginDiv = null;
    private Textbox nameTb = null;
    private Button loginBtn = null;
    private Textbox msgTb = null;
    private Button exitBtn = null;
    private Vbox msgBoard = null;
    private ChatRoom _chatRoom = null;
    private String _username = null;

    public ChatEmulator(Window instance) throws Exception {
        super(instance);
    }

    public Div getLoginDiv() {
        if (loginDiv == null) {
            loginDiv = (Div) getFellow("loginDiv");
        }
        return loginDiv;
    }

    public Textbox getNameTb() {
        if (nameTb == null) {
            nameTb = (Textbox) getFellow("nameTb");
        }
        return nameTb;
    }

    public Button getLoginBtn() {
        if (loginBtn == null) {
            loginBtn = (Button) getFellow("loginBtn");
        }
        return loginBtn;
    }

    public Textbox getMsgTb() {
        if (msgTb == null) {
            msgTb = (Textbox) getFellow("msgTb");
        }
        return msgTb;
    }

    public Button getExitBtn() {
        if (exitBtn == null) {
            exitBtn = (Button) getFellow("exitBtn");
        }
        return exitBtn;
    }

    public Vbox getMsgBoard() {
        if (msgBoard == null) {
            msgBoard = (Vbox) getFellow("msgBoard");
        }
        return msgBoard;
    }

    public boolean loginVisible() {
        return getLoginDiv().isVisible();
    }

    public ChatRoom getChatRoom() throws EmulationException {
        if (_chatRoom == null) {
            _chatRoom = (ChatRoom) INSTANCE.getDesktop().getWebApp().getAttribute("chatroom");
        }
        return _chatRoom;
    }

    public void waitForLogin(String username) throws EmulationException, InterruptedException {
        Collection<ChatUser> chatUsers = (Collection<ChatUser>) ReflectionUtil.getField(getChatRoom(), "_chatUsers");
        boolean logged = false;
        for (int i = 0; i < 30; i++) { // wait up to 15 seconds start user thread
            synchronized (chatUsers) {
                for (ChatUser chatUser : chatUsers) {
                    if (chatUser.getNickname().equals(username)) {
                        logged = true;
                        break;
                    }
                }
            }
            if (logged) {
                break;
            }
            Thread.sleep(500);
        }
        if (!logged) {
            throw new AssertionFailedError("Time out exeeded. User still not logged");
        }
        Thread.sleep(500);
    }

    public void waitForLogout(String username) throws EmulationException, InterruptedException {
        Collection<ChatUser> chatUsers = (Collection<ChatUser>) ReflectionUtil.getField(getChatRoom(), "_chatUsers");
        boolean logged = true;
        for (int i = 0; i < 30; i++) { // wait up to 15 seconds start user thread
            logged = false;
            synchronized (chatUsers) {
                for (ChatUser chatUser : chatUsers) {
                    if (chatUser.getNickname().equals(username)) {
                        logged = true;
                        break;
                    }
                }
            }
            if (!logged) {
                break;
            }
            Thread.sleep(500);
        }
        if (logged) {
            throw new AssertionFailedError("Time out exeeded. User still logged");
        }
        Thread.sleep(500);
    }

    public void waitToBroadcastAllMessages() throws EmulationException, InterruptedException {
        Collection<ChatUser> chatUsers = (Collection<ChatUser>) ReflectionUtil.getField(getChatRoom(), "_chatUsers");
        boolean allUsersNotified = false;
        for (int i = 0; i < 30 && !allUsersNotified; i++) { // wait up to 15 seconds to broadcast
            allUsersNotified = true;
            synchronized (chatUsers) {
                for (ChatUser chatUser : chatUsers) {
                    if (!chatUser.isAlive()) { // The user thread not started yet
                        allUsersNotified = false;
                        break;
                    }
                    String userMessage = (String) ReflectionUtil.getField(chatUser, "_msg");
                    if (!userMessage.isEmpty()) {
                        allUsersNotified = false;
                        break;
                    }
                }
            }
            Thread.sleep(500);
        }
        Thread.sleep(500);
        if (!allUsersNotified) {
            throw new AssertionFailedError("Time out exeeded. Not all messages sent");
        }
    }

    public void login(String username) throws Exception {
        _username = username;
        getNameTb().setValue(username);
        getLoginBtn().click();
        waitForLogin(username);
        waitToBroadcastAllMessages();
    }

    public void logout() throws Exception {
        getExitBtn().click();
        waitForLogout(_username);
        waitToBroadcastAllMessages();
        _username = null;
    }

    public void sendMessage(String message) throws Exception {
        getMsgTb().setValue(message);
        EventExecution.executeOK(INSTANCE);
        waitToBroadcastAllMessages();
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (Object child : getMsgBoard().getChildren()) {
            if (child instanceof Label) {
                sb.append(((Label) child).getValue()).append("\n");
            }
        }
        return sb.toString();
    }
}

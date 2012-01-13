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

import junit.framework.AssertionFailedError;
import org.tsc.emulation.Client;
import org.tsc.tools.TextUtil;
import org.zkoss.util.logging.Log;
import org.zkoss.zul.Window;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
@org.junit.Ignore
public class ParalelChatter extends Thread {

    private static final Log log = Log.lookup(ChatUser.class);

    private enum Activity {

        Login,
        SendMessage,
        Logout
    }

    private static class Operation {

        public Operation(Activity activity, String value) {
            this.activity = activity;
            this.value = value;
        }
        public Activity activity;
        public String value;
    }
    private final Client _paralelClient;
    private ChatEmulator emulator = null;
    private final Object SEMAPHOR = new Object();
    private boolean _exit = false;
    private Operation _operation = null;

    public ParalelChatter() {
        setName("Paralel chat client thread");
        _paralelClient = new Client();

    }

    private void createThread() throws Exception {
        _paralelClient.create("/chat/chat.zul");
        setName("Paralel chat client thread for desktop " + _paralelClient.getDesktop().getId());
        emulator = new ChatEmulator((Window) _paralelClient.getMainControl());
    }

    @Override
    public void start() {
        synchronized (SEMAPHOR) {
            if (isAlive()) {
                throw new AssertionFailedError("Thread started");
            }
            try {
                super.start();
                SEMAPHOR.wait();
            } catch (InterruptedException ex) {
                System.err.print(TextUtil.exceptionToString(ex));
                _exit = true;
            }
        }
    }

    @Override
    public void run() {
        try {
            createThread();
        } catch (Exception ex) {
            System.err.print(TextUtil.exceptionToString(ex));
            return;
        }
        synchronized (SEMAPHOR) {
            while (!_exit) {
                SEMAPHOR.notify();
                try {
                    if (_operation != null) {
                        switch (_operation.activity) {
                            case Login:
                                emulator.login(_operation.value);
                                break;
                            case Logout:
                                emulator.logout();
                                break;
                            case SendMessage:
                                emulator.sendMessage(_operation.value);
                                break;
                        }
                        _operation = null;
                    }
                    SEMAPHOR.wait();
                } catch (Exception ex) {
                    System.err.print(TextUtil.exceptionToString(ex));
                    _exit = true;
                }
            }
        }
    }

    public void login(String name) throws InterruptedException {
        synchronized (SEMAPHOR) {
            if (!isAlive()) {
                throw new AssertionFailedError("Thread stopped");
            }
            _operation = new Operation(Activity.Login, name);
            SEMAPHOR.notify();
            SEMAPHOR.wait();
        }
    }

    public void sendMessage(String message) throws InterruptedException {
        synchronized (SEMAPHOR) {
            if (!isAlive()) {
                throw new AssertionFailedError("Thread stopped");
            }
            _operation = new Operation(Activity.SendMessage, message);
            SEMAPHOR.notify();
            SEMAPHOR.wait();
        }
    }

    public void logout() throws InterruptedException {
        synchronized (SEMAPHOR) {
            if (!isAlive()) {
                throw new AssertionFailedError("Thread stopped");
            }
            _operation = new Operation(Activity.Logout, null);
            SEMAPHOR.notify();
            SEMAPHOR.wait();
        }
    }

    public void exit() throws InterruptedException {
        synchronized (SEMAPHOR) {
            if (!isAlive()) {
                throw new AssertionFailedError("Thread stopped");
            }
            _exit = true;
            SEMAPHOR.notify();
        }
    }

    public ChatEmulator getEmulator() {
        return emulator;
    }
}

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

import org.tsc.emulation.components.MessageBoxEmulator;
import org.zkoss.util.media.AMedia;
import java.io.IOException;
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
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Window;
import samples.GridSample;
import samples.UploadDownload;
import static org.junit.Assert.*;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class AccessDettachedComponents {

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
    public void testMessageBox() throws Exception {
        Client client = new Client();
        client.create("samples/UploadDownload.zul");

        // upload not image file. Cause show message box
        AMedia uploadMedia = new AMedia("empty", null, null, getClass().getClassLoader().getResourceAsStream("empty"));
        EventExecution.executeUpload(client.getMainControl().getFellow("upload"), uploadMedia);

        //find the MessageBox dialog
        MessageBoxEmulator msg = MessageBoxEmulator.get(client);

        // validate the data
        assertEquals("Not an image: empty", msg.getMessage());
        msg.getOK().click();
        assertTrue(msg.isClosed());
    }

    @Test
    public void testGetComponentByClass() throws Exception {
        // create not attached window 
        GridSample gridSample = (GridSample) ExecutionEmulator.createComponent("samples/GridSample.zul", null, null);

        // find firest dettached component instance of GridSample
        GridSample instance = (GridSample) GuiEnvironment.getComponent(GridSample.class);
        assertEquals(gridSample, instance);
    }

    @Test
    public void testFindComponent() throws EmulationException, IOException {
        // create a popup upload download window
        TestUpdateServlet.enter(GuiEnvironment.getDefaultClient());
        Window uploadDownload = (Window) ExecutionEmulator.createComponent("samples/UploadDownload.zul", null, null);
        uploadDownload.doPopup();
        TestUpdateServlet.leave(GuiEnvironment.getDefaultClient());

        // search the UploadDownload window in detached components
        Window instance = null;
        for (Component cmp : GuiEnvironment.getCreatedComponents()) {
            if (cmp instanceof Window
                    && cmp.getAttribute("$composer") != null
                    && cmp.getAttribute("$composer").getClass() == UploadDownload.class) {
                instance = (Window) cmp;
                break;
            }
        }
        assertEquals(uploadDownload, instance);
    }
}

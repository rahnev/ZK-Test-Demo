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

import cusomenvironment.MyClient;
import java.io.IOException;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tsc.emulation.componentutils.GridUtil;
import org.tsc.emulation.exceptions.EmulationException;
import org.tsc.tools.ExecutionEmulator;
import org.tsc.emulation.servlet.TestUpdateServlet;
import org.tsc.tools.EventExecution;
import org.zkoss.image.AImage;
import org.zkoss.zul.Column;
import org.zkoss.zul.Window;
import samples.AutomatedGrid;
import samples.GridSample;
import samples.UploadDownload;
import cusomenvironment.MyEnvironment;
import org.tsc.emulation.GuiEnvironment;
import static org.junit.Assert.*;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class CustomEnvironmentTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        MyEnvironment.createWithClient("index.html");
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
    public void testSendRequest() throws EmulationException {
        Window sendRequest = (Window) ExecutionEmulator.createComponent("samples/SendRequest.zul", null, null);
        assertTrue(GuiEnvironment.getCreatedComponents().contains(sendRequest));

        EventExecution.executeChange(sendRequest.getFellow("textbox"), "test1");
        assertEquals("test1", sendRequest.getTitle());
    }

    @Test
    public void testUploadDownload() throws EmulationException, IOException {
        MyClient client = new MyClient();
        client.create("samples/UploadDownload.zul");
        Window uploadDownload = (Window) client.getMainControl();
        EventExecution.executeUpload(uploadDownload.getFellow("upload"), new AImage("m1.gif", getClass().getClassLoader().getResourceAsStream("m1.gif")));
        EventExecution.executeClick(uploadDownload.getFellow("download"));
        assertEquals(1, client.getAllDownloads().size());

        // check if composer used
        assertEquals(UploadDownload.class, uploadDownload.getAttribute("$composer").getClass());
    }

    @Test
    public void testGridSample() throws Exception {
        GridSample gridSample = (GridSample) ExecutionEmulator.createComponent("samples/GridSample.zul", null, null);
        assertEquals(gridSample, GuiEnvironment.getComponent(GridSample.class));
        AutomatedGrid grid = (AutomatedGrid) gridSample.getFellow("grid");

        TestUpdateServlet.enter(GuiEnvironment.getDefaultClient());
        grid.changeSource(Arrays.asList(1, 5, 2));
        TestUpdateServlet.leave(GuiEnvironment.getDefaultClient());

        assertEquals("Integer\n1\n5\n2", GridUtil.printString(grid, true));

        Column col = (Column) grid.getColumns().getChildren().get(0);
        EventExecution.executeSort(col);

        assertEquals("Integer\n5\n2\n1", GridUtil.printString(grid, true));
    }
}

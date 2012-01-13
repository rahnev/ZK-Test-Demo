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
import org.tsc.tools.EventExecution;
import org.zkoss.image.AImage;
import org.zkoss.util.media.AMedia;
import org.zkoss.zul.Button;
import static org.junit.Assert.*;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class UploadDownloadTest {

    Button upload = null;
    Button download = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        GuiEnvironment.create("/demo/web", "samples/UploadDownload.zul");
        upload = (Button) GuiEnvironment.getMainControl().getFellow("upload");
        download = (Button) GuiEnvironment.getMainControl().getFellow("download");
    }

    @After
    public void tearDown() throws Exception {
        GuiEnvironment.destroy();
        upload = null;
        download = null;
    }

    @Test
    public void testUploadDownload() throws Exception {
        assertEquals(false, download.isVisible());

        AImage uploadMedia = new AImage("m1.gif", getClass().getClassLoader().getResourceAsStream("m1.gif"));
        EventExecution.executeUpload(upload, uploadMedia);

        assertEquals(true, download.isVisible());
        assertEquals("Download m1.gif", download.getLabel());

        assertEquals(0, GuiEnvironment.getAllDownloads().size());
        EventExecution.executeClick(download);
        assertEquals(1, GuiEnvironment.getAllDownloads().size());

        AMedia downloadMedia = (AMedia) GuiEnvironment.getAllDownloads().values().toArray()[0];
        assertEquals(uploadMedia.getName(), downloadMedia.getName());
        assertEquals(uploadMedia.getContentType(), downloadMedia.getContentType());
        assertEquals(uploadMedia.getFormat(), downloadMedia.getFormat());
        byte[] uploaded = uploadMedia.getByteData();
        byte[] downloaded = downloadMedia.getByteData();
        assertArrayEquals(uploaded, downloaded);
        assertNotSame(uploaded, downloaded);
    }
}

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
package cusomenvironment;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.tsc.emulation.Client;
import org.tsc.emulation.GuiEnvironment;
import org.tsc.emulation.exceptions.EmulationException;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
@org.junit.Ignore
public class MyEnvironment extends GuiEnvironment {

    public static void create() throws ServletException {
        SINGLETONE = new MyEnvironmentImpl();
        WEB_PATH = "/demo/web";
        ((MyEnvironmentImpl) SINGLETONE).create();
    }

    public static HttpServletResponse createWithClient(String page) throws EmulationException {
        try {
            create();
        } catch (ServletException ex) {
            throw new EmulationException(ex);
        }
        Client mainClient = new MyClient();
        HttpServletResponse ret = mainClient.create(page);
        SINGLETONE.setDefaultClient(mainClient);
        return ret;
    }
}

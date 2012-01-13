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

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.tsc.emulation.Client;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
@org.junit.Ignore
public class MyClient extends Client {

    public MyClient() {
        this(new MySession());
    }

    protected MyClient(HttpSession session) {
        super(session);
    }

    @Override
    public HttpServletRequest createRequest() {
        return new MyRequst(this);
    }

    @Override
    public HttpServletRequest createRequest(String desctopId, String componentId, String command, Map values) {
        return new MyRequst(this, desctopId, componentId, command, values);
    }

    @Override
    public HttpServletResponse createResponse() {
        return new MyResponse(this);
    }
}

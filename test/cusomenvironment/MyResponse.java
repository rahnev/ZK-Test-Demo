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

import java.io.IOException;
import java.io.PrintWriter;
import org.tsc.emulation.Client;
import org.tsc.emulation.servlet.TestResponse;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
@org.junit.Ignore
public class MyResponse extends TestResponse {

    private final PrintWriter _myWriter;

    public MyResponse(Client client) {
        super(client);
        _myWriter = new PrintWriter(System.out, true);
        _myWriter.write("...\n");
        _myWriter.write("Response : Tread " + Thread.currentThread().getName() + " Session " + client.getSession().getId() + "\n");
    }

    @Override
    public String getWriterString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return _myWriter;
    }

    @Override
    public void flushBuffer() throws IOException {
        _myWriter.flush();
    }
}

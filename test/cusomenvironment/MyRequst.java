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
import org.tsc.emulation.Client;
import org.tsc.emulation.servlet.TestRequest;
import org.zkoss.zk.ui.Component;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
@org.junit.Ignore
public class MyRequst extends TestRequest {

    public MyRequst(Client client) {
        super(client);
    }

    public MyRequst(Client client, String desctopId, String componentId, String command, Map values) {
        super(client, desctopId, componentId, command, values);
        StringBuilder sb = new StringBuilder();
        sb.append(" desktop:").append(desctopId).append(" command:").append(command);
        if (componentId != null && !componentId.isEmpty()) {
            Component cmp = client.getDesktop().getComponentByUuid(componentId);
            if (cmp != null) {
                sb.append(" component [").append("uid:").append(componentId);
                if (cmp.getId() != null && !cmp.getId().isEmpty()) {
                    sb.append(" id:").append(cmp.getId());
                }
                sb.append(" ").append(cmp.getClass().getSimpleName()).append("]");

            }
        }
        if (values != null) {
            sb.append("\nvalues[");
            boolean first = true;
            for (Object key : values.keySet()) {
                if (!first) {
                    sb.append(", ");
                }
                first = false;
                sb.append(key).append(":").append(values.get(key));
            }
            sb.append("]");
        }
        System.out.println("");
        System.out.println("-----------------------------------------------------");
        System.out.println("Request : Tread " + Thread.currentThread().getName() + " Session " + getSession().getId() + sb.toString());
    }
}

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
package samples;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class GridSample extends Window {

    public void changeSource(int number) throws Exception {
        List source = new ArrayList();
        int hashLo = 7;
        int hashHi = 5;

        for (int i = 0; i < 100; i++) {
            switch (number) {
                case 1:
                    source.add(new Object[]{i, Integer.toHexString(i), Integer.toOctalString(i), Integer.toBinaryString(i)});
                    break;
                case 2:
                    source.add(i);
                    break;
                case 3:
                    hashLo = 17 * hashLo + i;
                    hashHi = 37 * hashHi + i;
                    source.add(new UUID(hashHi, hashLo));
                    break;
                default:
                    Messagebox.show("Only 1, 2 or 3 supported as source");
                    return;
            }
        }
        ((AutomatedGrid) getFellow("grid")).changeSource(source);
    }
}

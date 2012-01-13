/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * Free ZeroKode testing example.
 * Copyright (C) 2011 Telesoft Consulting GmbH http://www.telesoft-consulting.at
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; If not, see http://www.gnu.org/licenses/
 * 
 * Telesoft Consulting GmbH
 * Gumpendorferstraße 83-85
 * House 1, 1st Floor, Office No.1
 * 1060 Vienna, Austria
 * http://www.telesoft-consulting.at/
 */
package accounts;

import accounts.database.User;
import accounts.database.Usergroup;
import java.util.ArrayList;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

/**
 *
 * @author Georgi Rahnev (http://github.com/rahnev)
 * @version $Id$
 */
public class Users extends Window implements AfterCompose {

    private static class UserRenderer implements RowRenderer {

        private final Users _owner;

        private UserRenderer(Users owner) {
            _owner = owner;
        }

        @Override
        public void render(Row row, Object data) throws Exception {
            final User user = (User) data;
            row.appendChild(new Label(user.getID()));
            row.appendChild(new Label(user.getName()));
            if (row.getGrid().getColumns().getChildren().size() == 4) {
                row.appendChild(new Label(user.getUsergroup().getId() + " (" + user.getUsergroup().getName() + ")"));
            }

            Hbox box = new Hbox();
            row.appendChild(box);

            Button edit = new Button("edit");
            Button delete = new Button("delete");
            box.appendChild(edit);
            box.appendChild(delete);
            if (user.getID().equals("admin")) {
                edit.setDisabled(true);
                delete.setDisabled(true);
            }

            delete.addEventListener(Events.ON_CLICK, new EventListener() {

                @Override
                public void onEvent(Event event) throws Exception {
                    if (Messagebox.show("Are you shure to delete user " + user.getID() + "?", "Delete confimation", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES) {
                        user.delete();
                        _owner.reload();
                    }
                }
            });

            edit.addEventListener(Events.ON_CLICK, new EventListener() {

                @Override
                public void onEvent(Event event) throws Exception {
                    UserEditor.show(user);
                    _owner.reload();
                }
            });

        }
    }

    private static class UsergroupRenderer implements TreeitemRenderer {

        @Override
        public void render(Treeitem item, Object data) throws Exception {
            Treerow tr = new Treerow();
            item.appendChild(tr);

            if (((DefaultTreeNode) data).getData().equals("Usergroups")) {
                tr.appendChild(new Treecell("Usergroups"));
                item.setOpen(true);
                return;
            }

            Usergroup usergroup = (Usergroup) ((DefaultTreeNode) data).getData();
            item.setValue(usergroup);
            tr.appendChild(new Treecell(usergroup.getName()));
        }
    }
    private Usergroup _selectedGruop = null;
    private String _stringFilter = null;
    private ListModelList _gridModel = new ListModelList();
    private DefaultTreeModel _treeModel;
    private Tree _usergroupTree;
    private Grid _userGrid;
    private Textbox _fitler;
    private Button _btnFilter;
    private Label _detail;
    private Button _delete;
    private Button _edit;
    private Button _add;

    public ListModel getGridModel() {
        return _gridModel;
    }

    public RowRenderer getGridRenderer() {
        return new UserRenderer(this);
    }

    public TreeitemRenderer getTreeRenderer() {
        return new UsergroupRenderer();
    }

    // button commands
    public void add() throws InterruptedException {
        _selectedGruop = UsergroupEditor.show(null).getChanged();
        reload();
    }

    public void edit() throws InterruptedException {
        if (_selectedGruop != null) {
            UsergroupEditor.show(_selectedGruop);
            reload();
        }
    }

    public void delete() throws InterruptedException {
        if (_selectedGruop != null) {
            if (Messagebox.show("Are you shure to delete usergroup " + _selectedGruop.getId(), "Delete confimation", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES) {
                _selectedGruop.delete();
                _selectedGruop = null;
                reload();
            }
        }
    }

    public void addUser() throws InterruptedException {
        UserEditor.show(_selectedGruop);
        reload();
    }

    public void filter() {
        _stringFilter = _fitler.getValue();
        reload();
    }

    private void reload() {
        // TREE
        //build the complete tree again
        _usergroupTree.setModel(new DefaultTreeModel(new DefaultTreeNode(null, new ArrayList())));
        DefaultTreeNode root = (DefaultTreeNode) _usergroupTree.getModel().getRoot();
        DefaultTreeNode usergroups = new DefaultTreeNode("Usergroups", new ArrayList());
        root.add(usergroups);
        TreeNode selectedNode = null;
        for (Usergroup item : Usergroup.findAll()) {
            DefaultTreeNode groupNode = new DefaultTreeNode(item);
            usergroups.add(groupNode);
            if (item == _selectedGruop) {
                // if same usergroup was selected, mark the node to select
                selectedNode = groupNode;
            }
        }
        if (selectedNode != null) {
            // select the tree item if any
            _usergroupTree.renderItemByPath(_usergroupTree.getModel().getPath(root, selectedNode)).setSelected(true);
        }


        //GRID
        // clean the grid
        _gridModel.clear();
        // build the grid header
        if (_userGrid.getColumns() != null) {
            _userGrid.getColumns().detach();
        }
        Columns columns = new Columns();
        columns.appendChild(new Column("ID"));
        columns.appendChild(new Column("Name"));
        if (_selectedGruop == null) {
            columns.appendChild(new Column("Usergroup"));
        }
        columns.appendChild(new Column());//Buttons column
        _userGrid.appendChild(columns);
        // load the grid model
        _gridModel.addAll(User.findLike(_stringFilter, _selectedGruop));


        //BUTTONS AND TEXT
        // Setup the 
        _delete.setDisabled(false);
        _edit.setDisabled(false);
        if (_selectedGruop == null) {
            _detail.setValue("All User Groups");
            _delete.setDisabled(true);
            _edit.setDisabled(true);
        } else {
            if (_selectedGruop.getId().equals("ADMIN")) {
                _delete.setDisabled(true);
            }
            _detail.setValue("Usergoup : " + _selectedGruop.getId() + " (" + _selectedGruop.getName() + ")");
        }

    }

    @Override
    public void afterCompose() {
        _fitler = (Textbox) getFellow("_filter");
        _btnFilter = (Button) getFellow("_btnFilter");
        _detail = (Label) getFellow("_detail");
        _delete = (Button) getFellow("_delete");
        _add = (Button) getFellow("_add");
        _edit = (Button) getFellow("_edit");
        _usergroupTree = (Tree) getFellow("_usergroupTree");
        _userGrid = (Grid) getFellow("_userGrid");
        reload();
        _usergroupTree.addEventListener(Events.ON_SELECT, new EventListener() {

            @Override
            public void onEvent(Event event) throws Exception {
                _selectedGruop = null;
                if (_usergroupTree.getSelectedItem() != null) {
                    _selectedGruop = (Usergroup) _usergroupTree.getSelectedItem().getValue();
                }
                reload();
            }
        });
    }

    public static Users create(Component parent) {
        return (Users) Executions.createComponents("/accounts/Users.zul", parent, null);
    }
}

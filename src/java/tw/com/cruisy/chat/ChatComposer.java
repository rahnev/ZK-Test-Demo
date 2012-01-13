package tw.com.cruisy.chat;

import java.util.HashMap;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public class ChatComposer extends GenericForwardComposer {
    //auto-wired components

    private Window win;
    private Textbox nameTb;
    private Vbox msgBoard;
    private Textbox msgTb;
    private Div loginDiv;
    private Div scrollDiv;
    private Vbox inputVb;
    private ChatRoom chatRoom;
    private ChatUser chatUser;
    private String nickname;
    private boolean isLogin;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        init();
    }

    public void init() {
        desktop = Executions.getCurrent().getDesktop();
        chatRoom = (ChatRoom) desktop.getWebApp().getAttribute("chatroom");
        if (chatRoom == null) {
            chatRoom = new ChatRoom();
            desktop.getWebApp().setAttribute("chatroom", chatRoom);
        }
    }

    public void onSend$win(ForwardEvent event) {
        HashMap hm = (HashMap) event.getOrigin().getData();
        String msg = (String) hm.get("msg");
        Label msgLbl = new Label(msg);
        msgBoard.appendChild(msgLbl);
        Clients.scrollIntoView(msgLbl);
    }

    public void onOK$win() {
        if (isLogin()) {
            sendMsg();
        } else {
            login();
        }
    }

    /**
     * used for login
     *
     */
    public void onClick$loginBtn() {
        login();
    }

    /**
     * used for exit
     *
     */
    public void onClick$exitBtn() {
        // clean up
        chatUser.setDone();

        // disable server push
        desktop.enableServerPush(false);

        setLogin(false);

        // refresh the UI
        msgBoard.getChildren().clear();
        loginDiv.setVisible(true);
        scrollDiv.setVisible(false);
        inputVb.setVisible(false);
    }

    /**
     * used to send messages
     *
     */
    public void sendMsg() {
        Label message = new Label();
        message.setValue(nickname + ":" + (msgTb.getValue()));
        msgBoard.appendChild(message);
        chatRoom.sendMessage(nickname, msgTb.getValue());
        msgTb.setRawValue("");
        Clients.scrollIntoView(message);
    }

    public void login() {
        // enable server push for this desktop
        desktop.enableServerPush(true);

        nickname = nameTb.getValue();

        // start the chatUser thread
        chatUser = new ChatUser(chatRoom, nickname, desktop);
        chatUser.start();

        // change state of user
        setLogin(true);

        // refresh UI
        msgBoard.appendChild(new Label("~~~Welcome " + nameTb.getValue() + "~~~"));
        nameTb.setRawValue("");
        loginDiv.setVisible(false);
        scrollDiv.setVisible(true);
        inputVb.setVisible(true);
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean bool) {
        isLogin = bool;
    }
}

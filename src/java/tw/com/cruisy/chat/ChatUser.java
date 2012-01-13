package tw.com.cruisy.chat;

import java.util.HashMap;
import org.zkoss.lang.Threads;
import org.zkoss.util.logging.Log;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Window;

public class ChatUser extends Thread {

    private static final Log log = Log.lookup(ChatUser.class);
    private boolean _ceased;
    private ChatRoom _chatRoom;
    private final Desktop _desktop;
    private String _nickname;
    private String _msg;

    public ChatUser(ChatRoom chatRoom, String nickname, Desktop desktop) {
        _chatRoom = chatRoom;
        _nickname = nickname;
        _desktop = desktop;
        _msg = "";
    }

    /**
     * send new messages to UI if necessary
     */
    public void run() {
        if (!_desktop.isServerPushEnabled()) {
            _desktop.enableServerPush(true);
        }
        log.info("Active chatUser thread: " + getName());
        _chatRoom.subscribe(this);
        try {
            while (!_ceased) {
                try {
                    if (_msg.compareTo("") == 0) {
                        Threads.sleep(500);// Update each 0.5 seconds
                    } else {
                        Executions.activate(_desktop);
                        try {
                            process();
                        } finally {
                            Executions.deactivate(_desktop);
                        }
                    }
                } catch (DesktopUnavailableException ex) {
                    log.info("Browser exited.");
                    logout();
                } catch (Throwable ex) {
                    log.error(ex);
                    throw UiException.Aide.wrap(ex);
                }
            }
        } finally {
            logout();
        }
        log.info("chatUser thread ceased: " + getName());
    }

    private void process() throws Exception {
        if (_msg.compareTo("") != 0) {
            HashMap<String, String> msgs = new HashMap<String, String>();
            msgs.put("msg", _msg);
            Window win = (Window) Path.getComponent("/win");
            Events.postEvent(new Event("onSend", win, msgs));
            _msg = "";//reset message
        }
    }

    public void logout() {
        setDone();
        log.info(getNickname() + " has logged out of the chatroom!");
        _chatRoom.unsubscribe(this);
        if (_desktop.isServerPushEnabled()) {
            Executions.getCurrent().getDesktop().enableServerPush(false);
        }
    }

    /**
     * stop this thread
     */
    public void setDone() {
        _ceased = true;
    }

    /**
     * set message for this chatUser
     *
     * @param message
     */
    public void addMessage(String message) {
        _msg = message;
    }

    /**
     * return sender's name
     *
     * @return
     */
    public String getNickname() {
        return _nickname;
    }
}

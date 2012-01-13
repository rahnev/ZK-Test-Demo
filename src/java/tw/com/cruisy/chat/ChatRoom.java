package tw.com.cruisy.chat;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author robbiecheng
 * revised by Richard Lovell
 */
public class ChatRoom {

    private Collection _chatUsers;
    private static final String SIGNAL = "~~~";

    public ChatRoom() {
        _chatUsers = new LinkedList();
    }

    public void sendMessage(String sender, String message) {
        broadcast(sender, sender + ":" + message);
    }

    /**
     * send messages to all chatUsers except sender
     *
     * @param sender
     * @param message
     */
    public void broadcast(String sender, String message) {
        synchronized (_chatUsers) {
            for (ChatUser _chatUser : (Collection<ChatUser>) _chatUsers) {
                if (!_chatUser.getNickname().equals(sender)) {
                    _chatUser.addMessage(message);
                }
            }
        }
    }

    /**
     * subscribe to the chatroom
     *
     * @param chatUser
     */
    public void subscribe(ChatUser chatUser) {
        synchronized (_chatUsers) {
            _chatUsers.add(chatUser);
        }
        broadcast(chatUser.getNickname(), SIGNAL + chatUser.getNickname()
                + " has joined this chatroom" + SIGNAL);
    }

    /**
     * unsubscribe to the chatroom
     *
     * @param chatUser
     */
    public void unsubscribe(ChatUser chatUser) {
        synchronized (_chatUsers) {
            _chatUsers.remove(chatUser);
        }
        broadcast(chatUser.getNickname(), SIGNAL + chatUser.getNickname()
                + " has left the chat room" + SIGNAL);
    }
}

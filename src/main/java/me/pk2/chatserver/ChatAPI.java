package me.pk2.chatserver;

import me.pk2.chatserver.clientside.objects.KeepAliveResponse;
import me.pk2.chatserver.message.Message;
import me.pk2.chatserver.packets.CSChatPacket;
import me.pk2.chatserver.packets.CSKeepAlivePacket;
import me.pk2.chatserver.packets.Packet;
import me.pk2.chatserver.packets.SCUpdatePacket;
import me.pk2.chatserver.user.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatAPI {
    public static final AtomicInteger lastKeepAliveUsers = new AtomicInteger(0);
    private static User user;

    public static KeepAliveResponse handshake(String username) {
        if(user != null)
            return keepAlive();

        user = new User(username);
        return keepAlive();
    }
    public static void sendMessage(String message) {
        boolean success = false;
        while(!success) {
            try {
                Socket socket = new Socket("209.141.58.112", 43710);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(new CSChatPacket(user.username, message));
                socket.close();
                success = true;
            } catch (Exception exception) { try { Thread.sleep(100); } catch (Exception exception1) {} }
        }
    }
    public static KeepAliveResponse keepAlive() {
        boolean success = false;
        while(!success) {
            try {
                Socket socket = new Socket("209.141.58.112", 43710);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(new CSKeepAlivePacket(user.username));
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                Packet packet = (Packet)inputStream.readObject();
                if(packet instanceof SCUpdatePacket) {
                    SCUpdatePacket updatePacket = (SCUpdatePacket)packet;
                    lastKeepAliveUsers.set((int)updatePacket.users.stream().filter(v -> v.isAlive()).count());
                    return new KeepAliveResponse(updatePacket.users, updatePacket.messages);
                }
                socket.close();

                success = true;
            } catch (Exception exception) { try { Thread.sleep(100); } catch (Exception exception1) {} }
        }
        return null;
    }

}
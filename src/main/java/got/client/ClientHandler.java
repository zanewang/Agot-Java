package got.client;

import got.logic.GameState;
import got.server.Message;
import got.server.MessageType;
import got.user.Account;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class ClientHandler extends SimpleChannelUpstreamHandler {
    /**
     * Creates a client-side handler.
     */
    private Client client;

    public ClientHandler(Client client) {
        this.client = client;
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        // Send the first message. Server will not send anything here
        // because the firstMessage's capacity is 0.
        System.out.println("client connect success");
        client.getClientMessageDispatch().setChannel(e.getChannel());
        Message msg = new Message();
        msg.setType(MessageType.CONNECT);
        Account account = new Account();
        account.setName("Test");
        msg.setAccount(account);
        client.getClientMessageDispatch().enque(msg);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        // Send back the received message to the remote peer.
        System.out.println("client receive message");
        Message msg = (Message) e.getMessage();
        client.getClientData().update(msg.getClientData());
        System.out.println(client.getClientData().getTerrMap().values().size());
        client.getClientData().setState(GameState.Ready_Start);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
        e.getChannel().close();
    }
}

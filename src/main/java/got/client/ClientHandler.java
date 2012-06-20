package got.client;

import got.server.Message;

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
        Message message  = new Message();
        message.setMsg("gg");
        e.getChannel().write(message);
        System.out.println("connect success");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        // Send back the received message to the remote peer.
        client.getQueue().add((Message) e.getMessage());
        System.out.println("receive message");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
        e.getChannel().close();
    }
}

package got.server;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class ServerHandler extends SimpleChannelUpstreamHandler {
    private Server server;

    public ServerHandler(Server server) {
        this.server = server;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        // Send back the received message to the remote peer.
        System.out.println("Receive Message");
        Message msg = (Message) e.getMessage();
        server.getChannelMap().put(msg.getAccount(), e.getChannel());
        if (e.getMessage() instanceof Message) {
            server.getServerEventDispatch().enque(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
        e.getChannel().close();
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
        System.out.println("Client close");
    }

}

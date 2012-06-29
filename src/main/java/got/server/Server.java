package got.server;

import got.ui.image.ImageLoader;
import got.user.Account;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

public class Server {
    private ImageLoader imageLoader;
    private ServerGameData serverGameData;
    private ServerEventDispatch serverEventDispatch;
    private ServerEventManager serverEventManager;

    private Map<Account, Channel> channelMap = new HashMap<Account, Channel>();

    private void init() throws Exception {
        imageLoader = new ImageLoader();
        imageLoader.load();
        serverGameData = new ServerGameData();
        serverGameData.init();
        serverEventDispatch = new ServerEventDispatch(this);
        serverEventManager = new ServerEventManager(this);
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors
                .newCachedThreadPool(), Executors.newCachedThreadPool()));

        // Set up the pipeline factory.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new ObjectEncoder(), new ObjectDecoder(ClassResolvers
                        .weakCachingConcurrentResolver(null)), new ServerHandler(Server.this));
            }
        });

        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(7890));
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.init();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @return the serverGameData
     */
    public ServerGameData getServerGameData() {
        return serverGameData;
    }

    /**
     * @return the serverEventDispatch
     */
    public ServerEventDispatch getServerEventDispatch() {
        return serverEventDispatch;
    }

    public ServerEventManager getServerEventManager() {
        return serverEventManager;
    }

    public Map<Account, Channel> getChannelMap() {
        return channelMap;
    }

    /**
     * @return the imageLoader
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }

}

package got.client;

import got.server.Message;

import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

public class Client {
    private Queue<Message> queue = new ArrayBlockingQueue<Message>(100);

    public Client() {
        String host = "127.0.0.1";
        int port = 7890;
        ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors
                .newCachedThreadPool(), Executors.newCachedThreadPool()));

        // Set up the pipeline factory.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new ObjectEncoder(), new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)), new ClientHandler(Client.this));
            }
        });

        // Start the connection attempt.
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));

        // Wait until the connection is closed or the connection attempt fails.
        future.getChannel().getCloseFuture().awaitUninterruptibly();

        // Shut down thread pools to exit.
        bootstrap.releaseExternalResources();
    }

    public void setQueue(Queue<Message> queue) {
        this.queue = queue;
    }

    public Queue<Message> getQueue() {
        return queue;
    }

}

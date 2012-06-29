package got.client;

import got.ui.image.ImageLoader;

import java.net.InetSocketAddress;
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
    private final class ThreadExtension extends Thread {
    }

    private ClientData clientData = new ClientData();
    private ImageLoader imageLoader = new ImageLoader();
    private ClientMessageDispatch clientMessageDispatch = new ClientMessageDispatch();

    public Client() {

    }

    public void init() {

        imageLoader.load();
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                String host = "127.0.0.1";
                int port = 7890;
                ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors
                        .newCachedThreadPool(), Executors.newCachedThreadPool()));

                // Set up the pipeline factory.
                bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
                    public ChannelPipeline getPipeline() throws Exception {
                        return Channels.pipeline(new ObjectEncoder(), new ObjectDecoder(ClassResolvers
                                .weakCachingConcurrentResolver(null)), new ClientHandler(Client.this));
                    }
                });

                // Start the connection attempt.
                ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));

                // Wait until the connection is closed or the connection attempt
                // fails.
                future.getChannel().getCloseFuture().awaitUninterruptibly();

                // Shut down thread pools to exit.
                bootstrap.releaseExternalResources();
            }

        }).start();

    }

    /**
     * @return the clientData
     */
    public ClientData getClientData() {
        return clientData;
    }

    /**
     * @param clientData
     *            the clientData to set
     */
    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }

    /**
     * @return the imageLoader
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    /**
     * @param imageLoader
     *            the imageLoader to set
     */
    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    /**
     * @return the clientMessageDispatch
     */
    public ClientMessageDispatch getClientMessageDispatch() {
        return clientMessageDispatch;
    }

    /**
     * @param clientMessageDispatch
     *            the clientMessageDispatch to set
     */
    public void setClientMessageDispatch(ClientMessageDispatch clientMessageDispatch) {
        this.clientMessageDispatch = clientMessageDispatch;
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.init();
    }

}

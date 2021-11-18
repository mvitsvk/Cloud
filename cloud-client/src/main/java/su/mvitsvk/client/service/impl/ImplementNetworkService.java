package su.mvitsvk.client.service.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import su.mvitsvk.client.service.NetworkService;
import su.mvitsvk.client.factory.Factory;
import su.mvitsvk.common.NetworkObject;


public class ImplementNetworkService implements NetworkService {
    private SocketChannel channel;
    private boolean isrun;

    @Override
    public boolean getIsrun() {
        return isrun;
    }


    @Override
    public void setIsrun(boolean isrun) {
        this.isrun = isrun;
    }

    @Override
    public void connect() {
        new Thread(() -> {
        EventLoopGroup workConnectingGroup = new NioEventLoopGroup();
        try {
            Bootstrap Bootstrap = new Bootstrap();
            Bootstrap.group(workConnectingGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel){
                            channel = socketChannel;
                            socketChannel.pipeline().addLast(new ObjectEncoder());
                            socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                            socketChannel.pipeline().addLast(new InBoundNetwork());
                        }
                    });
            ChannelFuture client = Bootstrap.connect(Factory.getConfig().getValue("HOST"),Integer.parseInt(Factory.getConfig().getValue("PORT"))).sync();
            client.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workConnectingGroup.shutdownGracefully();
        }
        }).start();
        setIsrun(true);
    }

    @Override
    public void disconect() {
        this.channel.disconnect();
        setIsrun(false);
    }

    @Override
    public void sendCMD(NetworkObject obj) {
        this.channel.writeAndFlush(obj);
    }

}

package su.mvitsvk.server.service.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import su.mvitsvk.server.factory.Factory;
import su.mvitsvk.server.service.ServerService;


public class NettyServerService implements ServerService {
    private static final Logger LOGGER = LogManager.getLogger(NettyServerService.class);

    @Override
    public void startServer() {
        EventLoopGroup newConnectGroup = new NioEventLoopGroup(1);
        EventLoopGroup workConnectingGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(newConnectGroup, workConnectingGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel){
                            socketChannel.pipeline()
                                    .addLast(new ObjectEncoder())
                                    .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
                                    .addLast(new InBoundNetwork());
                        }
                        @Override
                        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                            LOGGER.info("client connect");
                        }
                    });
            ChannelFuture server = serverBootstrap.bind(Factory.getConfig().getValue("HOST"),Integer.parseInt(Factory.getConfig().getValue("PORT"))).sync();
            LOGGER.info("server start");

            server.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            newConnectGroup.shutdownGracefully();
            workConnectingGroup.shutdownGracefully();
            LOGGER.info("server stop");
        }

    }
}


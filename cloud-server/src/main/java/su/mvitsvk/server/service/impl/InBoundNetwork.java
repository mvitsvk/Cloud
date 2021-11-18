package su.mvitsvk.server.service.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import su.mvitsvk.common.NetworkObject;
import su.mvitsvk.server.factory.Factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class InBoundNetwork extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LogManager.getLogger(InBoundNetwork.class);

    private CommandDictionary commandDictionary;

    public InBoundNetwork() {
        LOGGER.info("init class InBoundNetwork server");
        commandDictionary = Factory.getCommandDictionary();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.debug("channelRead");
        LOGGER.trace("channelRead :");
        LOGGER.trace(msg);
        NetworkObject networkObjectResult = commandDictionary.responceCMD((NetworkObject) msg);
        LOGGER.trace("channelWrite :");
        LOGGER.trace(msg);
        ctx.writeAndFlush(networkObjectResult);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        LOGGER.debug("channelReadComplete");
        ctx.flush();
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("channelRegistered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("channelUnregistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("channelActive");
        LOGGER.debug("start whois login");
        whois(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("channelInactive");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        LOGGER.debug("userEventTriggered");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("channelWritabilityChanged");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.debug("exceptionCaught");
    }

    private  void whois(ChannelHandlerContext ctx){
        NetworkObject msg = new NetworkObject();
        msg.setCommand("login");
        LOGGER.trace("whois :");
        LOGGER.trace(msg);
        ctx.writeAndFlush(msg);

    };
}

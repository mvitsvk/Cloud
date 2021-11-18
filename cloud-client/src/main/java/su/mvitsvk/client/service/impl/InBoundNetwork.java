package su.mvitsvk.client.service.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import su.mvitsvk.client.factory.Factory;
import su.mvitsvk.common.NetworkObject;

public class InBoundNetwork extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Factory.getCommandDictionary().responceCMD((NetworkObject) msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

}

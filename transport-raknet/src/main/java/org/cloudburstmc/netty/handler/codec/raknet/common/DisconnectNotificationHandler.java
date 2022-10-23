/*
 * Copyright 2022 CloudburstMC
 *
 * CloudburstMC licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.cloudburstmc.netty.handler.codec.raknet.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.cloudburstmc.netty.channel.raknet.RakConstants;
import org.cloudburstmc.netty.channel.raknet.RakDisconnectReason;
import org.cloudburstmc.netty.channel.raknet.packet.EncapsulatedPacket;
import org.cloudburstmc.netty.handler.codec.raknet.AdvancedChannelInboundHandler;

@ChannelHandler.Sharable
public class DisconnectNotificationHandler extends AdvancedChannelInboundHandler<EncapsulatedPacket> {
    private static final InternalLogger log = InternalLoggerFactory.getInstance(DisconnectNotificationHandler.class);

    public static final DisconnectNotificationHandler INSTANCE = new DisconnectNotificationHandler();
    public static final String NAME = "rak-disconnect-notification-handler";

    @Override
    protected boolean acceptInboundMessage(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!super.acceptInboundMessage(ctx, msg)) {
            return false;
        }

        ByteBuf buf = ((EncapsulatedPacket) msg).getBuffer();
        return buf.getUnsignedByte(buf.readerIndex()) == RakConstants.ID_DISCONNECTION_NOTIFICATION;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, EncapsulatedPacket packet) throws Exception {
        ByteBuf buf = packet.getBuffer();
        buf.readUnsignedByte(); // Packet ID
        if (log.isTraceEnabled()) {
            log.trace("RakNet Session ({} => {}) by remote peer!", ctx.channel().localAddress(), ctx.channel().remoteAddress());
        }
        System.out.println("Disconnect received");
        ctx.fireUserEventTriggered(RakDisconnectReason.CLOSED_BY_REMOTE_PEER).close();
        ctx.fireChannelInactive();
    }
}

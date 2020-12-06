package com.nukkitx.network.query.packet;

import com.nukkitx.network.query.QueryPacket;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class StatisticsPacket implements QueryPacket {
    private static final short ID = 0x00;
    // Both
    private int sessionId;
    // Request
    private int token;
    private boolean full;
    // Response
    private ByteBuf payload;

    @Override
    public void decode(ByteBuf buffer) {
        sessionId = buffer.readInt();
        token = buffer.readInt();
        full = (buffer.isReadable());
        buffer.skipBytes(buffer.readableBytes());
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(sessionId);
        buffer.writeBytes(payload);
    }

    @Override
    public short getId() {
        return ID;
    }
}

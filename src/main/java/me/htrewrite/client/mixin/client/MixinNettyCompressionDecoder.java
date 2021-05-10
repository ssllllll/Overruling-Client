package me.htrewrite.client.mixin.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import me.htrewrite.client.module.modules.exploit.AntiChunkBanModule;
import net.minecraft.network.NettyCompressionDecoder;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

@Mixin(NettyCompressionDecoder.class)
public class MixinNettyCompressionDecoder extends ByteToMessageDecoder {
    @Shadow
    private final Inflater inflater;

    @Shadow
    private int threshold;

    public MixinNettyCompressionDecoder(int thresholdIn) {
        this.threshold = thresholdIn;
        this.inflater = new Inflater();
    }

    @Overwrite
    protected void decode(ChannelHandlerContext p_decode_1_, ByteBuf p_decode_2_, List<Object> p_decode_3_) throws DataFormatException, Exception {
        if (p_decode_2_.readableBytes() != 0) {
            PacketBuffer packetbuffer = new PacketBuffer(p_decode_2_);
            int i = packetbuffer.readVarInt();
            if (i == 0) {
                p_decode_3_.add(packetbuffer.readBytes(packetbuffer.readableBytes()));
            } else {
                if (i < this.threshold) {
                    throw new DecoderException("Badly compressed packet - size of " + i + " is below server threshold of " + this.threshold);
                }

                if (i > AntiChunkBanModule.max_packet_size) {
                    throw new DecoderException("Badly compressed packet - size of " + i + " is larger than protocol maximum of " + AntiChunkBanModule.max_packet_size + ". Basically you got chunkbanned gg lol HAHAHAHAHAHAHAHAHA WKLGMWEKLGWGMKERWLGMKLERERLKLGERMQÑEGLG,WEÑLHW,.HWRH.\n\nActivate AntiChunkBan module or increase the max size :)");
                }

                byte[] abyte = new byte[packetbuffer.readableBytes()];
                packetbuffer.readBytes(abyte);
                this.inflater.setInput(abyte);
                byte[] abyte1 = new byte[i];
                this.inflater.inflate(abyte1);
                p_decode_3_.add(Unpooled.wrappedBuffer(abyte1));
                this.inflater.reset();
            }
        }
    }

    public void setCompressionThreshold(int thresholdIn) {
        this.threshold = thresholdIn;
    }
}
package me.sevj6.mixin.mixins;

import me.sevj6.event.TileEntityCreateEvent;
import me.txmc.rtmixin.CallbackInfo;
import me.txmc.rtmixin.mixin.At;
import me.txmc.rtmixin.mixin.Inject;
import me.txmc.rtmixin.mixin.MethodInfo;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.TileEntity;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Bukkit;

public class MixinTest {

    @Inject(info = @MethodInfo(_class = TileEntity.class, name = "create", sig = {World.class, NBTTagCompound.class}, rtype = TileEntity.class), at = @At(pos = At.Position.TAIL))
    public static void onTileEntityCreate(CallbackInfo ci) {
        World world = (World) ci.getParameters()[0];
        NBTTagCompound compound = (NBTTagCompound) ci.getParameters()[1];
        TileEntityCreateEvent createEvent = new TileEntityCreateEvent(world, compound, null);
        Bukkit.getServer().getPluginManager().callEvent(createEvent);
    }
}

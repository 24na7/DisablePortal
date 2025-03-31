package org.okunev.disableportal.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.okunev.disableportal.Command.PortalCommand;

@Mod.EventBusSubscriber(modid = "disableportal", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventSubscriber {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onPlayerUsePortal(PlayerInteractEvent event) {
        if (!PortalCommand.isPortalBlockEnabled()) {
            return;
        }

        if (event.getItemStack().getItem() == Items.FLINT_AND_STEEL) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            if (level.getBlockState(pos).getBlock() == Blocks.OBSIDIAN) {
                event.setCanceled(true);
                if (event.getEntity() instanceof ServerPlayer) {
                    ServerPlayer player = (ServerPlayer) event.getEntity();
                    player.sendSystemMessage(Component.literal("You cannot use Flint and Steel on Obsidian! This feature is disabled in DisablePortal. To enable activation, type /disableportal disable."));
                }
            }
        }

        if (event.getItemStack().getItem() == Items.ENDER_EYE) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();

            if (level.getBlockState(pos).getBlock() instanceof EndPortalFrameBlock) {
                event.setCanceled(true);
                if (event.getEntity() instanceof ServerPlayer) {
                    ServerPlayer player = (ServerPlayer) event.getEntity();
                    player.sendSystemMessage(Component.literal("You cannot activate this portal! This feature is disabled in DisablePortal. To enable activation, type /disableportal disable."));
                }
            }
        }
    }

    public static void register(IEventBus eventBus) {
        MinecraftForge.EVENT_BUS.addListener(ModEventSubscriber::onPlayerUsePortal);
    }
}
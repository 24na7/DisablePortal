package org.okunev.disableportal.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.okunev.disableportal.Command.PortalCommand;

@Mod.EventBusSubscriber(modid = "disableportal", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventSubscriber {

    private static final int PORTAL_FRAME_RADIUS = 1;

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onPlayerUsePortal(PlayerInteractEvent event) {
        if (!PortalCommand.isPortalBlockEnabled()) {
            return;
        }
    }

    // Blocking activate portal
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPortalSpawn(BlockEvent.PortalSpawnEvent event) {
        if (!PortalCommand.isPortalBlockEnabled()) return;
        event.setCanceled(true);
    }

    private static boolean isNearObsidianFrame(Level level, BlockPos pos) {
        for (BlockPos nearbyPos : BlockPos.betweenClosed(
                pos.offset(-PORTAL_FRAME_RADIUS, -PORTAL_FRAME_RADIUS, -PORTAL_FRAME_RADIUS),
                pos.offset(PORTAL_FRAME_RADIUS, PORTAL_FRAME_RADIUS, PORTAL_FRAME_RADIUS)
        )) {
            if (level.getBlockState(nearbyPos).getBlock() == Blocks.OBSIDIAN) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEndPortalActivate(PlayerInteractEvent.RightClickBlock event) {
        if (!PortalCommand.isPortalBlockEnabled()) return;

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        ItemStack item = event.getItemStack();

        if (item.getItem() == Items.ENDER_EYE) {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof EndPortalFrameBlock) {
                event.setCanceled(true);
            }
        }
    }

    public static void register(IEventBus eventBus) {
        MinecraftForge.EVENT_BUS.addListener(ModEventSubscriber::onPlayerUsePortal);
        MinecraftForge.EVENT_BUS.addListener(ModEventSubscriber::onPortalSpawn);
        MinecraftForge.EVENT_BUS.addListener(ModEventSubscriber::onEndPortalActivate);
    }
}
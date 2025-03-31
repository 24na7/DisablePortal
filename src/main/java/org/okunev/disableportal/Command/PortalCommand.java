package org.okunev.disableportal.Command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.okunev.disableportal.Disableportal;

@Mod.EventBusSubscriber(modid = Disableportal.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PortalCommand {

    private static boolean isPortalBlockEnabled = true;

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("disableportal")
                .then(Commands.literal("enable")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> {
                            isPortalBlockEnabled = true;
                            context.getSource().sendSuccess(() -> Component.literal("Portal blocking enabled!"), false);
                            return 1;
                        })
                )
                .then(Commands.literal("disable")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> {
                            isPortalBlockEnabled = false;
                            context.getSource().sendSuccess(() -> Component.literal("Portal blocking disabled!"), false);
                            return 1;
                        })
                )
        );
    }

    public static boolean isPortalBlockEnabled() {
        return isPortalBlockEnabled;
    }
}
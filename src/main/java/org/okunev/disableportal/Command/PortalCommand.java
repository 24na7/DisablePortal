package org.okunev.disableportal.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.okunev.disableportal.Disableportal;
import static org.okunev.disableportal.portal.ModEventSubscriber.isEndPortalEnabled;
import static org.okunev.disableportal.portal.ModEventSubscriber.isNetherPortalEnabled;

@Mod.EventBusSubscriber(modid = Disableportal.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PortalCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("disableportal")
                .then(Commands.literal("nether")
                        .then(Commands.argument("enabled", BoolArgumentType.bool())
                                .requires(source -> source.hasPermission(2))
                                .executes(context -> {
                                    boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                    isNetherPortalEnabled = enabled;
                                    context.getSource().sendSuccess(() -> Component.literal("Nether portal blocking " + (enabled ? "enabled" : "disabled")), false);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("end")
                        .then(Commands.argument("enabled", BoolArgumentType.bool())
                                .requires(source -> source.hasPermission(2))
                                .executes(context -> {
                                    boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                    isEndPortalEnabled = enabled;
                                    context.getSource().sendSuccess(() -> Component.literal("End portal blocking " + (enabled ? "enabled" : "disabled")), false);
                                    return 1;
                                })
                        )
                )
        );
    }

    public static boolean isNetherPortalEnabled() {
        return isNetherPortalEnabled;
    }

    public static boolean isEndPortalEnabled() {
        return isEndPortalEnabled;
    }
}
package net.dirtcraft.plugin.dirtcraftpokedex;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.dirtcraft.plugin.dirtcraftpokedex.Commands.*;
import net.dirtcraft.plugin.dirtcraftpokedex.Utility.CheckDex;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.text.DecimalFormat;

@Plugin(
        id = "dirtcraft-pokedex",
        name = "DirtCraft Pokedex",
        description = "Pokedéx Rewards... remade for DirtCraft",
        url = "https://dirtcraft.net/",
        authors = {
                "juliann"
        }
)
public class DirtCraftPokedex {

    @Inject
    private Logger logger;

    @Inject
    private PluginContainer container;

    private DirtCraftPokedex instance;

    public DecimalFormat decimalFormat = new DecimalFormat("#.##");


    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        instance = this;
    }

    @Listener
    public void onGameInit(GameInitializationEvent event) {
        initCommands();
        Pixelmon.EVENT_BUS.register(new ForgeEvents(new CheckDex(instance)));
    }

    private void initCommands() {
        CommandSpec list = CommandSpec.builder()
                .executor(new List(instance))
                .build();

        CommandSpec claim = CommandSpec.builder()
                .executor(new Claim(instance, new CheckDex(instance)))
                .build();

        CommandSpec check = CommandSpec.builder()
                .executor(new Check(instance, new CheckDex(instance)))
                .build();

        CommandSpec fakeDex = CommandSpec.builder()
                .arguments(GenericArguments.optional(GenericArguments.player(Text.of("player"))))
                .executor(new FakeDex(instance, new CheckDex(instance)))
                .build();

        CommandSpec base = CommandSpec.builder()
                .executor(new Base(instance))
                .child(claim, "claim")
                .child(check, "check")
                .child(list, "list")
                .build();

        CommandSpec caught = CommandSpec.builder()
                .executor(new Caught(instance))
                .build();

        CommandSpec remaining = CommandSpec.builder()
                .executor(new Remaining(instance))
                .build();

        CommandSpec pokemon = CommandSpec.builder()
                .child(remaining, "remaining")
                .child(caught, "caught")
                .child(list, "list")
                .build();

        Sponge.getCommandManager().register(instance, base, "dirtdex", "dex", "pokedex", "pd");
        Sponge.getCommandManager().register(instance, fakeDex, "fakedex");
        Sponge.getCommandManager().register(instance, pokemon, "pokemon", "pixelmon");
        logger.info("Commands for " + container.getName() + " have been initialized");
    }

    public Text format(String message) {
        return TextSerializers.FORMATTING_CODE.deserialize(message);
    }

}

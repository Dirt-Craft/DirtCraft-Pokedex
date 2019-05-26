package net.dirtcraft.plugin.dirtcraftpokedex;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.dirtcraft.plugin.dirtcraftpokedex.Commands.Base;
import net.dirtcraft.plugin.dirtcraftpokedex.Commands.Check;
import net.dirtcraft.plugin.dirtcraftpokedex.Commands.Claim;
import net.dirtcraft.plugin.dirtcraftpokedex.Commands.List;
import net.dirtcraft.plugin.dirtcraftpokedex.Utility.CheckDex;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
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
        Pixelmon.EVENT_BUS.register(new ForgeEvents());
    }

    private void initCommands() {
        CommandSpec remaining = CommandSpec.builder()
                .executor(new List(instance))
                .build();

        CommandSpec claim = CommandSpec.builder()
                .executor(new Claim(instance))
                .build();

        CommandSpec check = CommandSpec.builder()
                .executor(new Check(instance))
                .build();

        CommandSpec base = CommandSpec.builder()
                .executor(new Base(instance))
                .child(claim, "claim")
                .child(check, "check")
                .child(remaining, "list")
                .build();

        Sponge.getCommandManager().register(instance, base, "dirtdex", "dex", "pokedex", "pd");
        logger.info("Commands for " + container.getName() + " have been initialized");
    }

    public CheckDex checkDex() {
        return new CheckDex();
    }

    public Text format(String message) {
        return TextSerializers.FORMATTING_CODE.deserialize(message);
    }

}

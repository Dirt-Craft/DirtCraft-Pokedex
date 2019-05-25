package net.dirtcraft.plugin.dirtcraftpokedex.Commands;

import net.dirtcraft.plugin.dirtcraftpokedex.DirtCraftPokedex;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.ArrayList;

public class Base implements CommandExecutor {

    private final DirtCraftPokedex main;

    public Base(DirtCraftPokedex main) {
        this.main = main;
    }


    @Override
    public CommandResult execute(CommandSource source, CommandContext args) {

        ArrayList<Text> hoverList = new ArrayList<>();
        hoverList.add(main.format("&7Click me to claim your reward"));
        hoverList.add(main.format("&7Click me to check your rank"));

        ArrayList<String> commandList = new ArrayList<>();
        commandList.add("/pokedex claim");
        commandList.add("/pokedex check");

        Text.Builder claim = Text.builder();
        claim.append(
                Text.builder()
                .append(main.format("&6&o/pokedex claim"))
                .onHover(TextActions.showText(hoverList.get(0)))
                .onClick(TextActions.runCommand(commandList.get(0)))
                .build(),
                Text.builder()
                .append(main.format(" &8- "))
                .onHover(TextActions.showText(hoverList.get(0)))
                .onClick(TextActions.runCommand(commandList.get(0)))
                .build(),
                Text.builder()
                .append(main.format("&7Claims pending PokéDex rewards"))
                .onClick(TextActions.runCommand(commandList.get(0)))
                .onHover(TextActions.showText(hoverList.get(0)))
                .build()
        );

        Text.Builder check = Text.builder();
        check.append(
                Text.builder()
                .append(main.format("&6&o/pokedex check"))
                .onHover(TextActions.showText(hoverList.get(1)))
                .onClick(TextActions.runCommand(commandList.get(1)))
                .build(),
                Text.builder()
                        .append(main.format(" &8- "))
                        .onHover(TextActions.showText(hoverList.get(1)))
                        .onClick(TextActions.runCommand(commandList.get(1)))
                        .build(),
                Text.builder()
                .append(main.format("&7Checks your Pokédex rank"))
                .onHover(TextActions.showText(hoverList.get(1)))
                .onClick(TextActions.runCommand(commandList.get(1)))
                .build()

        );

        ArrayList<Text> contents = new ArrayList<>();
        contents.add(main.format(""));
        contents.add(claim.build());
        contents.add(check.build());
        contents.add(main.format(""));

        PaginationList.builder()
                .title(main.format("&cDirtCraft &bPokédex"))
                .contents(contents)
                .padding(main.format("&8&m-"))
                .build()
                .sendTo(source);

        return CommandResult.success();
    }

}

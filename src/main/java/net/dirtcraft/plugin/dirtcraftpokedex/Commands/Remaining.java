package net.dirtcraft.plugin.dirtcraftpokedex.Commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import me.lucko.luckperms.LuckPerms;
import net.dirtcraft.plugin.dirtcraftpokedex.DirtCraftPokedex;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.commons.lang3.text.WordUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Remaining implements CommandExecutor {

    private final DirtCraftPokedex main;

    public Remaining(DirtCraftPokedex main) {
        this.main = main;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {

        if (source instanceof Player) {
            Player player = (Player) source;
            EntityPlayerMP entity = (EntityPlayerMP) source;

            ArrayList<Text> contents = new ArrayList<>();

            int caught = Pixelmon.storageManager.getParty(entity).pokedex.countCaught();
            double percent = Double.parseDouble(main.decimalFormat.format((double) caught / ((double) EnumSpecies.values().length-1) * 100.0D));

            Pokedex playerDex = Pixelmon.storageManager.getParty(entity).pokedex;

            Arrays.stream(EnumSpecies.values()).sorted(Comparator.comparing(EnumSpecies::name)).forEach(species -> {

                if (!playerDex.hasCaught(Pokedex.nameToID(species.name)) && !species.name.equals("MissingNo")) {
                    contents.add(
                            Text.builder()
                                    .append(main.format("&c" + species))
                                    .onHover(TextActions.showText(main.format("&cYou have not caught this pokémon")))
                                    .build()
                    );
                }

            });

            PaginationList.Builder pagination = PaginationList.builder();
            pagination.title(main.format("&cDirtCraft &bPokédex"));
            pagination.padding(main.format("&4&m-"));
            pagination.contents(contents);

            if (!LuckPerms.getApiSafe().isPresent()) {
                pagination.build().sendTo(player);
                return CommandResult.empty();
            }


            try {
                String lpRank = WordUtils.capitalize(LuckPerms.getApiSafe().get().getUser(player.getUniqueId()).getPrimaryGroup());
                if (lpRank.equalsIgnoreCase("default")) lpRank = "Rookie";
                if (lpRank.toLowerCase().equalsIgnoreCase("pokemaster")) {
                    String pokemaster = "PokéMaster";

                    pagination.footer(
                            Text.builder()
                                    .append(main.format("&8[&dHover for Information&8]"))
                                    .onHover(TextActions.showText(main.format(
                                            "&7Rank&8: &6" + pokemaster + "\n" +
                                                    "&7Pokédex Complete&8: &6" + percent + "%\n" +
                                                    "&7Pokémon Caught&8: &6" + Pixelmon.storageManager.getParty(entity).pokedex.countCaught() + "\n" +
                                                    "&7Total Pokémon&8: &6" + (EnumSpecies.values().length-1))))
                                    .build());

                } else {
                    pagination.footer(
                            Text.builder()
                                    .append(main.format("&8[&dHover for Information&8]"))
                                    .onHover(TextActions.showText(main.format(
                                            "&7Rank&8: &6" + lpRank + "\n" +
                                                    "&7Pokédex Complete&8: &6" + percent + "%\n" +
                                                    "&7Pokémon Caught&8: &6" + Pixelmon.storageManager.getParty(entity).pokedex.countCaught() + "\n" +
                                                    "&7Total Pokémon&8: &6" + (EnumSpecies.values().length-1))))
                                    .build());
                }
            } catch (NullPointerException exception) {
                pagination.footer(
                        Text.builder()
                                .append(main.format("&8[&dHover for Information&8]"))
                                .onHover(TextActions.showText(main.format(
                                        "&7Pokédex Complete&8: &6" + percent + "%\n" +
                                                "&7Pokémon Caught&8: &6" + Pixelmon.storageManager.getParty(entity).pokedex.countCaught() + "\n" +
                                                "&7Total Pokémon&8: &6" + (EnumSpecies.values().length-1))))
                                .build());

            }
            pagination.build().sendTo(player);

            return CommandResult.success();

        } else {

            throw new CommandException(main.format("&cYou must be a player to view your remaining Pokémon"));

        }
    }
}

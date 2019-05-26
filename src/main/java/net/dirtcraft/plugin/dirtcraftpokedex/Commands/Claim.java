package net.dirtcraft.plugin.dirtcraftpokedex.Commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import me.lucko.luckperms.LuckPerms;
import net.dirtcraft.plugin.dirtcraftpokedex.DirtCraftPokedex;
import net.dirtcraft.plugin.dirtcraftpokedex.Utility.CheckDex;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;

public class Claim implements CommandExecutor {

    private final DirtCraftPokedex main;
    private final CheckDex checkDex;

    public Claim(DirtCraftPokedex main, CheckDex checkDex) {
        this.main = main;
        this.checkDex = checkDex;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {

        if (source instanceof Player) {
            Player player = (Player) source;
            EntityPlayerMP entity = (EntityPlayerMP) source;

            int caught = Pixelmon.storageManager.getParty(entity).pokedex.countCaught();
            double percent = Double.valueOf(main.decimalFormat.format((double) caught / ((double) EnumSpecies.values().length - 2.0D) * 100.0D));

            PaginationList.Builder pagination = PaginationList.builder();

            if (percent == 100 && !player.hasPermission("group.pokemaster")) {
                checkDex.onRankup("PokéMaster", 100, player, pagination);
            } else if (percent < 100 && percent >= 70 && !player.hasPermission("group.ace")) {
                checkDex.onRankup("Ace", 70, player, pagination);
            } else if (percent < 70 && percent >= 50 && !player.hasPermission("group.expert")) {
                checkDex.onRankup("Expert", 50, player, pagination);
            } else if (percent < 50 && percent >= 30 && !player.hasPermission("group.knowledgable")) {
                checkDex.onRankup("Knowledgable", 30, player, pagination);
            } else if (percent < 30 && percent >= 10 && !player.hasPermission("group.intermedius")) {
                checkDex.onRankup("Intermedius", 10, player, pagination);
            } else {
                pagination.contents(main.format("\n" +
                        "&c&l» &7You currently do &cnot &7have a rankup available"
                        + "\n"));

                if (!LuckPerms.getApiSafe().isPresent()) {
                    pagination.sendTo(player);
                    return CommandResult.empty();
                }

                try {
                    String rank = LuckPerms.getApiSafe().get().getUser(player.getUniqueId()).getPrimaryGroup();
                    rank = rank.substring(0, 1).toUpperCase() + rank.substring(1);
                    if (rank.toLowerCase().contains("pokemaster")) {
                        String pokemaster = "PokéMaster";

                        pagination.footer(
                                Text.builder()
                                        .append(main.format("&7Current Rank&8: &6" + pokemaster))
                                        .build());

                    }
                } catch (NullPointerException exception) {
                    exception.printStackTrace();
                }

                pagination.sendTo(player);
            }

        } else {
            throw new CommandException(main.format("&cOnly a player can claim Pokédex rewards"));
        }

        return CommandResult.success();
    }

}

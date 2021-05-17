package net.dirtcraft.plugin.dirtcraftpokedex.Commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
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

public class Check implements CommandExecutor {

    private final DirtCraftPokedex main;
    private final CheckDex checkDex;

    public Check(DirtCraftPokedex main, CheckDex checkDex) {
        this.main = main;
        this.checkDex = checkDex;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext arguments) throws CommandException {
        if (source instanceof Player) {
            Player player = (Player) source;
            EntityPlayerMP entity = (EntityPlayerMP) source;


            int caught = Pixelmon.storageManager.getParty((EntityPlayerMP) player).pokedex.countCaught();
            double percent = Double.parseDouble(main.decimalFormat.format((double) caught / ((double) (EnumSpecies.values().length - 1)) * 100.0D));

            PaginationList.Builder pagination = PaginationList.builder();

            if (player.hasPermission("group.pokemaster")) {

                checkDex.onCheck("Pokémaster", percent, player, entity, pagination);

            } else if (player.hasPermission("group.ace")) {

                checkDex.onCheck("Ace", percent, player, entity, pagination);

            } else if (player.hasPermission("group.expert")) {

                checkDex.onCheck("Expert", percent, player, entity, pagination);

            } else if (player.hasPermission("group.knowledgeable")) {

                checkDex.onCheck("Knowledgeable", percent, player, entity, pagination);

            } else if (player.hasPermission("group.intermedius")) {

                checkDex.onCheck("Intermedius", percent, player, entity, pagination);

            } else {

                checkDex.onCheck("Rookie", percent, player, entity, pagination);

            }


        } else {
            throw new CommandException(main.format("&cOnly a player can check their Pokédex"));
        }

        return CommandResult.success();
    }

}

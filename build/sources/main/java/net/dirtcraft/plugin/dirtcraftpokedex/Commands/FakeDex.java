package net.dirtcraft.plugin.dirtcraftpokedex.Commands;

import net.dirtcraft.plugin.dirtcraftpokedex.DirtCraftPokedex;
import net.dirtcraft.plugin.dirtcraftpokedex.Utility.CheckDex;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;

public class FakeDex implements CommandExecutor {

    private final DirtCraftPokedex main;
    private final CheckDex checkDex;

    public FakeDex(DirtCraftPokedex main, CheckDex checkDex) {
        this.main = main;
        this.checkDex = checkDex;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {

        if (!(source instanceof Player)) {
            if (!context.getOne("player").isPresent()) {
                throw new CommandException(main.format("&cYou have not specified a player!"));
            } else {
                checkDex.checkDex(context.<Player>getOne("player").get());
            }
        } else {
            if (!context.getOne("player").isPresent()) {
                Player player = (Player) source;
                PaginationList.Builder pagination = PaginationList.builder();
                checkDex.onRankup("Test Rank", null, 100, player, pagination);
            } else {
                checkDex.checkDex(context.<Player>getOne("player").get());
            }
        }

        return CommandResult.success();


    }

}

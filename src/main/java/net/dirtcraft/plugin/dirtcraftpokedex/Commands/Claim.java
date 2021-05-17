package net.dirtcraft.plugin.dirtcraftpokedex.Commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.github.nucleuspowered.nucleus.api.NucleusAPI;
import io.github.nucleuspowered.nucleus.api.exceptions.KitRedeemException;
import me.lucko.luckperms.LuckPerms;
import net.dirtcraft.plugin.dirtcraftpokedex.DirtCraftPokedex;
import net.dirtcraft.plugin.dirtcraftpokedex.Utility.CheckDex;
import net.dirtcraft.plugin.dirtcraftpokedex.Utility.Utility;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.commons.lang3.text.WordUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;

import java.util.concurrent.atomic.AtomicBoolean;

public class Claim implements CommandExecutor {
    private final DirtCraftPokedex main;
    private final CheckDex checkDex;

    public Claim(DirtCraftPokedex main, CheckDex checkDex) {
        this.main = main;
        this.checkDex = checkDex;
    }

    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        if (source instanceof Player) {
            Player player = (Player)source;
            EntityPlayerMP entity = (EntityPlayerMP)source;
            
            AtomicBoolean redeemedKit = new AtomicBoolean(false);
            int caught = Pixelmon.storageManager.getParty(entity).pokedex.countCaught();
            double percent = Double.parseDouble(main.decimalFormat.format((double) caught / ((double) (EnumSpecies.values().length - 1)) * 100.0D));
            PaginationList.Builder pagination = PaginationList.builder();
            if (percent == 100.0D) {
                if (player.hasPermission("group.pokemaster")) {
                    NucleusAPI.getKitService().ifPresent((kitService) -> {
                        kitService.getKit("PokeMaster").ifPresent((kit) -> {
                            try {
                                kit.redeem(player);
                                player.sendMessage(this.main.format("&7You have successfully &aredeemed&7 kit &6PokéMaster"));
                                redeemedKit.set(true);
                            } catch (KitRedeemException var5) {
                            }

                        });
                    });
                    if (redeemedKit.get()) {
                        return CommandResult.success();
                    }

                    this.noRankup(pagination, player);
                } else {
                    this.checkDex.onRankup("PokéMaster", "Ace", 100, player, pagination);
                }

                return CommandResult.success();
            } else if (percent < 100.0D && percent >= 70.0D) {
                if (player.hasPermission("group.ace")) {
                    NucleusAPI.getKitService().ifPresent((kitService) -> {
                        kitService.getKit("Ace").ifPresent((kit) -> {
                            try {
                                kit.redeem(player);
                                player.sendMessage(this.main.format("&7You have successfully &aredeemed&7 kit &6Ace"));
                                redeemedKit.set(true);
                            } catch (KitRedeemException ignored) {
                            }

                        });
                    });
                    if (redeemedKit.get()) {
                        return CommandResult.success();
                    }

                    this.noRankup(pagination, player);
                } else {
                    this.checkDex.onRankup("Ace", "Expert", 70, player, pagination);
                }

                return CommandResult.success();
            } else if (percent < 70.0D && percent >= 50.0D) {
                if (player.hasPermission("group.expert")) {
                    NucleusAPI.getKitService().ifPresent((kitService) -> {
                        kitService.getKit("Expert").ifPresent((kit) -> {
                            try {
                                kit.redeem(player);
                                player.sendMessage(this.main.format("&7You have successfully &aredeemed&7 kit &6Expert"));
                                redeemedKit.set(true);
                            } catch (KitRedeemException ignored) {
                            }

                        });
                    });
                    if (redeemedKit.get()) {
                        return CommandResult.success();
                    }

                    this.noRankup(pagination, player);
                } else {
                    this.checkDex.onRankup("Expert", "Knowledgeable", 50, player, pagination);
                }

                return CommandResult.success();
            } else if (percent < 50.0D && percent >= 30.0D) {
                if (player.hasPermission("group.knowledgeable")) {
                    NucleusAPI.getKitService().ifPresent((kitService) -> {
                        kitService.getKit("Knowledgeable").ifPresent((kit) -> {
                            try {
                                kit.redeem(player);
                                player.sendMessage(this.main.format("&7You have successfully &aredeemed&7 kit &6Knowledgeable"));
                                redeemedKit.set(true);
                            } catch (KitRedeemException ignored) {
                            }

                        });
                    });
                    if (redeemedKit.get()) {
                        return CommandResult.success();
                    }

                    this.noRankup(pagination, player);
                } else {
                    this.checkDex.onRankup("Knowledgeable", "Intermedius", 30, player, pagination);
                }

                return CommandResult.success();
            } else if (percent < 30.0D && percent >= 10.0D) {
                if (player.hasPermission("group.intermedius")) {
                    NucleusAPI.getKitService().ifPresent((kitService) -> {
                        kitService.getKit("Intermedius").ifPresent((kit) -> {
                            try {
                                kit.redeem(player);
                                player.sendMessage(this.main.format("&7You have successfully &aredeemed&7 kit &6Intermedius"));
                                redeemedKit.set(true);
                            } catch (KitRedeemException ignored) {
                            }

                        });
                    });
                    if (redeemedKit.get()) {
                        return CommandResult.success();
                    }

                    this.noRankup(pagination, player);
                } else {
                    this.checkDex.onRankup("Intermedius", (String)null, 10, player, pagination);
                }

                return CommandResult.success();
            } else {
                NucleusAPI.getKitService().ifPresent((kitService) -> {
                    kitService.getKit("Rookie").ifPresent((kit) -> {
                        try {
                            kit.redeem(player);
                            player.sendMessage(this.main.format("&7You have successfully &aredeemed&7 kit &6Rookie"));
                            redeemedKit.set(true);
                        } catch (KitRedeemException ignored) {
                        }

                    });
                });
                if (!redeemedKit.get()) {
                    this.noRankup(pagination, player);
                }
                return CommandResult.success();
            }
        } else {
            throw new CommandException(this.main.format("&cOnly a player can claim Pokédex rewards"));
        }
    }

    private void noRankup(PaginationList.Builder pagination, Player player) {
        pagination.title(this.main.format("&cDirtCraft &7Pokédex"));
        pagination.padding(this.main.format("&4&m-"));
        pagination.contents(this.main.format("\n&c&l» &7You currently do &cnot &7have any rewards available!\n"));
        String rank = WordUtils.capitalizeFully(LuckPerms.getApi().getUser(player.getUniqueId()).getPrimaryGroup());
        if (rank.equalsIgnoreCase("default")) rank = "Rookie";
        if (rank.equalsIgnoreCase("pokemaster")) rank = "PokéMaster";
        pagination.footer(Utility.format("&7Current Rank&8: &6" + rank));
        pagination.sendTo(player);
    }
}

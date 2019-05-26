package net.dirtcraft.plugin.dirtcraftpokedex.Utility;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import me.lucko.luckperms.LuckPerms;
import net.dirtcraft.plugin.dirtcraftpokedex.DirtCraftPokedex;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.FireworkEffect;
import org.spongepowered.api.item.FireworkShapes;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.util.Color;

import java.util.ArrayList;

public class CheckDex {
    
    private final DirtCraftPokedex main;
    
    public CheckDex(DirtCraftPokedex main) {
        this.main = main;
    }

    public void checkDex(Player player) {

        EntityPlayerMP entity = (EntityPlayerMP) player;

        int caught = Pixelmon.storageManager.getParty(entity).pokedex.countCaught();
        double percent = Double.valueOf(main.decimalFormat.format((double) caught / ((double) EnumSpecies.values().length - 2.0D) * 100.0D));

        PaginationList.Builder pagination = PaginationList.builder();

        if (percent == 100 && !player.hasPermission("group.pokemaster")) {

            onRankup("PokéMaster", 100, player, pagination);

        } else if (percent < 100 && percent >= 70 && !player.hasPermission("group.ace")) {

            onRankup("Ace", 70, player, pagination);

        } else if (percent < 70 && percent >= 50 && !player.hasPermission("group.expert")) {

            onRankup("Expert", 50, player, pagination);

        } else if (percent < 50 && percent >= 30 && !player.hasPermission("knowledgable")) {

            onRankup("Knowledgable", 30, player, pagination);

        } else if (percent < 30 && percent >= 10 && !player.hasPermission("intermedius")) {

            onRankup("Intermedius", 10, player, pagination);

        }
    }

    public void onRankup(String rank, int percent, Player player, PaginationList.Builder pagination) {
        ArrayList<Text> contents = new ArrayList<>();
        contents.add(main.format(""));
        contents.add(main.format("&c&l» &aCongratulations! &7You have completed &b" + percent + "% &7of the Pokédex and have been ranked up to &a" + rank));
        contents.add(main.format(""));

        pagination.contents(contents);

        pagination.title(main.format("&cDirtCraft &bPokédex"));
        pagination.padding(main.format("&4&m-"));

        if (rank.toLowerCase().equals("PokéMaster".toLowerCase())) {
            pagination.footer(Text.builder()
                    .append(main.format("&8[&5&nClick me&d Activate Kit " + rank + "&8]"))
                    .onHover(TextActions.showText(main.format("&6Are you sure you want to activate &6kit PokéMaster&6?\n&7You can do this later by using &a&n&o/kit " + rank.toLowerCase().replace("é", "e"))))
                    .onClick(TextActions.runCommand("/kit " + rank.toLowerCase().replace("é", "e")))
                    .build());
        } else {
            pagination.footer(Text.builder()
                    .append(main.format("&8[&5&nClick me&d Activate Kit " + rank + "&8]"))
                    .onHover(TextActions.showText(main.format("&6Are you sure you want to activate &6kit " + rank + " &6?\n&7You can do this later by using &a&n&o/kit " + rank.toLowerCase().replace("é", "e"))))
                    .onClick(TextActions.runCommand("/kit " + rank.toLowerCase().replace("é", "e")))
                    .build());
        }

        FireworkEffect fireworkEffect = FireworkEffect.builder()
                .color(Color.mixColors(Color.RED, Color.BLACK, Color.WHITE))
                .shape(FireworkShapes.BURST)
                .trail(true)
                .build();

        Entity firework = player.getWorld().createEntity(EntityTypes.FIREWORK, player.getLocation().getPosition());

        firework.offer(Keys.FIREWORK_EFFECTS, Lists.newArrayList(fireworkEffect));

        player.getWorld().spawnEntity(firework);

        pagination.build().sendTo(player);

        Sponge.getServer().getOnlinePlayers().forEach(online -> {
            if (!online.equals(player)) {
                online.sendMessage(main.format("&c&l» &6" + player.getName() + " &7has completed &b" + percent + "% &7of the Pokédex and ranked up to &a" + rank));
            }
        });
    }

    public void onCheck(String rank, double percent, Player player, EntityPlayerMP entity, PaginationList.Builder pagination) {

        pagination.title(main.format("&cDirtCraft &bPokédex"));
        pagination.padding(main.format("&4&m-"));

        pagination.contents(Text.builder()
                .append(main.format("\n" +
                        "&7You are currently a &6" + rank + "&7"
                        + "\n"))
                .onHover(TextActions.showText(
                        main.format("&7You have completed &6" + percent + "% &7of the Pokédex and are a &6" + rank)))
                .build());

        if (LuckPerms.getApiSafe().isPresent()) {
            String lpRank = LuckPerms.getApiSafe().get().getUser(player.getUniqueId()).getPrimaryGroup();
            lpRank = lpRank.substring(0, 1).toUpperCase() + lpRank.substring(1);
            if (lpRank.toLowerCase().equalsIgnoreCase("pokemaster")) {
                String pokemaster = "PokéMaster";

                pagination.footer(
                        Text.builder()
                                .append(main.format("&7[&dHover for Information&7]"))
                                .onHover(TextActions.showText(main.format(
                                        "&7Rank&8: &6" + pokemaster + "\n" +
                                                "&7Pokédex Complete&8: &6" + percent + "%\n" +
                                                "&7Pokémon Caught&8: &6" + Pixelmon.storageManager.getParty(entity).pokedex.countCaught() + "\n" +
                                                "&7Total Pokémon&8: &6" + main.decimalFormat.format(EnumSpecies.values().length - 2.0D))))
                                .build());

            } else {
                pagination.footer(
                        Text.builder()
                                .append(main.format("&7[&dHover for Information&7]"))
                                .onHover(TextActions.showText(main.format(
                                        "&7Rank&8: &6" + lpRank + "\n" +
                                                "&7Pokédex Complete&8: &6" + percent + "%\n" +
                                                "&7Pokémon Caught&8: &6" + Pixelmon.storageManager.getParty(entity).pokedex.countCaught() + "\n" +
                                                "&7Total Pokémon&8: &6" + main.decimalFormat.format(EnumSpecies.values().length - 2.0D))))
                                .build());

            }
        }
        pagination.sendTo(player);
    }

}

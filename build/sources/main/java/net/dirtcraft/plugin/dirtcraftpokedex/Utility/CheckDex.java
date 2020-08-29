package net.dirtcraft.plugin.dirtcraftpokedex.Utility;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import net.dirtcraft.plugin.dirtcraftpokedex.DirtCraftPokedex;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.commons.lang3.text.WordUtils;
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
        double percent = Double.parseDouble(main.decimalFormat.format((double) caught / ((double) EnumSpecies.values().length) * 100.0D));

        PaginationList.Builder pagination = PaginationList.builder();

        if (percent == 100 && !player.hasPermission("group.pokemaster")) {

            onRankup("PokéMaster", "Ace", 100, player, pagination);

        } else if (percent < 100 && percent >= 70 && !player.hasPermission("group.ace")) {

            onRankup("Ace", "Expert", 70, player, pagination);

        } else if (percent < 70 && percent >= 50 && !player.hasPermission("group.expert")) {

            onRankup("Expert", "Knowledgable", 50, player, pagination);

        } else if (percent < 50 && percent >= 30 && !player.hasPermission("group.knowledgable")) {

            onRankup("Knowledgable", "Intermedius", 30, player, pagination);

        } else if (percent < 30 && percent >= 10 && !player.hasPermission("group.intermedius")) {

            onRankup("Intermedius", null,10, player, pagination);

        }
    }

    public void onRankup(String rank, String oldRank, int percent, Player player, PaginationList.Builder pagination) {
        ArrayList<Text> contents = new ArrayList<>();
        contents.add(main.format(""));
        contents.add(main.format("&c&l» &aCongratulations! &7You have completed &b" + percent + "% &7of the Pokédex and have been ranked up to &a" + rank));
        contents.add(main.format(""));

        pagination.contents(contents);

        pagination.title(main.format("&cDirtCraft &bPokédex"));
        pagination.padding(main.format("&4&m-"));

        String rankString = rank.toLowerCase().replace("é", "e");

        pagination.footer(Text.builder()
                .append(main.format("&8[&5&nClick Me&d Activate Kit " + rank + "&8]"))
                .onHover(TextActions.showText(main.format("&6Are you sure you want to activate &6kit " + rank + "&6?\n&7You can do this later by using &a&n&o/kit " + rankString)))
                .onClick(TextActions.runCommand("/kit " + rankString))
                .build());

        User user = LuckPerms.getApi().getUser(player.getUniqueId());
        Node group = LuckPerms.getApi().getNodeFactory()
                .makeGroupNode(rankString)
                .setServer(LuckPerms.getApi().getConfiguration().getServer())
                .build();

        if (user != null) {
            if (oldRank != null) {
                oldRank = oldRank.toLowerCase().replace("é", "e");
                user.unsetPermission(
                        LuckPerms.getApi().getNodeFactory()
                                .makeGroupNode(oldRank.toLowerCase())
                                .setServer(LuckPerms.getApi().getConfiguration().getServer())
                                .build());
            }

            user.setPermission(group);
            LuckPerms.getApi().getUserManager().saveUser(user);
        } else main.getLogger().error("Unable to set new group for " + player.getName());

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
            if (!online.equals(player)) online.sendMessage(main.format("&c&l» &6" + player.getName() + " &7has completed &b" + percent + "% &7of the Pokédex and ranked up to &a" + rank));
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
            String lpRank = WordUtils.capitalizeFully(LuckPerms.getApiSafe().get().getUser(player.getUniqueId()).getPrimaryGroup());
            lpRank = WordUtils.capitalize(lpRank);
            if (lpRank.equalsIgnoreCase("default")) lpRank = "Rookie";
            if (lpRank.toLowerCase().equals("pokemaster")) lpRank = "PokéMaster";
            pagination.footer(
                    Text.builder()
                            .append(main.format("&7[&dHover for Information&7]"))
                            .onHover(TextActions.showText(main.format(
                                    "&7Rank&8: &6" + lpRank + "\n" +
                                            "&7Pokédex Complete&8: &6" + percent + "%\n" +
                                            "&7Pokémon Caught&8: &6" + Pixelmon.storageManager.getParty(entity).pokedex.countCaught() + "\n" +
                                                "&7Total Pokémon&8: &6" + EnumSpecies.values().length)))
                            .build());
        }
        pagination.sendTo(player);
    }

}

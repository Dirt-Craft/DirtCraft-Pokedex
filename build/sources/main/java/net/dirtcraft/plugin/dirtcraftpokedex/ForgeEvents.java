package net.dirtcraft.plugin.dirtcraftpokedex;

import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import net.dirtcraft.plugin.dirtcraftpokedex.Utility.CheckDex;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.entity.living.player.Player;

public class ForgeEvents {

    private final CheckDex checkDex;

    public ForgeEvents(CheckDex checkDex) {
     this.checkDex = checkDex;
    }

    @SubscribeEvent
    public void onPixelmonReceived(PixelmonReceivedEvent event) {

        Player player = (Player) event.player;

        checkDex.checkDex(player);

    }
}

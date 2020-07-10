package net.dirtcraft.plugin.dirtcraftpokedex.Utility;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class Utility {

    public static Text format(String text) {
        return TextSerializers.FORMATTING_CODE.deserialize(text);
    }

}

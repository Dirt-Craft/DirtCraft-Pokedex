package net.dirtcraft.plugin.dirtcraftpokedex.Configuration;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class PluginConfiguration {
    @Setting(value = "Data")
    private PluginConfiguration.Data data = new PluginConfiguration.Data();

    @ConfigSerializable
    public static class Data {


    }

}
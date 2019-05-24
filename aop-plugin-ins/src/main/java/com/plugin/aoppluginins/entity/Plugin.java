package com.plugin.aoppluginins.entity;

import java.util.List;

/**
 * Created by DESTINY on 2019/5/24.
 */
public class Plugin {

    private String name;
    private List<PluginConfig> configs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PluginConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<PluginConfig> configs) {
        this.configs = configs;
    }
}

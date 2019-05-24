package com.plugin.aoppluginins.entity;

/**
 * Created by DESTINY on 2019/5/24.
 */
public class PluginConfig {

    private String id;
    private String name;
    private String className;
    private String jarRemoteUrl;

    private boolean active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getJarRemoteUrl() {
        return jarRemoteUrl;
    }

    public void setJarRemoteUrl(String jarRemoteUrl) {
        this.jarRemoteUrl = jarRemoteUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

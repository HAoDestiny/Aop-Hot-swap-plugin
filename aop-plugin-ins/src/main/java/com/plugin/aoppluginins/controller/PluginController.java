package com.plugin.aoppluginins.controller;

import com.alibaba.fastjson.JSON;
import com.plugin.aoppluginins.spring.DefaultSpringPluginFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.server.PathParam;
import java.io.IOException;

/**
 * Created by DESTINY on 2019/5/24.
 */

@Controller
@RequestMapping("/plugin")
public class PluginController {

    @Autowired
    private DefaultSpringPluginFactory pluginFactory;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public String getHavePlugins() throws IOException {
        return JSON.toJSONString(pluginFactory.flushConfigs());
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    @ResponseBody
    public String active(@PathParam(value = "pluginId") String pluginId) {
        pluginFactory.activePlugin(pluginId);
        return "ACTIVE SUCCESS";
    }

    @RequestMapping(value = "/enable", method = RequestMethod.GET)
    @ResponseBody
    public String enable(@PathParam(value = "pluginId") String pluginId) {
        pluginFactory.enablePlugin(pluginId);
        return "ENABLE SUCCESS";
    }
}

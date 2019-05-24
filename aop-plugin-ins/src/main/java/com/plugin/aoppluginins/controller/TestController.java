package com.plugin.aoppluginins.controller;

import com.plugin.aoppluginins.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.server.PathParam;

/**
 * Created by DESTINY on 2019/5/24.
 */

@Controller
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test(@PathParam("userId") String userId) {

        System.out.println("userId = " + userId);
        return testService.test(userId);
    }

}

package com.plugin.aoppluginins.service.impl;

import com.plugin.aoppluginins.service.TestService;
import org.springframework.stereotype.Service;

/**
 * Created by DESTINY on 2019/5/24.
 */

@Service
public class TestServiceImpl implements TestService {

    @Override
    public String test(String userId) {
        return "userId=" + userId;
    }
}

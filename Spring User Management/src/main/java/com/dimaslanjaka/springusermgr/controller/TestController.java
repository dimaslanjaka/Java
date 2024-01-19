package com.dimaslanjaka.springusermgr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @RequestMapping(value = { "/test", "/" }, produces = "text/html")
    @ResponseBody
    public String index() {
        return "<title>index</title><body>hello world</title>";
    }
}

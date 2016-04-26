package com.hao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by user on 2016/2/24.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "redirect:/index";
    }

    @RequestMapping("index")
    public String home(){
        return "index";
    }


}

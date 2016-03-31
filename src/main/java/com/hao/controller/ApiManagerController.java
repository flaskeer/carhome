package com.hao.controller;

import com.hao.model.api.Api;
import com.hao.service.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by user on 2016/3/31.
 */
@Controller
@RequestMapping("api")
public class ApiManagerController extends AbstractController{

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiManagerController.class);

    @Autowired
    private ApiService apiService;

    @RequestMapping(value = "add",method = RequestMethod.GET)
    public String addApi() {
        return "api/edit";
    }

    @RequestMapping(value = "edit",method = RequestMethod.GET)
    public String editApi(@RequestParam(value = "id") String id, Model model) {
        Api api = apiService.query(id);
        if (api != null) {
            model.addAttribute("api",api);
        } else {
            LOGGER.info("此API不存在 API id {}",id);
        }
        return "api/edit";
    }

}

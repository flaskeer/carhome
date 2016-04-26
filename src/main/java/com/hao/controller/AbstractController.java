package com.hao.controller;

import com.hao.enums.TypeMessageEnum;
import com.hao.model.vo.MessageVo;
import com.hao.model.vo.UserVo;
import com.hao.util.session.SessionManager;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

import static com.hao.constants.Constants.BRACE_SPLIT_PATTERN;
/**
 * Created by user on 2016/2/24.
 */

public abstract class AbstractController {


    protected UserVo getLoginUser(HttpServletRequest request){
        return SessionManager.INSTANCE.getLoginUser(request.getSession());
    }

    protected void setMessage(Model model,TypeMessageEnum type,String message,String ... url){
        setMessage(model,type,message,null,true,url);
    }

    protected void setMessage(Model model,TypeMessageEnum type,String message){
        setMessage(model,type,message,null,true);
    }

    protected void setMessage(Model model, TypeMessageEnum type,String message,String messageIcon,
      Boolean closeAble,String ... urls){
        if(urls.length != 0){
            for (String url : urls) {
                if(BRACE_SPLIT_PATTERN.matcher(message).find()){
                    message = message.replaceFirst("\\{","<a href=\"" + url + "\" target=\"_blank\">").replaceFirst("\\}","</a>");
                }
            }
        }
        MessageVo vo = new MessageVo(type.toString(),messageIcon,message,closeAble);
        model.addAttribute("message",vo);
    }

}

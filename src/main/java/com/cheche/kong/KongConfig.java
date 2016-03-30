package com.cheche.kong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class KongConfig {

    /**
     * kong 连接地址
     */
    @Value("${kong.admin.address}")
    private String kongAddress;

    /**
     * kong 内部API 请求端口
     */
    @Value("${kong.admin.port}")
    private Integer kongAdminPort;

    /**
     * kong调用超时时间
     */
    @Value("${kong.invoke.timeout}")
    private Integer kongInvokeTimeoutMills;

    private String getKongAddress() {
        if (kongAddress != null) {
            return kongAddress.startsWith("http") ? kongAddress : "http://" + kongAddress;
        }
        return "http://127.0.0.1";
    }

    public Integer getKongAdminPort() {
        if (kongAdminPort != null) {
            return kongAdminPort;
        }
        return 8001;
    }

    /**
     * 获取kong  的API管理地址
     * @return
     */
    public String getKongAdminUrl() {
        return getKongAddress() + ":" + getKongAdminPort();
    }

    private Integer getTimeoutMills() {
        return kongInvokeTimeoutMills;
    }


}

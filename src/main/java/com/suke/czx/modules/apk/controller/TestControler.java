package com.suke.czx.modules.apk.controller;

import com.suke.czx.modules.apk.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author cuiyubao
 * @date 2024/9/29 20:57
 */
@RestController
public class TestControler {

    @Resource
    private TestService testService;

    @GetMapping("/api/v1/traceId")
    public String traceId() {
//        testService.dealPdf1();
//        testService.splicePdf();
        testService.sortPdf();
        return "";
    }






}

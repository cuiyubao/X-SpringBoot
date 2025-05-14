package com.suke.czx.modules.apk.controller;

import com.suke.czx.modules.apk.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

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
//        testService.sortPdf();
        testService.mergeImage();
        return "";
    }

    @GetMapping("/api/v1/traceId1")
    public String traceId1() {
        String url = "https://file.ljcdn.com/utopia-file/f1/bfcfd37e6d1d4bbdcaa32e7a8562986d38c671f8.pdf";
        String url1 = "https://file.ljcdn.com/utopia-file/f1/18251993f4366ae91d164f21c709845326c6f6f4.pdf";
        List<String> strings = testService.pdf2ImageParallel(url1);
        System.out.println("转图片结果："+strings);
        try {
            testService.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    @GetMapping("/api/v1/traceId2")
    public String traceId2() {
        String url = "https://file.ljcdn.com/utopia-file/f1/bfcfd37e6d1d4bbdcaa32e7a8562986d38c671f8.pdf";
        String url1 = "https://file.ljcdn.com/utopia-file/f1/18251993f4366ae91d164f21c709845326c6f6f4.pdf";
        List<String> strings = testService.pdf2ImageParallel2(url1);
        System.out.println("转图片结果："+strings);
        try {
            testService.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }









}

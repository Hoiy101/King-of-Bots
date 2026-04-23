package com.kob.backend.controller.record;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.service.record.GetRecotdListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetRecordListController {
    @Autowired
    private GetRecotdListService getRecotdListService;

    @GetMapping("/record/getlist/")
    private JSONObject getRecordList(@RequestParam Map<String,String> data) {
        return getRecotdListService.getList(Integer.parseInt(data.get("page")));
    }
}

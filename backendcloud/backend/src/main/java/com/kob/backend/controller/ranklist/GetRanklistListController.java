package com.kob.backend.controller.ranklist;

import com.kob.backend.pojo.User;
import com.kob.backend.service.ranklist.GetRanklistListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetRanklistListController {
    @Autowired
    private GetRanklistListService getRanklistListService;

    @GetMapping("/ranklist/getlist/")
    private List<User> getRecordList(){
        return getRanklistListService.getlist();
    }
}

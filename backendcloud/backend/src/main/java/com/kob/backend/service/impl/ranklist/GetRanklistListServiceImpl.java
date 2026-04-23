package com.kob.backend.service.impl.ranklist;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.ranklist.GetRanklistListService;
import com.kob.backend.service.user.Bot.GetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GetRanklistListServiceImpl implements GetRanklistListService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getlist() {
        int k = 0;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        List<User> users = userMapper.selectList(queryWrapper);
        List<User> list = new ArrayList<>();
        for (User user : users) {
            if(k >= 10) break;
            list.add(user);
            k ++;
        }
        return list;
    }
}

package com.kob.backend.service.impl.record;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.service.record.GetRecotdListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class GetRecordListServiceImpl implements GetRecotdListService {
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        IPage<Record> recordIPage = new Page<>(page,10);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        List<Record> list = recordMapper.selectPage(recordIPage,queryWrapper).getRecords();
        List<JSONObject> items = new LinkedList<>();
        for(Record record:list){
            JSONObject item = new JSONObject();
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            item.put("a_username",userA.getUsername());
            item.put("b_username",userB.getUsername());
            item.put("a_photo",userA.getPhoto());
            item.put("b_photo",userB.getPhoto());
            item.put("record",record);
            String result = "平局";
            if("a".equals(record.getLoser())){
                result = "B胜利";
            }
            else if("b".equals(record.getLoser())){
                result = "A胜利";
            }
            item.put("result",result);
            items.add(item);
        }

        JSONObject resp= new JSONObject();
        resp.put("items",items);
        resp.put("records_count",recordMapper.selectCount(null));
        return resp;
    }
}

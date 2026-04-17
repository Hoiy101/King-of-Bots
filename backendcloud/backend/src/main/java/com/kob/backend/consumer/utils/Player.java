package com.kob.backend.consumer.utils;

import com.kob.backend.pojo.Bot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer sx;
    private Integer sy;
    private Integer BotId; // -1表示人工操作
    private String BotCode;
    private List<Integer> steps;

    private  boolean check_tail_increasing(int step){
        if(step <= 10){
            return true;
        } else if(step % 3 == 1){
            return true;
        }
        return false;
    }

    public List<Cell> getcells(){
        List<Cell> res = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0},  dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x,y));
        for(int d: steps){
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x,y));
            if(!check_tail_increasing(++ step)){
                res.remove(0);
            }
        }
        return res;
    };
    public String getStepsString(){
        StringBuilder res = new StringBuilder();
        for(int d: steps){
            res.append(d);
        }
        return res.toString();
    }
}

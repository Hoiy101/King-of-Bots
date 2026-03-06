package com.kob.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchingPool extends Thread {
    private static List<Player> players = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();

    private static RestTemplate restTemplate;
    private final static String startGameUrl = "http://127.0.0.1:3000/pk/start/game/";

    @Autowired
    public void setMatchingPool(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }

    public void addPlayer(Integer userId, Integer rating) {
        lock.lock();
        try {
            if(players.size() == 0){
                players.add(new Player(userId, rating, 0));
            }
            else {
                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i).getUserId().equals(userId)) {
                        players.remove(players.get(i));
                        players.add(new Player(userId, rating, 0));
                        break;
                    }
                    if (i == players.size() - 1) {
                        players.add(new Player(userId, rating, 0));
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId) {
        lock.lock();
        try{
            players.removeIf(player -> player.getUserId().equals(userId));
        }finally {
            lock.unlock();
        }
    }

    private void incrementWaitingTime() {
            for(Player player : players){
                player.setWaitingTime(player.getWaitingTime() + 1);
            }
    }

    private boolean checkMatched(Player a, Player b) { //判断能否匹配
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        return ratingDelta <= a.getWaitingTime() * 10 && ratingDelta <= b.getWaitingTime() * 10 && !a.getUserId().equals(b.getUserId());
    }

    private void sendResult(Player a, Player b) { //返回两名玩家匹配结果
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", a.getUserId().toString());
        data.add("b_id", b.getUserId().toString());
        restTemplate.postForObject(startGameUrl, data, String.class);
    }

    private void matchingplayer() {
        boolean[] used = new boolean[players.size()];
        System.out.println("matchingplayer: " + players.toString());
        for(int i = 0; i < players.size(); i++){
            if(used[i]){continue;}
            for(int j = i + 1; j < players.size(); j++){
                if(used[j]){continue;}
                Player a = players.get(i);
                Player b = players.get(j);
                if(checkMatched(a, b)){
                    sendResult(a, b);
                    used[i] = true;
                    used[j] = true;
                    break;
                }
            }
        }
        for(int i = players.size() - 1; i >= 0; i--){
            if(used[i]){
                players.remove(players.get(i));
            }
        }
    }
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    incrementWaitingTime();
                    matchingplayer();
                }finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

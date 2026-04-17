package com.kob.matchingsystem.service;

public interface MatchingService {
    String addplayer(Integer userId, Integer rating,  Integer botId);
    String removeplayer(Integer userId);
}

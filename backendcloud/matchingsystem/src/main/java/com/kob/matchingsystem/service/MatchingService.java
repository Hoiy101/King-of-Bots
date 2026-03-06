package com.kob.matchingsystem.service;

public interface MatchingService {
    String addplayer(Integer userId, Integer rating);
    String removeplayer(Integer userId);
}

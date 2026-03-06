package com.kob.backend.controller.pk;

import com.kob.backend.service.pk.StartGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class StartGameController {
    @Autowired
    StartGameService startGameService;

    @PostMapping("/pk/start/game/")
    public String startGame(@RequestParam MultiValueMap<String,String> Data) {
        Integer aId = Integer.parseInt(Objects.requireNonNull(Data.getFirst("a_id")));
        Integer bId = Integer.parseInt(Objects.requireNonNull(Data.getFirst("b_id")));
        return startGameService.startGame(aId, bId);
    }
}

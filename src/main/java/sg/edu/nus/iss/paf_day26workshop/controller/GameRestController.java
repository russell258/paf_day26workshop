package sg.edu.nus.iss.paf_day26workshop.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.paf_day26workshop.model.Game;
import sg.edu.nus.iss.paf_day26workshop.service.GameService;

@RestController
@RequestMapping
public class GameRestController {
    @Autowired
    GameService svc;

    @GetMapping(path="/games")
    public ResponseEntity<String> listAllGames(@RequestParam (defaultValue="25") Integer limit, @RequestParam(defaultValue="0")Integer offset){
        return new ResponseEntity<>(svc.listGames(limit, offset).toString(), HttpStatus.OK);
    }

    @GetMapping(path="/games/rank")
    public ResponseEntity<String> listGameByRank(@RequestParam(defaultValue="25")Integer limit, @RequestParam(defaultValue="0")Integer offset){
        List<Game> list = svc.listGameByRank(limit, offset);
        return new ResponseEntity<String>(svc.createReturnJson(list, limit, offset).toString(),HttpStatus.OK);
    }

    @GetMapping(path="/game/{id}")
    public ResponseEntity<String> listGameById(@PathVariable Integer id){
        Document doc = svc.listGameById(id);
        if (doc == null){
            JsonObject errorJson = Json.createObjectBuilder()
                                        .add("error","No game found with ID: "+ id)
                                        .build();
            return new ResponseEntity<>(errorJson.toString(),HttpStatus.NOT_FOUND);
        }

        double average = svc.getSumOfRatings(id)/ svc.countComments(id);

        JsonObject gameDetailsJson = Json.createObjectBuilder()
                                            .add("game_id",doc.getInteger("id"))
                                            .add("name",doc.getString("name"))
                                            .add("year",doc.getInteger("year"))
                                            .add("ranking",doc.getInteger("ranking"))
                                            .add("average",average)
                                            .add("users_rated",doc.getInteger("users_rated"))
                                            .add("url",doc.getString("image"))
                                            .add("timestamp",LocalDateTime.now().toString())
                                            .build();
        return new ResponseEntity<>(gameDetailsJson.toString(),HttpStatus.OK);
    }

}

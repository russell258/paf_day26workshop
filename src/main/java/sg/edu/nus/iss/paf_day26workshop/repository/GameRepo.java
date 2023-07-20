package sg.edu.nus.iss.paf_day26workshop.repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.paf_day26workshop.model.Game;

@Repository
public class GameRepo {
    
    @Autowired
    MongoTemplate template;

    //write query to list all games
    public List<Game> listAllGames(Integer offset, Integer limit){
        Criteria c = Criteria.where("");

        //write query (requires criteria parameter)
        /*
         * db.games.find().limit(?).skip(?).sort({gid:1})
         * 
         * .limit() set limit of generated query
         * .with() additional filter to sort
         * .with(Sort.by("gid")) to sort through by ascending order for "gid" field
         * use .skip() for offset to "skip" fields
         */
        
         Query q = Query.query(c).limit(limit).skip(offset).with(Sort.by("gid"));

         // (query, class type, collection name)
         List<Document> list = template.find(q, Document.class,"game");

         List<Game> gameList = new ArrayList<>();

         //loop through first list of all document objects with all attributes, then create new objects
         // with only the required attributes and add to another list to return

         for (Document d: list){
            //create new object that retrieves only gid and name from mongodb
            // note: attribute name has to match mongodb

            Game game = new Game(d.getInteger("gid"), d.getString("name"));
            gameList.add(game);
         }
         return gameList;

    }

    // db.games.find().count()
    //write query to count total number of games
    public long countGames(){
        Criteria c = Criteria.where("");

        Query q = Query.query(c);

        return template.count(q,"game");
    }

    // db.games.find("").limit(1).skip(1).sort({rank:1})
    //write query to list all games by rank
    public List<Game> getGamesByRank(Integer limit, Integer offset){
        Criteria c = Criteria.where("");
        Query q = Query.query(c).limit(limit).skip(offset).with(Sort.by("ranking"));

        List<Document> list = template.find(q,Document.class,"game");

        List<Game> gameList = new ArrayList<>();

        for (Document d: list){
            Game game = new Game(d.getInteger("gid"),d.getString("name"));
            gameList.add(game);
        }

        return gameList;
    }

    // create query for returning game details based on id
    // db.games.find({"gid":?})
    public Document getGameById(Integer id){
        // .is --> match the field we want to check in mongo
        Criteria c = Criteria.where("gid").is(id);
        Query q = Query.query(c);

        //template.findOne -> return 1 result
        return template.findOne(q,Document.class,"game");
    }

    


}

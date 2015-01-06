package controllers;

import models.Card;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.cardpage;


public class Cards extends Controller {


    public static Result getCard(long mid) {
        return ok(cardpage.render(Card.findbyMultiverseId(mid)));
    }
}

// jackson JSON tutorial
//http://www.journaldev.com/2324/jackson-json-processing-api-in-java-example-tutorial
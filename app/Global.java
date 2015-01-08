import models.Card;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.SimpleResult;
import utilities.JsonUtil;
import views.html.dashboard;
import views.html.error;

import java.io.IOException;

import static play.mvc.Results.notFound;

public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app) {
        // Check if the cards database is empty
        if (Card.find.findRowCount() == 0) {
            try {
                JsonUtil.loadCardsDB("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(User.findUserByUsername("admin")==null) {
            User admin = new User("admin", "admin", "admin@admin.pt", "admin");
            admin.city = "Porto";
            admin.save(true);
        }
    }
/*
    @Override
    public Promise<Result> onHandlerNotFound(RequestHeader request) {
        return Results.notFound(error.render());
    }

    @Override
    public Result onError(RequestHeader request, Throwable t) {
        return Results.internalServerError(error.render());
    }
    */
}
import models.Card;
import models.User;
import play.Application;
import play.GlobalSettings;
import utilities.JsonUtil;

import java.io.IOException;

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
}
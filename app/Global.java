import models.Card;
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
    }
}
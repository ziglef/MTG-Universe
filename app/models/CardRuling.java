package models;

import java.util.Date;

public class CardRuling {

    private Date date;
    private String text;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CardRuling(Date date, String text) {

        this.date = date;
        this.text = text;
    }
}

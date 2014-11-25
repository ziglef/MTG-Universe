package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Table(name = "cardcolors")
public class CardColor extends Model implements Serializable {

    // Finds a color
    public static Model.Finder<String, CardColor> find = new Model.Finder<>(String.class, CardColor.class);


    // Finds all cards containing a given string
    public static ArrayList<CardColor> findCardColors(String name) {

        ArrayList<CardColor> cardColors = new ArrayList<>(find.where().icontains("colorname", name).findList());

        return cardColors.size() > 0 ? cardColors : null;
    }

    // Cardcolor id on our database
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long cardcolorid;

    public String colorname;

    public CardColor(String color){
        this.colorname = color;
        this.save();
    }

    public static CardColor getCardColor(String cname) {
        CardColor c = find.where().eq("colorname",cname).findUnique();
        return c == null ? new CardColor(cname) : c;
    }
}

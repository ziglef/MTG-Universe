package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "cardcolors")
public class CardColor {

    // Finds all the cards in the database
    public static Model.Finder<String, CardNames> find = new Model.Finder<>(String.class, CardNames.class);

    // Finds all cards containing a given string
    public static ArrayList<CardNames> findCardNames(String name){

        ArrayList<CardNames> cardNames = new ArrayList<>(find.where().icontains("name", name).findList());

        return cardNames.size() > 0 ? cardNames : null;
    }

    // Card id on our database
    @Id
    public Integer id;

    // Card layout
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CCOLORS_ID")
    public Card cColors;

    public String color;

    public CardColor(String name){
        this.color = color;
    }
}

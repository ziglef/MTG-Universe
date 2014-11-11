package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Table(name = "cardnames")
public class CardNames extends Model implements Serializable{

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
    @ManyToOne
    @JoinColumn(name = "CNAMES_ID")
    public Card cNames;

    public String name;

    public CardNames(String name){
        this.name = name;
        this.save();
    }
}

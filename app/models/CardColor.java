package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Table(name = "cardcolors")
public class CardColor extends Model implements Serializable {

    // Finds all the cards in the database
    public static Model.Finder<String, CardColor> find = new Model.Finder<>(String.class, CardColor.class);

    // Finds all cards containing a given string
    public static ArrayList<CardColor> findCardColors(String name){

        ArrayList<CardColor> cardColors = new ArrayList<>(find.where().icontains("color", name).findList());

        return cardColors.size() > 0 ? cardColors : null;
    }

    // Card id on our database
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cardcolorid")
    public Long cardcolorid;

    // Card layout
    @ManyToOne
    @JoinColumn(name = "cardidfk")
    public Card card;

    public String color;

    public CardColor(String color){
        this.color = color;
        this.save();
    }
}

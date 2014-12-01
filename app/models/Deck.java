package models;

import models.enums.Visibility;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "deck")
public class Deck extends AbstractSetofCards {

    public Deck(String name, User owner, Visibility vis) {
        super(name, owner, vis);
    }

    @Override
    public void removeCard(Integer id) {

    }

    public static Deck create(String name, Integer owner_id, Visibility vis) {
        Deck deck = new Deck(name, User.find.byId(owner_id), vis);
        deck.save();
        return deck;
    }
}

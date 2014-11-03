package models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "deck")
public class Deck extends AbstractSetofCards {

    public Deck(String name, User owner) {
        super(name, owner);
    }

    public static Deck create(String name, Integer owner_id) {
        Deck deck = new Deck(name, User.find.byId(owner_id));
        deck.save();
        return deck;
    }
}

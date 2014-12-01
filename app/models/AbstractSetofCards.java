package models;

import models.enums.Visibility;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
//@Table(name = "Collection")
@MappedSuperclass
public abstract class AbstractSetofCards extends Model implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    public Integer id;

    public String name;

    @ManyToMany(cascade=CascadeType.ALL)
   // @JoinTable(name="cards_collection")
    public List<Card> cards = new ArrayList<>();

    @ManyToOne
    public User owner;

    public Visibility visibility;

    public AbstractSetofCards(String name, User owner, Visibility vis) {
        this.name=name;
        this.owner=owner;
        this.visibility=vis;
    }

    //create a collection and add to database
    /*public static GenericCollection create(String name, Integer owner_id) {
        GenericCollection collection = new GenericCollection(name, User.find.byId(owner_id));
        collection.save();
        return collection;
    }*/

    // rename collection
    /*public static String rename(Integer collectionId, String newName) {
        AbstractSetofCards collection = find.ref(collectionId);
        collection.name = newName;
        collection.update();
        return newName;
    }*/

    public void rename(String newName) {
        this.name=newName;
        this.save();
    }

    // TODO delete a collection

    public void addCard(Card c) {
        cards.add(c);
       // this.save();
    }

    public abstract void removeCard(Integer id);
}

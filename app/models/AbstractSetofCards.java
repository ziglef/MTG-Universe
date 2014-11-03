package models;

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

    public AbstractSetofCards(String name, User owner) {
        this.name=name;
        this.owner=owner;
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

    // find all collections of some user
    public static List<AbstractSetofCards> findUserCollections(String username) {
        return find.where().eq("owner.username", username).findList();
    }

    public static Finder<Integer, AbstractSetofCards> find = new Model.Finder<>(Integer.class, AbstractSetofCards.class);

    public void addCard(Card c) {
        cards.add(c);
        this.save();
    }
}

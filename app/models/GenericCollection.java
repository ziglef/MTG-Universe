package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@MappedSuperclass
@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Collection")
public class GenericCollection extends Model implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    protected Integer id;

    public String name;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="cards_collection")
    protected List<Card> cards = new ArrayList<Card>();

    @ManyToOne
    protected User owner;

    public GenericCollection(String name, User owner) {
        this.name=name;
        this.owner=owner;
    }

    //create a collection and add to database
    public static GenericCollection create(String name, String owner_username) {
        GenericCollection collection = new GenericCollection(name, User.findUserByUsername(owner_username));
        collection.save();

        return collection;
    }

    // rename collection
    public static String rename(Integer collectionId, String newName) {
        GenericCollection collection = find.ref(collectionId);
        collection.name = newName;
        collection.update();
        return newName;
    }

    // TODO delete a collection



    // find all collections of some user
    public static List<GenericCollection> findUserCollections(String username) {
        return find.where().eq("owner.username", username).findList();
    }

    public static Finder<Integer, GenericCollection> find = new Model.Finder<>(Integer.class, GenericCollection.class);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public void addCard(Card c) { cards.add(c); }
}

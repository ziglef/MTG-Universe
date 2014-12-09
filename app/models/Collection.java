

        package models;

        import models.enums.Visibility;
        import play.db.ebean.Model;

        import javax.persistence.*;
        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.List;

@Entity
public class Collection extends Model implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    public Integer id;

    public String name;

    /* co - collection
    * de - deck
    * tl - tradelist
    * wl - wantlist
    */
    public String coltype;

    @ManyToMany(cascade=CascadeType.ALL)
    // @JoinTable(name="cards_collection")
    public List<Card> cards = new ArrayList<>();

    @ManyToOne
    public User owner;

    public Visibility visibility;

    public Collection(String name, User owner, Visibility vis) {
        this.name=name;
        this.owner=owner;
        this.visibility=vis;
    }

    public void removeCard(Integer id) {
        this.cards.remove(Card.find.byId(Integer.toString(id)));
    }

    public void rename(String newName) {
        this.name=newName;
        this.save();
    }

    public static Collection create(String name, Integer owner_id, Visibility vis) {
        Collection collection = new Collection(name, User.find.byId(owner_id), vis);
        collection.save();
        return collection;
    }

    public void addCard(Card c) {
        cards.add(c);
    }

    public static Finder<Integer, Collection> find = new Model.Finder<>(Integer.class, Collection.class);

    // find all collections of some user
    public static List<Collection> findUserCollections(Integer userId) {
        return find.where().eq("owner.id", userId).findList();
    }

    public static Collection findCollectionByID(Integer id){
        return find.where().eq("id", id).findUnique();
    }

    public static List<Card> findCollectionCards(Integer id){
        Collection c = find.where().eq("id", id).findUnique();
        return c.cards;
    }

}
package models;

import models.enums.Visibility;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
<<<<<<< HEAD
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
=======
@Table(name = "collection")
public class Collection extends AbstractSetofCards {
>>>>>>> parent of 114eece... Merge branch 'develop' of https://bitbucket.org/Ziglef/ldso-turma-1-2014-2015 into develop

    public Collection(String name, User owner, Visibility vis) {
        super(name, owner, vis);
    }

    @Override
    public void removeCard(Integer id) {
        this.cards.remove(Card.find.byId(Integer.toString(id)));
    }

    public static Collection create(String name, Integer owner_id, Visibility vis) {
        Collection collection = new Collection(name, User.find.byId(owner_id), vis);
        collection.save();
        return collection;
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
        //c.addCard(Card.findCardsByName("Lightning Bolt").get(0));
        return c.cards;
    }

}

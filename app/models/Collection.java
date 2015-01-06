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

    public Collection(String name, User owner, Visibility vis, String type) {
        this.name=name;
        this.owner=owner;
        this.visibility=vis;
        this.coltype = type;
    }

    public void removeCard(Integer id) {
        this.cards.remove(Card.find.byId(Integer.toString(id)));
    }

    public void rename(String newName) {
        this.name=newName;
        this.save();
    }

    public static Collection createCollection(String name, Integer owner_id, Visibility vis, String type) {
        Collection collection = new Collection(name, User.find.byId(owner_id), vis, type);
        collection.save();
        return collection;
    }
    /*
    public static Collection createDeck(String name, Integer owner_id, Visibility vis) {
        Collection collection = new Collection(name, User.find.byId(owner_id), vis, "de");
        collection.save();
        return collection;
    }
    public static Collection createTradeList(String name, Integer owner_id, Visibility vis) {
        Collection collection = new Collection(name, User.find.byId(owner_id), vis, "tl");
        collection.save();
        return collection;
    }
    public static Collection createWantList(String name, Integer owner_id, Visibility vis) {
        Collection collection = new Collection(name, User.find.byId(owner_id), vis, "wl");
        collection.save();
        return collection;
    }
    */

    public void addCard(Card c) {
        cards.add(c);
    }

    public static Finder<Integer, Collection> find = new Model.Finder<>(Integer.class, Collection.class);

    // find all collections of some user
    public static List<Collection> findUserCollections(Integer userId) {
        return find.where().and(
                com.avaje.ebean.Expr.eq("owner.id", userId),
                com.avaje.ebean.Expr.eq("coltype", "co")
        ).findList();
        //eq("owner.id", userId).and("coltype", "co").findList();
    }
    // find all decks of some user
    public static List<Collection> findUserDecks(Integer userId) {
        return find.where().and(
                com.avaje.ebean.Expr.eq("owner.id", userId),
                com.avaje.ebean.Expr.eq("coltype", "de")
        ).findList();
    }
    // find all wantlists of some user
    public static List<Collection> findUserWantLists(Integer userId) {
        return find.where().and(
                com.avaje.ebean.Expr.eq("owner.id", userId),
                com.avaje.ebean.Expr.eq("coltype", "wl")
        ).findList();
    }
    // find all tradelists of some user
    public static List<Collection> findUserTradeLists(Integer userId) {
        return find.where().and(
                com.avaje.ebean.Expr.eq("owner.id", userId),
                com.avaje.ebean.Expr.eq("coltype", "tl")
        ).findList();
    }

    public static Collection findCollectionByID(Integer id){
        return find.where().eq("id", id).findUnique();
    }

    public static List<Card> findCollectionCards(Integer id){
        Collection c = find.where().eq("id", id).findUnique();
        return c.cards;
    }

    public boolean containsCard(Card newcard) {
        return cards.contains(newcard);
    }
}
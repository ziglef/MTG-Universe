package models;

import models.enums.Visibility;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "collection")
public class Collection extends AbstractSetofCards {

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

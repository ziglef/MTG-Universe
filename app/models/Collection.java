package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "collection")
public class Collection extends AbstractSetofCards {

    public Collection(String name, User owner) {
        super(name, owner);
    }

    public static Collection create(String name, Integer owner_id) {
        Collection collection = new Collection(name, User.find.byId(owner_id));
        collection.save();
        return collection;
    }

    public static Finder<Integer, Collection> find = new Model.Finder<>(Integer.class, Collection.class);

    // find all collections of some user
    public static List<Collection> findUserCollections(Integer userId) {
        return find.where().eq("owner.id", userId).findList();
    }
}

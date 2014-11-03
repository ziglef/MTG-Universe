package models;

import javax.persistence.Entity;
import javax.persistence.Table;

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

}

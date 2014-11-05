package models;

        import models.enums.Visibility;

        import javax.persistence.Entity;
        import javax.persistence.Table;

@Entity
@Table(name = "wishlist")
public class WishList extends AbstractSetofCards {

    public WishList(String name, User owner, Visibility vis) {
        super(name, owner, vis);
    }

    public static WishList create(String name, Integer owner_id, Visibility vis) {
        WishList wlist = new WishList(name, User.find.byId(owner_id), vis);
        wlist.save();
        return wlist;
    }
}

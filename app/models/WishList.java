package models;

        import javax.persistence.Entity;
        import javax.persistence.Table;

@Entity
@Table(name = "wishlist")
public class WishList extends AbstractSetofCards {

    public WishList(String name, User owner) {
        super(name, owner);
    }

    public static WishList create(String name, Integer owner_id) {
        WishList wlist = new WishList(name, User.find.byId(owner_id));
        wlist.save();
        return wlist;
    }
}

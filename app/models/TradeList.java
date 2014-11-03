package models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tradelist")
public class TradeList extends AbstractSetofCards {

    public TradeList(String name, User owner) {
        super(name, owner);
    }

    public static TradeList create(String name, Integer owner_id) {
        TradeList tlist = new TradeList(name, User.find.byId(owner_id));
        tlist.save();
        return tlist;
    }
}

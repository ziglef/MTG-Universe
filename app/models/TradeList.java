package models;

import models.enums.Visibility;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tradelist")
public class TradeList extends AbstractSetofCards {

    public TradeList(String name, User owner, Visibility vis) {
        super(name, owner, vis);
    }

    public static TradeList create(String name, Integer owner_id, Visibility vis) {
        TradeList tlist = new TradeList(name, User.find.byId(owner_id), vis);
        tlist.save();
        return tlist;
    }
}

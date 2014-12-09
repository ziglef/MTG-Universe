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
    public long id;

    public String name;

    //references:
    //http://en.wikibooks.org/wiki/Java_Persistence/ManyToMany
    //https://giannigar.wordpress.com/2009/09/04/mapping-a-many-to-many-join-table-with-extra-column-using-jpa/

    //@ManyToMany(cascade=CascadeType.ALL)
    // @JoinTable(name="cards_collection")
    @OneToMany//(mappedBy="collection", cascade=CascadeType.ALL)  //@OneToMany(cascade=CascadeType.PERSIST) //(mappedBy="collection")
    public List<CollectionCard> collectionCards = new ArrayList<CollectionCard>();

    @ManyToOne
    public User owner;

    public Visibility visibility;

    public Collection(String name, User owner, Visibility vis) {
        this.name=name;
        this.owner=owner;
        this.visibility=vis;
    }

    public List<Card> getCards() {
        ArrayList<Card> result = new ArrayList<>();
        for(CollectionCard cc: collectionCards)
            result.add(cc.getCard());
        return result;
    }

    public void removeCard(Integer id) {
        //TODO decrement unity - if 1, remove connection
        this.collectionCards.remove(Card.find.byId(Integer.toString(id)));
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
        List<Card> cards = new ArrayList<>();

        for(CollectionCard cc: c.collectionCards)
            cards.add(cc.getCard());
        return cards;
    }
/*
    public void addEmployee(Employee employee, boolean teamLead) {
        ProjectAssociation association =new ProjectAssociation();
        association.setEmployee(employee);
        association.setProject(this);
        association.setEmployeeId(employee.getId());
        association.setProjectId(this.getId());
        association.setIsTeamLead(teamLead);
        employees.add(association);
    }
*/
    public void addCard(Card c) {
        //TODO verify if connection exists, if so add 1
        CollectionCard cc = new CollectionCard();
        cc.setCard(c);
        cc.setCollection(this);
        //cc.setCardId(c.id);
        //cc.setCollectionId(this.id);
        int q = 1;
        cc.setQuantity(q);
        //verificar quantidade
        cc.save();
        collectionCards.add(cc);
        this.save();
    }
}
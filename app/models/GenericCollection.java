package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

//@MappedSuperclass
@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Collection")
public class GenericCollection extends Model implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    protected Integer id;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="cards_collection")
    protected List<Card> cards;

    @OneToOne
    protected User owner;

    public static Finder<Integer, GenericCollection> find = new Model.Finder<>(Integer.class, GenericCollection.class);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void addCard(Card c) { cards.add(c); }
}

package models;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "Sets")
public class Set {

    // Set id on our database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    public Integer id;

    @Column(name = "name")
    private String	name;

    public String getName() {
        return name;
    }

    @Column(name = "code")
    private String	code;

    @Column(name = "releaseDate")
    private String	releaseDate;

    @Column(name = "border")
    private String	border;

    @Column(name = "type")
    private String	type;

    @Column(name = "block")
    private String	block;

    @Column(name = "gathererCode")
    private String	gathererCode;

    @Column(name = "cards")
    private ArrayList<Card> cards;

    public ArrayList<Card> getCards() {
        return cards;
    }
}

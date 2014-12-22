package models;

import com.avaje.ebean.Ebean;
import models.enums.Layout;

import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// The model for a single card (current version uses the simple .json file)

@Entity
@Table(name = "Cards")
public class Card extends Model implements Serializable, Comparable{

    // Finds all cards containing a given string
    public static ArrayList<Card> findCardsByName(String name){

        ArrayList<Card> cards = new ArrayList<>(find.where().icontains("name", name).findPagingList(200).getAsList());


        return cards.size() > 0 ? cards : null;
    }

    // Finds all cards
    public static ArrayList<Card> findAll(){

        ArrayList<Card> cards = new ArrayList<>(find.all());

        return cards.size() > 0 ? cards : null;
    }

    // Finds all the cards in the database
    public static Finder<String, Card> find = new Model.Finder<>(String.class, Card.class);

    // Card id on our database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    public Integer id;

    // Card layout
    @Column(name = "layout")
    public String layout;

    // Card name
    @Column(name = "name")
    public String name;

    // Card names (if it has multiple names, optional)
    @Column(name = "names")
    public ArrayList<String> names; //FIXME

    // Card mana cost
    @Column(name = "manaCost")
    public String manaCost;

    // Card converted mana cost
    @Column(name = "cmc")
    public Integer cmc;

    // Card color(s)
    @Column(name = "colors")
    public ArrayList<String> colors; //FIXME

    // Card type
    @Column(name = "type")
    public String type;

    // Card supertype(s)
    @Column(name = "supertypes")
    public ArrayList<String> supertypes; //FIXME

    // Card types
    @Column(name = "types")
    public ArrayList<String> types; //FIXME

    // Card subtypes
    @Column(name = "subtypes")
    public ArrayList<String> subtypes; //FIXME

    // Card rarity
    @Column(name = "rarity")
    public String rarity;

    // Card text
    @Column(name = "text", columnDefinition = "TEXT(1023)")
    public String text;

    // Card flavor text
    @Column(name = "flavor", columnDefinition = "TEXT(1023)")
    public String flavor;

    // Card artist
    @Column(name = "artist")
    public String artist;

    // Card number
    @Column(name = "number")
    public String number;

    // Card power
    @Column(name = "power")
    public String power;

    // Card toughness
    @Column(name = "toughness")
    public String toughness;

    // Card loyalty
    @Column(name = "loyalty")
    public Integer loyalty;

    // Card multiverseid
    @Column(name = "multiverseid")
    public Integer multiverseid;

    // Card variations (doesnt include its own multiverseid)
    @Column(name = "variations")
    public ArrayList<Integer> variations; //FIXME

    // Card image name (for use with mtgimage.com)
    @Column(name = "imageName")
    public String imageName;

    // Card watermark
    @Column(name = "watermark")
    public String watermark;

    // Card border
    @Column(name = "border")
    public String border;

    // Card timeshifted
    @Column(name = "timeshifted")
    public String timeshifted;

    // Card hand size modifier (vanguard)
    @Column(name = "hand")
    public Integer hand;

    // Card life modifier (vanguard)
    @Column(name = "life")
    public Integer life;

    // Card reserved status
    @Column(name = "reserved")
    public String reserved;

    // Card release date (promo cards)
    @Column(name = "releaseDate")
    public String releaseDate;

    // EXTRA FIELDS //

    // Card rulings
    @Column(name = "rulings")
    public ArrayList<HashMap<String, String>> rulings; //FIXME

    // Card foreign names
    @Column(name = "foreignNames")
    public ArrayList<HashMap<String, String>> foreignNames; //FIXME

    // Card printings (in which sets the card was print on)
    @Column(name = "printings")
    public ArrayList<String> printings; //FIXME

    // Card original text
    @Column(name = "originalText", columnDefinition = "TEXT(1023)")
    public String originalText;

    // Card original type
    @Column(name = "originalType")
    public String originalType;

    // Will hash map work? //
    // Card legalities
    @Column(name = "legalities")
    public HashMap<String, String> legalities; //FIXME

    // Card source (where promo cards could be obtained)
    @Column(name = "source")
    public String source;


    // not necessary
    // Collections on which the card is
    //@ManyToMany(cascade=CascadeType.ALL)
    // @JoinTable(name="cards_collection")
    // List<GenericCollection> collection = new ArrayList<>();

    @Override
    public int hashCode(){
        return 0;
    }

    @Override
    public boolean equals(Object o){
        if(o.getClass() != this.getClass())
            return false;

        return this.name.equalsIgnoreCase(((Card)o).name);
    }

    @Override
    public int compareTo(Object o) {
        if(o.getClass() != this.getClass())
            return -1;

        return this.name.compareToIgnoreCase(((Card)o).name);
    }
}

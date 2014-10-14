package models;

import com.avaje.ebean.Ebean;
import models.enums.Layout;

import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

// The model for a single card (current version uses the simple .json file)

@Entity
@Table(name = "Cards")
public class Card extends Model implements Serializable{

    // Finds all cards containing a given string
    public static ArrayList<Card> findCardsByName(String name){

        ArrayList<Card> cards = new ArrayList<Card>(Ebean.find(Card.class)
                .where()
                    .contains("name", name)
                .findList());

        return cards.size() > 0 ? cards : null;
    }

    // Finds all the cards in the database
    public static Finder<Integer, Card> find = new Model.Finder<>(Integer.class, Card.class);

    // Card id on our database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    public Integer id;

    // Card layout
    @Column(name = "layout")
    public Layout layout;

    // Card name
    @Column(name = "name")
    public String name;

    // Card names (if it has multiple names, optional)
    @Column(name = "names")
    public ArrayList<String> names;

    // Card mana cost
    @Column(name = "manaCost")
    public String manaCost;

    // Card converted mana cost
    @Column(name = "cmc")
    public Float cmc;

    // Card color(s)
    @Column(name = "colors")
    public ArrayList<String> colors;

    // Card type
    @Column(name = "type")
    public String type;

    // Card supertype(s)
    @Column(name = "supertypes")
    public String supertypes;

    // Card types
    @Column(name = "types")
    public String types;

    // Card subtypes
    @Column(name = "subtypes")
    public String subtypes;

    // Card rarity
    @Column(name = "rarity")
    public String rarity;

    // Card text
    @Column(name = "text")
    public String text;

    // Card flavor text
    @Column(name = "flavor")
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
    public List<Integer> variations;

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
    public List<CardRuling> rulings;

    // Card foreign names
    @Column(name = "foreignNames")
    public String foreignNames;

    // Card printings (in which sets the card was print on)
    @Column(name = "printings")
    public List<String> printings;

    // Card original text
    @Column(name = "originalText")
    public String originalText;

    // Card original type
    @Column(name = "originalType")
    public String originalType;

    // Will hash map work? //
    // Card legalities
    @Column(name = "legalities")
    public HashMap<String, String> legalities;

    // Card source (where promo cards could be obtained)
    @Column(name = "source")
    public String source;



}

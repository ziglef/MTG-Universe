package models;

import models.enums.Layout;

import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

// The model for a single card (current version uses the simple .json file)

@Entity
@Table(name = "Cards")
public class Card extends Model implements Serializable{

    // Card id on our database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    public Integer id;

    // Card layout
    @Basic(optional = false)
    @Column(name = "layout")
    public Layout layout;

    // Card name
    @Basic(optional = false)
    @Column(name = "name")
    public String name;

    // Card names (if it has multiple names, optional)
    @Column(name = "names")
    public ArrayList<String> names;

    // Card mana cost
    @Basic(optional = false)
    @Column(name = "manaCost")
    public String manaCost;

    // Card converted mana cost
    @Basic(optional = false)
    @Column(name = "cmc")
    public Float cmc;

    // Card color(s)
    @Basic(optional = false)
    @Column(name = "colors")
    public ArrayList<String> colors;

    // Card type
    @Basic(optional = false)
    @Column(name = "type")
    public String type;

    // Card supertype(s)
    @Basic(optional = false)
    @Column(name = "supertypes")
    public String supertypes;

    // Card types
    @Basic(optional = false)
    @Column(name = "types")
    public String types;

    // Card subtypes
    @Basic(optional = false)
    @Column(name = "subtypes")
    public String subtypes;

    // Card rarity
    @Basic(optional = false)
    @Column(name = "rarity")
    public String rarity;


}

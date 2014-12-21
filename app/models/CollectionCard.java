package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="COLLECTION_CARDS")
@IdClass(CollectionCardId.class)
public class CollectionCard extends Model {

   // public static Finder<String, CollectionCard> find = new Model.Finder<>(String.class, CollectionCard.class);

    @Id
    @ManyToOne
    @JoinColumn(name="COLLECTION_PK1")
    public Collection collection;

    @Id
    @ManyToOne
    @JoinColumn(name="CARD_PK1")
    public Card card;

    @Column(name="QUANTITY")
    public int quantity;

    /*
    @ManyToOne
    //@JoinColumn(name="collection_id")
    @PrimaryKeyJoinColumn(name="COLLECTIONID", referencedColumnName="ID")
    public Collection collection;

    @ManyToOne
   // @JoinColumn(name="card_id")
    @PrimaryKeyJoinColumn(name="CARDID", referencedColumnName="ID")
    public Card card;
    */

    //public CollectionCard() {
      //  this.collection=col;
      //  this.card=card;
      //  this.quantity=1;
      //  this.save();
    //}
/*
    public void setCollection(Collection c) {
        collection = c;
    }

    public void setCard(Card c) {
        card = c;
    }

    public Card getCard() {
        return card;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public void setCollectionId(long collectionId) {
        this.collectionId = collectionId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    */
}

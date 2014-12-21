package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class CollectionCardId extends Model implements Serializable {

    @Column(name="COLLECTION_PK1")
    public Long collectionId;

    @Column(name="CARD_PK1")
    public Long cardId;
}
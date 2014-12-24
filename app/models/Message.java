package models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.ebean.Model;

@Entity
@Table(name = "Messages")
public class Message extends Model implements Serializable {
	
	public static Finder<Integer, Message> find = new Model.Finder<>(Integer.class, Message.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	public Integer id;
	public boolean read;
	
	@Column(columnDefinition = "TEXT(3000)")
	public String content;
	
	public String subject;
	public long date;
	public String dateStr;
	
	@ManyToOne
	public User from;
	
	@ManyToOne
	public User to;
   
	public Message(User from, User to, String content, String subject) {
		this.from = from;
		this.to = to;
		this.content = content;
		this.subject = subject;
		this.read = false;
		this.date = System.currentTimeMillis();
		
		// Date str
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		this.dateStr = dateFormat.format(this.date);
	}
	
    public static class MessageArray {
        public String name, dateStr;
        public Boolean read;
        public List<Message> list;
    }
	
    // find all messages of some user (group by username)
    public static List<MessageArray> findAllUserMessage(String username) {
    	
    	List<Message> list = find.where().or(
    	        com.avaje.ebean.Expr.eq("from.username", username),
    	        com.avaje.ebean.Expr.eq("to.username",  username)
    	)
    	.orderBy("date DESC")
    	.findList(); 
    	
    	// Object
    	List<MessageArray> messages = new ArrayList<MessageArray>();
    	   	
    	// Organize
    	for(int i=0; i<list.size(); i++)
    	{
    		MessageArray obj = null;    
    		Message currentMsg = list.get(i);
    		String otherUser = (currentMsg.from.username.equals(username)) ? currentMsg.to.username : currentMsg.from.username;
    		
    		// Find if already exists
    		for(int j=0; j<messages.size(); j++)
    		{
    			if ( messages.get(j).name.equals(otherUser) )
    			{
    				obj = messages.get(j);
    				break;
    			}
    		}
    		
    		// Create new object
    		if ( obj == null )
			{
    			obj = new MessageArray();
    			obj.name = otherUser;
    			obj.list = new ArrayList<>();
    			obj.read = true;
    			obj.dateStr = currentMsg.dateStr;
    			messages.add(obj);
			}
    		    		
    		// Set unread (only messages to me)
    		if ( !currentMsg.read && currentMsg.to.username.equals(username) ) obj.read = false;
    		
    		// Add message
    		obj.list.add(currentMsg);
    	}
    	    	
        return messages;
    }
    
    // find messages with another user
    public static List<Message> findAUserMessage(String username, String other) {
    	
    	// Set read
    	List<Message> list = find.where().and(
			com.avaje.ebean.Expr.eq("from.username", other),
			com.avaje.ebean.Expr.eq("to.username", username)
		).findList();
    	
    	for( Message m : list ) {
    		m.read = true;
    		m.save();
    	}
    	
    	// Return messages
    	return find.where().or(
			com.avaje.ebean.Expr.and(
					com.avaje.ebean.Expr.eq("from.username", username),
					com.avaje.ebean.Expr.eq("to.username", other)
			),
			com.avaje.ebean.Expr.and(
					com.avaje.ebean.Expr.eq("from.username", other),
					com.avaje.ebean.Expr.eq("to.username", username)
			)
    	)
    	.orderBy("date ASC")
    	.findList(); 
    }
   
}

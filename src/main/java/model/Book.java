package model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class Book {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Integer idbooks;

  public Integer getIdbooks() {
	return idbooks;
}

public void setIdbooks(Integer idbooks) {
	this.idbooks = idbooks;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getAuthor() {
	return author;
}

public void setAuthor(String author) {
	this.author = author;
}

public String getBetweeness() {
	return betweeness;
}

public void setBetweeness(String betweeness) {
	this.betweeness = betweeness;
}

private String name;

  private String author;
  
  private String betweeness;



}

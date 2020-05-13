package Models;

import javax.persistence.*;
import javax.swing.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class CheckInOut {
    @Id @GeneratedValue
    private int id;
    @OneToOne
    private Book book;
    @OneToOne
    private Client client;
    @Temporal(TemporalType.DATE)
    private Date date;
    public CheckInOut(){}

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CheckInOut(Book book, Client client,Date date){
        this.book=book;
        this.client=client;
        this.date=date;

    }



    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}

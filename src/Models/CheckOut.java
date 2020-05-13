package Models;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.util.Date;


@Entity
public class CheckOut extends CheckInOut{
    public CheckOut (){
        super();
    }
    public CheckOut(Book book, Client client, Date date) {
        super(book, client,date);
    }
}

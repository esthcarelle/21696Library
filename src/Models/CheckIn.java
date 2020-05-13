package Models;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class CheckIn extends CheckInOut {
    public CheckIn(){
        super();
    }

    public CheckIn(Book book, Client client, Date date) {
        super(book, client,date);

    }

}

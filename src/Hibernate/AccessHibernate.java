package Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class AccessHibernate {
    Session session;
    private SessionFactory sessionFactory=new Configuration().configure("Hibernate/hibernate.cfg.xml").buildSessionFactory();

    public Session openSession(){
        session=sessionFactory.openSession();
        return session;
    }
    public void closeConnection(Session sessionn){
        sessionn.beginTransaction().commit();
        sessionn.close();
    }
}

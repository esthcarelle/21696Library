package Hibernate;

import Models.*;
import UI.LoginUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;

public class Main {
    public static void main (String... args) {
        SessionFactory sessionFactory = new Configuration().configure("Hibernate/hibernate.cfg.xml").buildSessionFactory();
        Session session = sessionFactory.openSession();

        try {


            LoginUser loginFrame = new LoginUser();
            loginFrame.setSize(600, 300);
            loginFrame.setVisible(true);
        } catch (Exception e) {
            {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            session.beginTransaction().commit();
            session.close();

        }
    }
}

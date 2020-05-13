package UI;


import Hibernate.AccessHibernate;
import Models.User;
import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginUser extends JDialog {
    private JPanel contentPane;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;
    ImageIcon imageIcon;

    public LoginUser() {
        setContentPane(contentPane);
        setModal(true);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                String email=textField1.getText();
                String password=passwordField1.getText();
                AccessHibernate accessHibernate=new AccessHibernate();
                Session session=accessHibernate.openSession();

                User user;

                user=new User("wilcarrelle@gmail.com",HashPassword("123456"));
                User userFound=session.get(User.class,"wilcarrelle@gmail.com");
                if(userFound==null) {
                    session.save(user);
                }
                user=session.get(User.class,email);

                if(user!=null){
                    if(checkPassword(password,user.getPassword())){
                        CrudBook crudBook=new CrudBook();
                        crudBook.setSize(700,700);
                        contentPane.setVisible(false);
                        crudBook.setVisible(true);
                    }
                    else
                        JOptionPane.showMessageDialog(contentPane,"Password not matching check again!");
                }
                else {
                    JOptionPane.showMessageDialog(contentPane,"Incorrect email");
                }
                accessHibernate.closeConnection(session);


            }
        });
    }

    private String HashPassword(String password){
        return BCrypt.hashpw(password,BCrypt.gensalt());

    }
    private boolean checkPassword(String password,String hashedPassword){
        if(BCrypt.checkpw(password,hashedPassword)){
            return true;
        }
        else
            return false;
    }


    public static void main(String[] args) {
        LoginUser dialog = new LoginUser();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}

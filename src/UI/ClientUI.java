package UI;

import Hibernate.AccessHibernate;
import Models.Book;
import Models.Client;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientUI extends JDialog {
    private JPanel contentPane;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton chooseImageButton;
    private JButton saveClientButton;
    private JTable table1;
    private JLabel image;
    String path;
    String[] columns={"First Name","Last Name","Phone number","Email","Photo"};
    Object[] row=new Object[5];

    AccessHibernate accessHibernate=new AccessHibernate();

    public ClientUI() {
        setContentPane(contentPane);
        setModal(true);

        Session session=accessHibernate.openSession();
        Query query=session.createQuery("from Client");
        DefaultTableModel defaultTableModel=new DefaultTableModel(columns,0)
        {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if(columnIndex == 4){
                    return Icon.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };
        List<Client> clients=query.list();
        for(int i=0;i<clients.size();i++){
            row[0]=clients.get(i).getFirstName();
            row[1]=clients.get(i).getLastName();
            row[2]=clients.get(i).getPhoneNumber();
            row[3]=clients.get(i).getEmailAddress();
            ImageIcon imageIcon=new ImageIcon(clients.get(i).getPhoto());
            imageIcon=new ImageIcon(imageIcon.getImage().getScaledInstance(50,30, Image.SCALE_SMOOTH));
            row[4]=imageIcon;

            defaultTableModel.addRow(row);
        }



        table1.setModel(defaultTableModel);

        session.close();


        chooseImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                JFileChooser file = new JFileChooser();

                FileNameExtensionFilter fnef = new FileNameExtensionFilter("png", "png","jpeg");
                file.addChoosableFileFilter(fnef);
                if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                    File chfile = file.getSelectedFile();
                    path= chfile.getAbsolutePath();
                    System.out.println(path);
                }
            }
        });

        saveClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(path==null){
                    JOptionPane.showMessageDialog(contentPane,"Please insert an image");
                }
                else{
                    Session session=accessHibernate.openSession();
                    Client client=new Client(textField1.getText(),textField2.getText(),Integer.parseInt(textField3.getText()),textField4.getText(),path);
                    session.save(client);
                    JOptionPane.showMessageDialog(contentPane,"Successfully saved");
                    DefaultTableModel defaultTableModel1= (DefaultTableModel) table1.getModel();;
                    row[0]=textField1.getText();
                    row[1]=textField2.getText();
                    row[2]=textField3.getText();
                    row[3]=textField4.getText();
                    ImageIcon imageIcon=new ImageIcon(path);
                    imageIcon=new ImageIcon(imageIcon.getImage().getScaledInstance(50,30, Image.SCALE_SMOOTH));
                    row[4]=imageIcon;

                    defaultTableModel1.addRow(row);
                    accessHibernate.closeConnection(session);
                }
                path=null;
            }
        });
    }
    public static void main(String[] args) {
        ClientUI dialog = new ClientUI();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}

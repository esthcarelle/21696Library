package UI;

import Hibernate.AccessHibernate;
import Models.Book;
import org.hibernate.Session;
import org.hibernate.query.Query;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

public class CrudBook extends JDialog {
    private JPanel contentPane;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton saveButton;
    private JTable table1;
    private JTextField id;
    private JTextField newTitle;
    private JTextField newAuthor;
    private JTextField newPubl;
    private JTextField newPages;
    private JButton updateButton;
    private JTextField bookId;
    private JButton deleteButton;
    private JButton checkInAndOutButton;
    private JButton addClientButton;
    String[] columns={"id","title","author","Publishing house","pages"};
    Object[] row=new Object[6];

    public CrudBook()  {
        setContentPane(contentPane);
        AccessHibernate accessHibernate=new AccessHibernate();
        Session session=accessHibernate.openSession();
         Query query=session.createQuery("from books");
        DefaultTableModel defaultTableModel=new DefaultTableModel(columns,0);
        List<Book> books=query.list();
        for(int i=0;i<books.size();i++){
            row[0]=books.get(i).getBookId();
            row[1]=books.get(i).getTitle();
            row[2]=books.get(i).getPublishingHouse();
            row[3]=books.get(i).getAuthor();
            row[4]=books.get(i).getPages();
            defaultTableModel.addRow(row);
        }


       table1.setModel(defaultTableModel);

        session.close();

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(!isInteger(textField4.getText())) {
                    JOptionPane.showMessageDialog(contentPane, "Pages must be numbers");
                    return;
                }
                    Session session = accessHibernate.openSession();
                    Book book = new Book(textField1.getText(), textField2.getText(), textField3.getText(), Integer.parseInt(textField4.getText()));
                    session.save(book);

                    DefaultTableModel defaultTableModel1=(DefaultTableModel) table1.getModel();

                    row[0]=book.getBookId();
                    row[1]=book.getTitle();
                    row[2]=book.getPublishingHouse();
                    row[3]=book.getAuthor();
                    row[4]=book.getPages();
                    defaultTableModel1.addRow(row);

                JOptionPane.showMessageDialog(contentPane,"Successfully saved");
                textField1.setText("");
                textField2.setText("");
                textField3.setText("");
                textField4.setText("");


                accessHibernate.closeConnection(session);
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!isInteger(newPages.getText())) {
                    JOptionPane.showMessageDialog(contentPane, "Pages must be numbers");
                    return;
                }
                Session session=accessHibernate.openSession();
                Book book;
                book=session.get(Book.class, Integer.parseInt(id.getText()));
                DefaultTableModel defaultTableModel=(DefaultTableModel) table1.getModel();
                if(book!=null) {
                    book.setTitle(newTitle.getText());
                    book.setPublishingHouse(newPubl.getText());
                    book.setPages(Integer.parseInt(newPages.getText()));
                    book.setAuthor(newAuthor.getText());

                    session.update(book);
                    JOptionPane.showMessageDialog(contentPane,"Successfully updated");
                    id.setText("");
                    newAuthor.setText("");
                    newPubl.setText("");
                    newPages.setText("");
                    newTitle.setText("");

                        for(int j=0;j<defaultTableModel.getRowCount();j++){
                            if(Integer.parseInt(defaultTableModel.getValueAt(j,0).toString())==book.getBookId()){
                                defaultTableModel.removeRow(j);

                                row[0]=book.getBookId();
                                row[1]=book.getTitle();
                                row[2]=book.getPublishingHouse();
                                row[3]=book.getAuthor();
                                row[4]=book.getPages();
                                defaultTableModel.addRow(row);
                            }
                        }
                }
                else
                    JOptionPane.showMessageDialog(contentPane,"The id was not found!Try with a valid id");

                accessHibernate.closeConnection(session);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                Session session=accessHibernate.openSession();

                Book book;
                book=session.get(Book.class, Integer.parseInt(bookId.getText()));
                if(book!=null) {
                    session.delete(book);
                    DefaultTableModel defaultTableModel = (DefaultTableModel) table1.getModel();
                    JOptionPane.showMessageDialog(contentPane,"Successfully deleted");
                    bookId.setText("");
                    for(int j=0;j<defaultTableModel.getRowCount();j++) {
                        if (Integer.parseInt(defaultTableModel.getValueAt(j, 0).toString()) == book.getBookId()) {
                            defaultTableModel.removeRow(j);
                        }
                    }

                }
                else
                    JOptionPane.showMessageDialog(contentPane,"The id was not found!Try with a valid id");

                accessHibernate.closeConnection(session);
            }
        });
        checkInAndOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                CheckInOut checkInOut=new CheckInOut();
                checkInOut.setSize(700,700);
                contentPane.setVisible(false);
                checkInOut.setVisible(true);
            }
        });
        addClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                ClientUI clientUI=new ClientUI();
                clientUI.setSize(800,500);
                contentPane.setVisible(false);
                clientUI.setVisible(true);
            }
        });
    }


    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        }
        // if exception isn't thrown, then it is an integer
        return true;
    }

    public static void main(String[] args) {
        CrudBook dialog = new CrudBook();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

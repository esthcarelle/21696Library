package UI;

import Hibernate.AccessHibernate;
import Models.Book;
import Models.CheckIn;
import Models.CheckOut;
import Models.Client;


import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.query.Query;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;


public class CheckInOut extends JDialog {
    private JPanel contentPane;
    private JComboBox client;
    private JComboBox book;
    private JButton saveButton;
    private JTable table1;
    private JTextField searchBar;
    private JButton searchButton;
    private JComboBox status;
    private JLabel notFound;
    private JPanel notfound;
    private JTable table2;
    private JButton exportListToPdfButton;
    private JButton exportListToExcelButton;
    int k,j=0;
    AccessHibernate accessHibernate=new AccessHibernate();
    Object [] row=new Object[5];
    Object [] rowBorrowers=new Object[5];
    String[] columns ={"Client id","Client Name","Book name","Date","status"};
    String[] columnsBorrowers={"id","first name","last name","email","date"};
    DefaultTableModel defaultTableModelBorrowers=new DefaultTableModel(columnsBorrowers,0);
    DefaultTableModel defaultTableModel=new DefaultTableModel(columns,0);
    List<CheckIn> checkIn;
    List<CheckOut> checkOuts;

    public CheckInOut() {
        setContentPane(contentPane);
        setModal(true);

        Session session=accessHibernate.openSession();
        Query queryBorrowers=session.createQuery("from CheckOut ");
        List<CheckOut> borrowers=queryBorrowers.list();
        for(int i=0;i<borrowers.size();i++){
            rowBorrowers[0]=borrowers.get(0).getClient().getRegNo();
            rowBorrowers[1]=borrowers.get(0).getClient().getFirstName();
            rowBorrowers[2]=borrowers.get(0).getClient().getLastName();
            rowBorrowers[3]=borrowers.get(0).getClient().getEmailAddress();
            rowBorrowers[4]=borrowers.get(0).getDate();
            defaultTableModelBorrowers.addRow(rowBorrowers);
        }
        table2.setModel(defaultTableModelBorrowers);

        Query query=session.createQuery("from Client");
        Query query1=session.createQuery("from books");
        List<Book>  books=query1.list();

        List<Client> clients=query.list();


        for(int i=0;i<clients.size();i++){
            client.addItem(clients.get(i).getRegNo()+"."+" "+clients.get(i).getFirstName());
        }
        for(int i=0;i<books.size();i++){
            book.addItem(books.get(i).getBookId()+"."+" "+books.get(i).getTitle());
        }

        status.addItem("Check In");
        status.addItem("Check Out");

        Query query2=session.createQuery("from CheckInOut where dtype='CheckIn'");
        Query query3=session.createQuery("from CheckInOut where dtype='CheckOut'");


        checkIn=query2.list();
        for(int i=0;i<checkIn.size();i++){
            row[0]=checkIn.get(i).getClient().getRegNo();
            row[1]=checkIn.get(i).getClient().getFirstName();
            row[2]=checkIn.get(i).getBook().getTitle();
            row[3]=checkIn.get(i).getDate();
            row[4]="Check In";
            defaultTableModel.addRow(row);
        }
        checkOuts=query3.list();
        for(int i=0;i<checkOuts.size();i++){
            row[0]=checkOuts.get(i).getClient().getRegNo();
            row[1]=checkOuts.get(i).getClient().getFirstName();
            row[2]=checkOuts.get(i).getBook().getTitle();
            row[3]=checkOuts.get(i).getDate();
            row[4]="Check Out";
            defaultTableModel.addRow(row);
        }


        table1.setModel(defaultTableModel);
        accessHibernate.closeConnection(session);
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                save();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                search(searchBar.getText());
            }
        });
        exportListToPdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {


                    Document document = new Document(PageSize.A4.rotate());
                try  {

                    PdfWriter writer=PdfWriter.getInstance(document,new FileOutputStream("tableBorrowers"+ k++ +".pdf"));

                    document.open();
                    PdfContentByte cb=writer.getDirectContent();

                    cb.saveState();
                    PdfTemplate pdfTemplate=cb.createTemplate(table2.getWidth(),table2.getHeight());
                    Graphics2D graphics2D=pdfTemplate.createGraphics(table2.getWidth(),table2.getHeight());

                    Shape oldClip=graphics2D.getClip();
                    table2.print(graphics2D);
                    cb.addTemplate(pdfTemplate,1,1);
                    graphics2D.setClip(oldClip);
                    graphics2D.dispose();
                    cb.restoreState();
                    JOptionPane.showMessageDialog(contentPane,"Successfully exported check in the file directory");


                } catch (FileNotFoundException ex){

                } catch (com.itextpdf.text.DocumentException e) {
                    e.printStackTrace();
                }
                document.close();

            }
        });
        exportListToExcelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    exportToExcel(table2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void search(String keyword){
       DefaultTableModel defaultTableModel1=new DefaultTableModel(columns,0);
        Session session=accessHibernate.openSession();

        Query query2=session.createQuery("from CheckInOut where dtype='CheckIn'");
        Query query3=session.createQuery("from CheckInOut where dtype='CheckOut'");


        checkIn=null;
        checkOuts=null;
        checkIn=query2.list();

        int counterCheckIn=0;
        for(int i=0;i<checkIn.size();i++){
            if(checkIn.get(i).getBook().getTitle().equals(keyword)) {
                row[0] = checkIn.get(i).getClient().getRegNo();
                row[1] = checkIn.get(i).getClient().getFirstName();
                row[2] = checkIn.get(i).getBook().getTitle();
                row[3] = checkIn.get(i).getDate();
                row[4] = "Check In";
                defaultTableModel1.addRow(row);
                counterCheckIn++;
            }
        }
        checkOuts=query3.list();
        int counter=0;
        for(int i=0;i<checkOuts.size();i++){
            if(checkOuts.get(i).getBook().getTitle().equals(keyword)) {
                row[0] = checkOuts.get(i).getClient().getRegNo();
                row[1] = checkOuts.get(i).getClient().getFirstName();
                row[2] = checkOuts.get(i).getBook().getTitle();
                row[3] = checkOuts.get(i).getDate();
                row[4] = "Check Out";
                defaultTableModel1.addRow(row);
                counter++;
            }
        }


        if(counter==0 && counterCheckIn==0) {
            notFound.setText("Books with title " + keyword + " not found");
        }
        table1.setModel(defaultTableModel1);

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
    private void save(){

        Session session=accessHibernate.openSession();
        defaultTableModel=(DefaultTableModel)table1.getModel();
        String bookSelected= (String) book.getSelectedItem();
        Book book1=session.get(Book.class,Integer.parseInt(String.valueOf(bookSelected.charAt(0))));

        String clientSelected=(String) client.getSelectedItem();
        Client client1=session.get(Client.class,Integer.parseInt(String.valueOf(clientSelected.charAt(0))));

        String statusSelected=(String) status.getSelectedItem();
        CheckIn checkIn;
        CheckOut checkOut;
        if(statusSelected.equals("Check In")){
           checkIn =new CheckIn(book1,client1,new Date());
            session.save(checkIn);
            row[4]="Check In";
        }
        else {
            checkOut = new CheckOut(book1, client1, new Date());
            session.save(checkOut);
            row[4]="Check Out";
        }
        row[0]=client1.getRegNo();
        row[1]=client1.getFirstName();
        row[2]=book1.getTitle();
        row[3]=new Date();
        defaultTableModel.addRow(row);
        JOptionPane.showMessageDialog(contentPane,"Successfully saved");

        accessHibernate.closeConnection(session);
    }

    public void exportToExcel(JTable jTable) throws FileNotFoundException, IOException {
        Workbook workbook=new XSSFWorkbook();
        Sheet sheet=workbook.createSheet();
        Row row=sheet.createRow(2);
        TableModel model=jTable.getModel();

        Row headerRow=sheet.createRow(0);

        for(int  heading=0;heading<model.getRowCount();heading++){
            headerRow.createCell(heading).setCellValue(model.getColumnName(heading));


        }
        for(int rows=0;rows<model.getRowCount();rows++){
            for(int cols=0;cols<model.getColumnCount();cols++){
                row.createCell(cols).setCellValue(model.getValueAt(rows,cols).toString());
            }
            row=sheet.createRow((rows+3));
        }


        workbook.write((new FileOutputStream("tableBorrowersExcel"+ j++ +".xlsx")));
        JOptionPane.showMessageDialog(contentPane,"Successfully exported check in the file directory");

    }
    public static void main(String[] args) {
        CheckInOut dialog = new CheckInOut();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}

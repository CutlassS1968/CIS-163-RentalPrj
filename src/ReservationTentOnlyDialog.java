import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ReservationTentOnlyDialog extends JDialog implements ActionListener {
    private JTextField txtGuestName;
    private JTextField txtDateCheckin;
    private JTextField txtDateCheckout;
    private JTextField txtNumberOfTenters;
    private JButton okButton;
    private JButton cancelButton;
    private int closeStatus;
    private TentOnly tentOnly;
    public static final int OK = 0;
    public static final int CANCEL = 1;

    /*********************************************************
     Instantiate a Custom Dialog as 'modal' and wait for the
     user to provide data and click on a button.

     @param parent reference to the JFrame application
     @param tentOnly an instantiated object to be filled with data
     *********************************************************/

    public ReservationTentOnlyDialog(JFrame parent, TentOnly tentOnly) {
        // call parent and create a 'modal' dialog
        super(parent, true);
        this.tentOnly = tentOnly;

        setTitle("TentOnly dialog box");
        closeStatus = CANCEL;
        setSize(400,200);

        // prevent user from closing window
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // instantiate and display two text fields
        txtGuestName = new JTextField("Judy",30);
        txtDateCheckin = new JTextField(15);
        txtDateCheckout = new JTextField(15);
        txtNumberOfTenters = new JTextField("4", 15);

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter= new SimpleDateFormat("MM/dd/yyyy"); //format it as per your requirement
        String dateNow = formatter.format(currentDate.getTime());
        currentDate.add(Calendar.DATE, 1);
        String datetomorrow = formatter.format(currentDate.getTime());

        txtDateCheckin.setText(dateNow);
        txtDateCheckout.setText(datetomorrow);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(4,2));

        textPanel.add(new JLabel("Name of guest: "));
        textPanel.add(txtGuestName);
        textPanel.add(new JLabel("Check in date: "));
        textPanel.add(txtDateCheckin);
        textPanel.add(new JLabel("Planned check out date: "));
        textPanel.add(txtDateCheckout);
        textPanel.add(new JLabel("Number of tenters"));
        textPanel.add(txtNumberOfTenters);

        getContentPane().add(textPanel, BorderLayout.CENTER);

        // Instantiate and display two buttons
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        setVisible (true);
    }

    /**************************************************************
     Respond to either button clicks
     @param e the action event that was just fired
     **************************************************************/
    public void actionPerformed(ActionEvent e) {

        JButton button = (JButton) e.getSource();

        // if OK clicked the fill the object
        if (button == okButton) {
            // save the information in the object
            closeStatus = OK;
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            Date d1 = null;
            Date d2 = null;
            try {
                GregorianCalendar gregTemp = new GregorianCalendar();
                d1 = df.parse(txtDateCheckin.getText());
                gregTemp.setTime(d1);
                tentOnly.setCheckIn(gregTemp);

                gregTemp = new GregorianCalendar();
                d2 = df.parse(txtDateCheckout.getText());
                gregTemp.setTime(d2);
                tentOnly.setEstimatedCheckOut(gregTemp);

            } catch (ParseException e1) {
//                  Do some thing good, what I am not sure.
            }

            tentOnly.setGuestName(txtGuestName.getText());
            tentOnly.setNumberOfTenters(Integer.parseInt(txtNumberOfTenters.getText()));
        }

        // make the dialog disappear
        dispose();
    }

    /**************************************************************
     Return a String to let the caller know which button
     was clicked

     @return an int representing the option OK or CANCEL
     **************************************************************/
    public int getCloseStatus(){
        return closeStatus;
    }
}


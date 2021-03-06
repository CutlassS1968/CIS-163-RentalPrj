import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**********************************************************************************************
 *
 * ReservationRVDialog is the dialog box that shows up when you select "Reserve RV"
 *
 * @author Evan Johns
 * @author Austin Ackerman
 * @version 03/23/2020
 *
 **********************************************************************************************/
public class ReservationRVDialog extends JDialog implements ActionListener {

  /**
   * GUI elements
   */
  private JTextField txtGuestName;
  private JTextField txtDateCheckIn;
  private JTextField txtDateCheckout;
  private JTextField txtPowerSupplied;
  private JButton okButton;
  private JButton cancelButton;


  /**
   * Rv CampSite being booked
   */
  private RV rv;


  /**
   * current status of the dialog box
   */
  private int closeStatus;
  public static final int OK = 0;
  public static final int CANCEL = 1;


  /*********************************************************************************************
   *
   * Instantiate a custom dialog box as 'modal' and wait for the use to provide an input
   *
   * @param parent reference to JFrame parent of the dialog box
   * @param rv the CampSite being booked
   *
   *********************************************************************************************/
  public ReservationRVDialog(JFrame parent, RV rv) {
    // call parent and create a 'modal' dialog
    super(parent, true);
    this.rv = rv;

    setTitle("RV Reservation dialog box");
    closeStatus = CANCEL;
    setSize(400, 200);

    // prevent user from closing window
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    // instantiate and display two text fields
    txtGuestName = new JTextField("Roger", 30);
    txtDateCheckIn = new JTextField(15);
    txtDateCheckout = new JTextField(15);
    txtPowerSupplied = new JTextField("1500", 15);

    Calendar currentDate = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); //format it as per your requirement
    String dateNow = formatter.format(currentDate.getTime());
    currentDate.add(Calendar.DATE, 1);
    String dateTomorrow = formatter.format(currentDate.getTime());

    txtDateCheckIn.setText(dateNow);
    txtDateCheckout.setText(dateTomorrow);

    JPanel textPanel = new JPanel();
    textPanel.setLayout(new GridLayout(4, 2));

    textPanel.add(new JLabel("Name of Guest: "));
    textPanel.add(txtGuestName);
    textPanel.add(new JLabel("Check in date: "));
    textPanel.add(txtDateCheckIn);
    textPanel.add(new JLabel("Planned check out date: "));
    textPanel.add(txtDateCheckout);
    textPanel.add(new JLabel("Power to be supplied: "));
    textPanel.add(txtPowerSupplied);

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

    setVisible(true);
  }

  /*********************************************************************************************
   *
   * Activate a response to either button presses
   *
   * @param e the action even that was just activated
   *
   *********************************************************************************************/
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
        d1 = df.parse(txtDateCheckIn.getText());
        gregTemp.setTime(d1);
        rv.setCheckIn(gregTemp);

        gregTemp = new GregorianCalendar();
        d2 = df.parse(txtDateCheckout.getText());
        gregTemp.setTime(d2);
        rv.setEstimatedCheckOut(gregTemp);

      } catch (ParseException e1) {
        closeStatus = CANCEL;
      }

      rv.setGuestName(txtGuestName.getText());
      rv.setPower(Integer.parseInt(txtPowerSupplied.getText()));
    }

    // make the dialog disappear
    dispose();
  }

  public int getCloseStatus() {
    return closeStatus;
  }
}
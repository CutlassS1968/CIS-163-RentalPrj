import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**********************************************************************************************
 *
 * CheckOutOnDialog is the dialog box that shows up when you select "Check Out"
 *
 * @author Evan Johns
 * @author Austin Ackerman
 * @version 03/23/2020
 *
 **********************************************************************************************/
public class CheckOutOnDialog extends JDialog implements ActionListener {

  /**
   * GUI elements
   */
  private JTextField txtDate;
  private JButton okButton;
  private JButton cancelButton;


  /**
   * CampSite selected in the GUI
   */
  private CampSite campSite;


  /**
   * current status of the dialog box
   */
  private int closeStatus;
  static final int OK = 0;
  static final int CANCEL = 1;


  /*********************************************************************************************
   *
   * Instantiate a custom dialog box as 'modal' and wait for the use to provide an input
   *
   * @param parent reference to JFrame parent of the dialog box
   * @param campSite the selected CampSite in the GUI
   *
   *********************************************************************************************/
  public CheckOutOnDialog(JFrame parent, CampSite campSite) {
    // call parent and create a 'modal' dialog
    super(parent, true);

    this.campSite = campSite;
    setTitle("Check Out dialog box");
    closeStatus = CANCEL;
    setSize(300, 100);

    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    txtDate = new JTextField(dateFormat.format(campSite.
        getEstimatedCheckOut().getTime()), 30);

    JPanel textPanel = new JPanel();
    textPanel.setLayout(new GridLayout(1, 2));
    textPanel.add(new JLabel("Check out Date: "));
    textPanel.add(txtDate);

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

      SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
      GregorianCalendar gTemp = new GregorianCalendar();

      Date d = null;
      try {
        d = df.parse(txtDate.getText());
        gTemp.setTime(d);
        if (campSite.getCheckIn().getTime().getTime() < gTemp.getTime().getTime()) {
          campSite.setActualCheckOut(gTemp);
          closeStatus = OK;
        } else throw new IllegalArgumentException("Date entered is before check in date!");

      } catch (ParseException e1) {
      }
    }

    // make the dialog disappear
    dispose();
  }

  public int getCloseStatus() {
    return closeStatus;
  }

}


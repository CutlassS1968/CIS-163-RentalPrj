import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**********************************************************************************************
 *
 * OverDueReferenceDialog is the dialog box that shows up when you select "Over Due"
 *
 * @author Evan Johns
 * @author Austin Ackerman
 * @version 03/23/2020
 *
 **********************************************************************************************/
public class OverDueReferenceDialog extends JDialog implements ActionListener {

  /**
   * GUI elements
   */
  private JTextField dateField;
  private JButton okButton;
  private JButton cancelButton;


  /**
   * User's over due date reference
   */
  private Date refDate;


  /**
   * Date Format for parsing dates
   */
  private SimpleDateFormat dateFormat;


  /**
   * Current status of the dialog box
   */
  private int closeStatus;
  static final int OK = 0;
  static final int CANCEL = 1;


  /*********************************************************************************************
   *
   * Instantiate a custom dialog box as 'modal' and wait for the use to provide an input
   *
   * @param parent reference to JFrame parent of the dialog box
   * @param modal controls whether you can manipulate the parent frame
   *
   *********************************************************************************************/
  public OverDueReferenceDialog(JFrame parent, Boolean modal) {
    // call parent and create a 'modal' dialog
    super(parent, modal);

    setTitle("Input");
    closeStatus = CANCEL;
    setSize(225, 120);

    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    JPanel textPanel = new JPanel();
    textPanel.setLayout(new GridLayout(1, 2));
    textPanel.add(new JLabel("Enter Date (Default is current date) :"));

    JPanel datePanel = new JPanel();
    dateField = new JTextField(15);
    datePanel.add(dateField);

    getContentPane().add(textPanel, BorderLayout.NORTH);
    getContentPane().add(datePanel, BorderLayout.CENTER);

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
      GregorianCalendar gTemp = new GregorianCalendar();
      refDate = gTemp.getTime();

      try {
        refDate = dateFormat.parse(dateField.getText());
      } catch (ParseException e1) {
        throw new RuntimeException("Illegal date entered");
      }
    }

    // make the dialog disappear
    dispose();
  }

  public Date getRefDate() {
    return refDate;
  }

  public int getCloseStatus() {
    return closeStatus;
  }
}


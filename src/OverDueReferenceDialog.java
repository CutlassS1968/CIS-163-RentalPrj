import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class OverDueReferenceDialog extends JDialog implements ActionListener {

  private JTextField dateField;

  private JButton okButton;
  private JButton cancelButton;
  private int closeStatus;
  private Date refDate;

  static final int OK = 0;
  static final int CANCEL = 1;

  public OverDueReferenceDialog(JFrame parent, Boolean modal) {
    // call parent and create a 'modal' dialog
    super(parent, true);

    setTitle("Input");
    closeStatus = CANCEL;
    setSize(300,100);

    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    JPanel textPanel = new JPanel();
    textPanel.setLayout(new GridLayout(1,3));
    textPanel.add(new JLabel("Enter Date:"));
    dateField = new JTextField();
    textPanel.add(dateField);

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
      GregorianCalendar gTemp = new GregorianCalendar();
      refDate = gTemp.getTime();

      try {
        refDate = df.parse(dateField.getText());
//        ListModel.setRefDate(refDate);

      } catch (ParseException e1) {
        // TODO: default to current date when incorrect date;
      }

    }

    if (button == cancelButton) {
      // TODO: add cancel button; return to previous window
    }

    // make the dialog disappear
    dispose();
  }

  public Date getRefDate() {
    return refDate;
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


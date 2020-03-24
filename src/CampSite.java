import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**********************************************************************************************
 *
 * CampSite is the base form of all possible locations in the database
 *
 * @author Evan Johns
 * @author Austin Ackerman
 * @version 03/23/2020
 *
 **********************************************************************************************/
public abstract class CampSite implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * Guest's name for the reservation
   */
  protected String guestName;


  /**
   * The type of Camp Site it is
   */
  protected int guestType;


  /**
   * All important dates that pertain to the CampSite
   */
  protected GregorianCalendar checkIn;
  protected GregorianCalendar estimatedCheckOut;
  protected GregorianCalendar actualCheckOut;


  /*********************************************************************************************
   * General CampSite Constructor
   *********************************************************************************************/
  public CampSite(String guestName, GregorianCalendar checkIn,
                  GregorianCalendar estimatedCheckOut,
                  GregorianCalendar actualCheckOut) {
    this.guestName = guestName;
    this.checkIn = checkIn;
    this.estimatedCheckOut = estimatedCheckOut;
    this.actualCheckOut = actualCheckOut;
  }

  /*********************************************************************************************
   * Default CampSite Constructor
   *********************************************************************************************/
  public CampSite() {
  }

  public int daysBetween(Date d1, Date d2) {
    return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
  }

  public int daysSince(Date refDate) {
    return (int) ((estimatedCheckOut.getTime().getTime() - refDate.getTime()) /
        (1000 * 60 * 60 * 24));
  }

  public abstract double getCost(GregorianCalendar checkOut);

  public String getGuestName() {
    return guestName;
  }

  public void setGuestName(String guestName) {
    this.guestName = guestName;
  }

  public int getGuestType() {
    return guestType;
  }

  public void setGuestType(int guestType) {
    this.guestType = guestType;
  }

  public GregorianCalendar getCheckIn() {
    return checkIn;
  }

  public void setCheckIn(GregorianCalendar checkIn) {
    this.checkIn = checkIn;
  }

  public GregorianCalendar getEstimatedCheckOut() {
    return estimatedCheckOut;
  }

  public void setEstimatedCheckOut(GregorianCalendar estimatedCheckOut) {
    this.estimatedCheckOut = estimatedCheckOut;
  }

  public GregorianCalendar getActualCheckOut() {
    return actualCheckOut;
  }

  public void setActualCheckOut(GregorianCalendar actualCheckOut) {
    this.actualCheckOut = actualCheckOut;
  }

  // Code used by the debugger
  @Override
  public String toString() {
    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    String checkInOnDateStr;
    if (getCheckIn() == null)
      checkInOnDateStr = "";
    else
      checkInOnDateStr = formatter.format(getCheckIn().getTime());

    String estCheckOutStr;
    if (getEstimatedCheckOut() == null)
      estCheckOutStr = "";
    else
      estCheckOutStr = formatter.format(getEstimatedCheckOut().getTime());

    String checkOutStr;
    if (getActualCheckOut() == null)
      checkOutStr = "";
    else
      checkOutStr = formatter.format(getActualCheckOut().getTime());

    return "CampSite{" +
        "guestName='" + guestName + '\'' +
        ", checkIn=" + checkInOnDateStr +
        ", estimatedCheckOut=" + estCheckOutStr +
        ", actualCheckOut=" + checkOutStr +
        '}';
  }
}

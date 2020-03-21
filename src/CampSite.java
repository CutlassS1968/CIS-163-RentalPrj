import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public abstract class CampSite implements Serializable {
  // What is the purpose of this variable (search Google)
  private static final long serialVersionUID = 1L;
  /**
   * "Java serialization is the process of converting an object into a stream of bytes so we can
   * do stuff like store it on disk or send it over the network. Deserialization is the reverse
   * process - converting a stream of bytes into an object in memory.
   *
   *      During serialization, java runtime associates a version number with each
   * serializable class. This number is called serialVersionUID, whis is used during
   * deserialization to verify that the sender and receiver of a serialized object have loaded
   * classes for that object that are compatible with respect to serialization. If the receiver
   * has loaded a class for the object that has a different serialVersionUID than that of the
   * corresponding sender's class, then deserialization will result in an InvalidClassException"
   *  - How To Do In Java
   */

  /**
   * The name of the person that  is reserving the CampSite
   */
  protected String guestName;
  protected int guestType;

  /**
   * The date the CampSite was checkIn on
   */
  protected GregorianCalendar checkIn;
  protected GregorianCalendar estimatedCheckOut;
  protected GregorianCalendar actualCheckOut;

  public CampSite() {
  }

  public CampSite(String guestName,
                  GregorianCalendar checkIn,
                  GregorianCalendar estimatedCheckOut,
                  GregorianCalendar actualCheckOut) {
    this.guestName = guestName;
    this.checkIn = checkIn;
    this.estimatedCheckOut = estimatedCheckOut;
    this.actualCheckOut = actualCheckOut;
  }

  public int daysBetween(Date d1, Date d2) {
    return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
  }

  public int daysSince(Date refDate) {
    return (int) ((estimatedCheckOut.getTime().getTime() - refDate.getTime()) / (1000 * 60 * 60 * 24));
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

  // following code used for debugging only
  // IntelliJ using the toString for displaying in debugger.
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

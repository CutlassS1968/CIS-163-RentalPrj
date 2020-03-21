import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

public class TentOnly extends CampSite {

  private int numberOfTenters;

  public TentOnly() {
  }

  public TentOnly(String guestName, GregorianCalendar checkIn, GregorianCalendar estimatedCheckOut,
                  GregorianCalendar actualCheckOut, int numberOfTenters) {
    super(guestName, checkIn, estimatedCheckOut, actualCheckOut);
    this.numberOfTenters = numberOfTenters;
  }

  public int getNumberOfTenters() {
    return numberOfTenters;
  }

  public void setNumberOfTenters(int numberOfTenters) {
    this.numberOfTenters = numberOfTenters;
  }

  @Override
  public double getCost(GregorianCalendar checkOut) {
    if (numberOfTenters <= 10) {
      return 10 * daysBetween(checkIn.getTime(), checkOut.getTime());
    } else {
      return 20 * daysBetween(checkIn.getTime(), checkOut.getTime());
    }
  }

  @Override
  public String toString() {
    return "TentOnly{" +
        "numberOfTenters=" + numberOfTenters +
        super.toString() +
        '}';
  }

}

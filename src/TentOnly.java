import java.util.GregorianCalendar;

/**********************************************************************************************
 *
 * Derived from the Campsite class, TentOnly unique in that it has a separate cost method and a
 * different instance variable to that of other types of classes derived from the CampSite class
 *
 * @author Evan Johns
 * @author Austin Ackerman
 * @version 03/23/2020
 *
 **********************************************************************************************/
public class TentOnly extends CampSite {

  /**
   * Amount of people per CampSite
   */
  private int numberOfTenters;


  /*********************************************************************************************
   * General CampSite Constructor
   *********************************************************************************************/
  public TentOnly(String guestName, GregorianCalendar checkIn, GregorianCalendar estimatedCheckOut,
                  GregorianCalendar actualCheckOut, int numberOfTenters) {
    super(guestName, checkIn, estimatedCheckOut, actualCheckOut);
    this.numberOfTenters = numberOfTenters;
    guestType = 0;
  }

  /*********************************************************************************************
   * Default CampSite Constructor
   *********************************************************************************************/
  public TentOnly() {
    guestType = 0;
  }

  /**
   * The overridden cost function, Tents are 10 dollars, with an extra 10
   * dollars per day for more than 10 tenters
   *
   * @param checkOut Day of actual check out
   * @return cost in dollars that is due
   */
  @Override
  public double getCost(GregorianCalendar checkOut) {
    if (numberOfTenters <= 10) {
      return 10 * daysBetween(checkIn.getTime(), checkOut.getTime());
    } else {
      return 20 * daysBetween(checkIn.getTime(), checkOut.getTime());
    }
  }

  public int getNumberOfTenters() {
    return numberOfTenters;
  }

  public void setNumberOfTenters(int numberOfTenters) {
    this.numberOfTenters = numberOfTenters;
  }

  @Override
  public String toString() {
    return "TentOnly{" +
        "numberOfTenters=" + numberOfTenters +
        super.toString() +
        '}';
  }
}

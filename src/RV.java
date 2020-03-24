import java.util.GregorianCalendar;

/**********************************************************************************************
 *
 * Derived from the Campsite class, RV unique in that it has a separate cost method and a
 * different instance variable to that of other types of classes derived from the campsite class
 *
 * @author Evan Johns
 * @author Austin Ackerman
 * @version 03/23/2020
 *
 **********************************************************************************************/
public class RV extends CampSite {

  /**
   * Guest RV power usage
   */
  private int power;


  /*********************************************************************************************
   * General CampSite Constructor
   *********************************************************************************************/
  public RV(String guestName, GregorianCalendar checkIn, GregorianCalendar estimatedCheckOut,
            GregorianCalendar actualCheckOut, int power) {
    super(guestName, checkIn, estimatedCheckOut, actualCheckOut);
    this.power = power;
    guestType = 1;
  }

  /*********************************************************************************************
   * Default CampSite Constructor
   *********************************************************************************************/
  public RV() {
    guestType = 1;
  }

  /**
   * The overridden cost function, RVs start at 10 dollars, with 20 dollars per day
   * and an extra 10 dollars per day for power greater than 1000
   *
   * @param checkOut Day of actual check out
   * @return cost in dollars that is due
   */
  @Override
  public double getCost(GregorianCalendar checkOut) {
    if (power <= 1000) {
      return 10 + (20 * daysBetween(checkIn.getTime(), checkOut.getTime()));
    } else {
      return 10 + (30 * daysBetween(checkIn.getTime(), checkOut.getTime()));
    }
  }

  public int getPower() {
    return power;
  }

  public void setPower(int power) {
    this.power = power;
  }

  @Override
  public String toString() {
    return "RV{" +
        "power=" + power +
        super.toString() +
        '}';
  }
}
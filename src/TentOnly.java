import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TentOnly extends CampSite {

    private int numberOfTenters;
    private int numberOfDays;
    SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy");

    public TentOnly() {
    }

    public TentOnly(String guestName,
                    GregorianCalendar checkIn,
                    GregorianCalendar estimatedCheckOut,
                    GregorianCalendar actualCheckOut,
                    int numberOfTenters) {
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
      SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy");
      String strCheckOutDate = formatter.format(checkOut);
      String strCheckInDate = formatter.format(checkIn);

      String strCheckOutDateDay = strCheckOutDate.substring(0,1);
      String strCheckOutDateMonth = strCheckOutDate.substring(3,4);
      String strCheckOutDateYear =  strCheckOutDate.substring(6,9);

      String strCheckInDateDay =  strCheckInDate.substring(0,1);
      String strCheckInDateMonth = strCheckInDate.substring(3,4);
      String strCheckInDateYear = strCheckInDate.substring(6,9);

      int outDay = Integer.parseInt(strCheckOutDateDay);
      int outMonth = Integer.parseInt(strCheckOutDateMonth);
      int outYear = Integer.parseInt(strCheckOutDateYear);

      int inDay = Integer.parseInt(strCheckInDateDay);
      int inMonth = Integer.parseInt(strCheckInDateMonth);
      int inYear = Integer.parseInt(strCheckInDateYear);

      int tempMonthDay;
      if (inMonth == 1 || inMonth == 3 || inMonth == 5 || inMonth == 7 || inMonth == 8 ||
          inMonth == 10 || inMonth == 12) {
        tempMonthDay = 31;
      }
      if (inMonth == 4 || inMonth == 6 || inMonth == 9 || inMonth == 11) {
        tempMonthDay = 30;
      }
      if (inMonth == 2 && inYear % 4 == 0) {
        if (inYear % 100 == 0) {
          if (inYear % 400 == 0) {
            tempMonthDay = 29;
          }
          else tempMonthDay = 28;
        }
        else tempMonthDay = 29;
      }
      else tempMonthDay = 28;



      numberOfDays += outDay;
      numberOfDays += (tempMonthDay - inDay);


      double cost = 10;
        return cost;
    }

    @Override
    public String toString() {
        return "TentOnly{" +
                "numberOfTenters=" + numberOfTenters +
                super.toString() +
                '}';
    }

}

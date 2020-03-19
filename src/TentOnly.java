import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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


      int[] outMonths = {1,2,3,4,5,6,7,8,9,10,11,12};
      int tempOutMonths = 0;
      int tempOutDays;
      for (int month : outMonths) {
        tempOutMonths = month;
      }
      switch(tempOutMonths) {
        case 1: case 3: case 5: case 7: case 8: case 10: case 12:
          tempOutDays = 31;
          break;
        case 4: case 6: case 9: case 11:
          tempOutDays = 30;
          break;
        case 2:
          if (outYear % 4 == 0) {
            if (outYear % 100 == 0) {
              if (outYear % 400 == 0) {
                tempOutDays = 29;
              }
              else tempOutDays = 28;
            }
            else tempOutDays = 29;
          }
          else tempOutDays = 28;
          break;
        default:
          throw new IllegalArgumentException("Invalid Month");
      }
      
      int[] inMonths = {1,2,3,4,5,6,7,8,9,10,11,12};
      int tempInMonths = 0;
      int tempInDays;
      for (int month : inMonths) {
        tempInMonths = month;
      }
      switch(tempInMonths) {
        case 1: case 3: case 5: case 7: case 8: case 10: case 12:
          tempInDays = 31;
          break;
        case 4: case 6: case 9: case 11:
          tempInDays = 30;
          break;
        case 2:
          if (outYear % 4 == 0) {
            if (outYear % 100 == 0) {
              if (outYear % 400 == 0) {
                tempInDays = 29;
              }
              else tempInDays = 28;
            }
            else tempInDays = 29;
          }
          else tempInDays = 28;
          break;
        default:
          throw new IllegalArgumentException("Invalid Month");
      }

      numberOfDays += outDay;
      numberOfDays += (tempOutDays - tempInDays);


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

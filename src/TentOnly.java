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
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
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

      int numberOfDays = 0;

      boolean all31DayMonths = inMonth == 1 || inMonth == 3 || inMonth == 5 || inMonth == 7 || inMonth == 8 ||
          inMonth == 10 || inMonth == 12;
      boolean all30DayMonths = inMonth == 4 || inMonth == 6 || inMonth == 9 || inMonth == 11;
      boolean monthsApart = inMonth + 2 == outMonth || inMonth + 3 == outMonth || inMonth + 4 == outMonth ||
          inMonth + 5 == outMonth || inMonth + 6 == outMonth || inMonth + 7 == outMonth || inMonth + 8 == outMonth ||
          inMonth + 9 == outMonth || inMonth + 10 == outMonth || inMonth + 11 == outMonth;

      // when years equal
      if (outYear == inYear) {
        // same month
        if (inMonth == outMonth) {
          numberOfDays = outDay - inDay;
        }

        // 1 month apart
        else if (inMonth + 1 == outMonth) {
          // Days
          numberOfDays += outDay;
          if (all31DayMonths) {
            numberOfDays = (31 - inDay);
          } else if (all30DayMonths) {
            numberOfDays = (30 - inDay);
          } else if (inMonth == 2) {
            if (outYear % 4 == 0) {
              if (outYear % 100 == 0) {
                if (outYear % 400 == 0) {
                  numberOfDays = (29 - inDay);
                }
                else numberOfDays = (28 - inDay);
              }
              else numberOfDays = (29 - inDay);
            }
            else numberOfDays = (28 - inDay);
          }
        }

        // 2 or more months apart
        else if (monthsApart) {
          // Months
          for (int i = (inMonth + 1); i < outMonth; i++) {
            if (i == 1 || i == 3 || i == 5 || i == 7 || i == 8 || i == 10 || i == 12) {
              numberOfDays += 31;
            } else if (i == 4 || i == 6 || i == 9 || i == 11) {
              numberOfDays += 30;
            } else if (i == 2) {
              if (outYear % 4 == 0) {
                if (outYear % 100 == 0) {
                  if (outYear % 400 == 0) {
                    numberOfDays += 29;
                  } else numberOfDays += 28;
                } else numberOfDays += 29;
              } else numberOfDays += 28;
            }
          }
          // Days
          numberOfDays += outDay;
          if (all31DayMonths) {
            numberOfDays = (31 - inDay);
          } else if (all30DayMonths) {
            numberOfDays = (30 - inDay);
          } else if (inMonth == 2) {
            if (outYear % 4 == 0) {
              if (outYear % 100 == 0) {
                if (outYear % 400 == 0) {
                  numberOfDays = (29 - inDay);
                } else numberOfDays = (28 - inDay);
              } else numberOfDays = (29 - inDay);
            } else numberOfDays = (28 - inDay);
          }
        }
      }


      // when years are not equal
      else {

        // years
        numberOfDays += ((outYear - inYear) * 365);

        // same month
        if (inMonth == outMonth) {
          numberOfDays = outDay - inDay;
        }

        // 1 month apart
        else if (inMonth + 1 == outMonth) {
          // Days
          numberOfDays += outDay;
          if (all31DayMonths) {
            numberOfDays = (31 - inDay);
          } else if (all30DayMonths) {
            numberOfDays = (30 - inDay);
          } else if (inMonth == 2) {
            if (outYear % 4 == 0) {
              if (outYear % 100 == 0) {
                if (outYear % 400 == 0) {
                  numberOfDays = (29 - inDay);
                }
                else numberOfDays = (28 - inDay);
              }
              else numberOfDays = (29 - inDay);
            }
            else numberOfDays = (28 - inDay);
          }
        }

        // 2 or more months apart
        else if (monthsApart) {
          // Months
          for (int i = (inMonth + 1); i < outMonth; i++) {
            if (i == 1 || i == 3 || i == 5 || i == 7 || i == 8 || i == 10 || i == 12) {
              numberOfDays += 31;
            } else if (i == 4 || i == 6 || i == 9 || i == 11) {
              numberOfDays += 30;
            } else if (i == 2) {
              if (outYear % 4 == 0) {
                if (outYear % 100 == 0) {
                  if (outYear % 400 == 0) {
                    numberOfDays += 29;
                  } else numberOfDays += 28;
                } else numberOfDays += 29;
              } else numberOfDays += 28;
            }
          }
          // Days
          numberOfDays += outDay;
          if (all31DayMonths) {
            numberOfDays = (31 - inDay);
          } else if (all30DayMonths) {
            numberOfDays = (30 - inDay);
          } else if (inMonth == 2) {
            if (outYear % 4 == 0) {
              if (outYear % 100 == 0) {
                if (outYear % 400 == 0) {
                  numberOfDays = (29 - inDay);
                } else numberOfDays = (28 - inDay);
              } else numberOfDays = (29 - inDay);
            } else numberOfDays = (28 - inDay);
          }
        }
      }

      double cost = 0;
      if (numberOfTenters > 0 && numberOfTenters <= 10) {
        cost = numberOfDays * 10;
      } else if (numberOfTenters > 10) {
        cost = numberOfDays * 20;
      }

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

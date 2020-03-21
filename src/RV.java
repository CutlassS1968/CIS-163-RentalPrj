import java.util.Calendar;
import java.util.GregorianCalendar;

public class RV extends CampSite {

private int power;

    public RV() {
    }

    public RV(String guestName, GregorianCalendar checkIn, GregorianCalendar estimatedCheckOut, GregorianCalendar actualCheckOut, int power) {
        super(guestName, checkIn, estimatedCheckOut, actualCheckOut);
        this.power = power;
        }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public double getCost(GregorianCalendar checkOut) {
        if (power <= 1000) {
          return 10 + (20 * daysBetween(checkIn.getTime(), checkOut.getTime()));
        } else {
          return 10 + (30 * daysBetween(checkIn.getTime(), checkOut.getTime()));
        }
    }

    @Override
    public String toString() {
        return "RV{" +
                "power=" + power +
                super.toString() +
                '}';
    }
}
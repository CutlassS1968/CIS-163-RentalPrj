import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public abstract class
CampSite implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String guestName;
    protected GregorianCalendar checkIn;
    protected GregorianCalendar estimatedCheckOut;
    protected GregorianCalendar actualCheckOut;

    public CampSite() {
    }

    public abstract double getCost(GregorianCalendar checkOut);

    public CampSite(String guestName,
                    GregorianCalendar checkIn,
                    GregorianCalendar estimatedCheckOut,
                    GregorianCalendar actualCheckOut) {
        this.guestName = guestName;
        this.checkIn = checkIn;
        this.estimatedCheckOut = estimatedCheckOut;
        this.actualCheckOut = actualCheckOut;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
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

        String  estCheckOutStr;
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

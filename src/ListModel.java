import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.sql.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ListModel extends AbstractTableModel {
  private ArrayList<CampSite> listCampSites;
  private ArrayList<CampSite> filteredListCampSites;

  private ScreenDisplay display;

  private String[] columnNamesForCurrentPark = {"Guest Name", "Est. Cost",
      "Check in Date", "EST. Check out Date ", "Max Power", "Num of Tenters"};

  private String[] columnNamesForCheckouts = {"Guest Name", "Est. Cost",
      "Check in Date", "ACTUAL Check out Date ", " Real Cost"};

  private String[] columnNamesForOverDue = {"Guest Name", "Est. Cost",
      "Est. Check Out Date", "Days Over Due"};

  private String[] columnNamesForRvTent = columnNamesForCurrentPark;

  private String[] columnNamesForTentRv = columnNamesForCurrentPark;

  private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

  private Date refDate;

  public ListModel() {
    super();
    display = ScreenDisplay.CurrentParkStatus;
    listCampSites = new ArrayList<CampSite>();
    UpdateScreen();
    createList();
  }

  public void setDisplay(ScreenDisplay selected) {
    display = selected;
    UpdateScreen();
  }

  private void UpdateScreen() {
    switch (display) {
      case CurrentParkStatus:
        filteredListCampSites = (ArrayList<CampSite>) listCampSites.stream().
            filter(n -> n.actualCheckOut == null).collect(Collectors.toList());

        // Note: This uses Lambda function
        Collections.sort(filteredListCampSites, (n1, n2) -> n1.getGuestName().compareTo(n2.guestName));
        break;

      case CheckOutGuest:
        filteredListCampSites = (ArrayList<CampSite>) listCampSites.stream().
            filter(n -> n.getActualCheckOut() != null).collect(Collectors.toList());

        // Note: This uses an anonymous class.
        Collections.sort(filteredListCampSites, new Comparator<CampSite>() {
          @Override
          public int compare(CampSite n1, CampSite n2) {
            return n1.getGuestName().compareTo(n2.guestName);
          }
        });

        break;

      case OverDueGuest:
        filteredListCampSites = (ArrayList<CampSite>) listCampSites.stream().
            filter(n -> n.actualCheckOut == null).collect(Collectors.toList());

        Collections.sort(filteredListCampSites,
            (n1, n2) -> n1.getEstimatedCheckOut().compareTo(n2.getEstimatedCheckOut()));

        break;

      case RvTent:
        filteredListCampSites = (ArrayList<CampSite>) listCampSites.stream().
            filter(n -> n.actualCheckOut == null).collect(Collectors.toList());

        // Alternatively you could use streams to sort the function, which is much easier than
        // using lambdas or anonymous functions
        //
//         filteredListCampSites.stream().sorted(Comparator.comparing(CampSite::getGuestType)
//         .thenComparing(CampSite::getGuestName));

        /** Sorting names by using a lambda function */
        Collections.sort(filteredListCampSites,
            (n1, n2) -> {
          if (n1.getGuestType() != 1 && n2.getGuestType() != 1)
            return Integer.toString(n1.getGuestType()).compareTo(Integer.toString(n2.getGuestType()));
          else
            return n1.getGuestName().compareTo(n2.getGuestName());
            });
        break;

      case TentRv:
        filteredListCampSites = (ArrayList<CampSite>) listCampSites.stream().
            filter(n -> n.actualCheckOut == null).collect(Collectors.toList());

        /** Sorting names by using an anonymous function */
        Collections.sort(filteredListCampSites, new Comparator<CampSite>() {
          @Override
          public int compare(CampSite n1, CampSite n2) {
            if (n1.getGuestType() == 0 && n2.getGuestType() == 0) {
              if (n1.getGuestName().compareTo(n2.getGuestName()) == 0) {
                return n1.getEstimatedCheckOut().compareTo(n2.getEstimatedCheckOut());
              }
              return n1.getGuestName().compareTo(n2.getGuestName());
            } else {
              return Integer.toString(n1.getGuestType()).compareTo(Integer.toString(n2.getGuestType()));
            }
          }
        });

        break;

      default:
        throw new RuntimeException("upDate is in undefined state: " + display);
    }
    fireTableStructureChanged();
  }

  // JTABLE IS USING THESE METHODS TO DETERMINE WHERE YOU'RE DATA IS GOING
  @Override
  public String getColumnName(int col) {
    switch (display) {
      case CurrentParkStatus:
        return columnNamesForCurrentPark[col];
      case CheckOutGuest:
        return columnNamesForCheckouts[col];
      case OverDueGuest:
        return columnNamesForOverDue[col];
      case RvTent:
        return columnNamesForRvTent[col];
      case TentRv:
        return columnNamesForTentRv[col];
    }
    throw new RuntimeException("Undefined state for Col Names: " + display);
  }

  @Override
  public int getColumnCount() {
    switch (display) {
      case CurrentParkStatus:
        return columnNamesForCurrentPark.length;
      case CheckOutGuest:
        return columnNamesForCheckouts.length;
      case OverDueGuest:
        return columnNamesForOverDue.length;
      case RvTent:
        return columnNamesForRvTent.length;
      case TentRv:
        return columnNamesForTentRv.length;
    }
    throw new IllegalArgumentException();
  }

  @Override
  public int getRowCount() {
    return filteredListCampSites.size();     // returns number of items in the arraylist
  }

  @Override
  public Object getValueAt(int row, int col) {
    switch (display) {
      case CurrentParkStatus:
        return currentParkScreen(row, col);
      case CheckOutGuest:
        return checkOutScreen(row, col);
      case OverDueGuest:
        return overDueScreen(row, col);
      case RvTent:
        return rvTentScreen(row, col);
      case TentRv:
        return tentRvScreen(row, col);
    }
    throw new IllegalArgumentException();
  }

  private Object currentParkScreen(int row, int col) {
    switch (col) {
      case 0:
        return (filteredListCampSites.get(row).guestName);

      case 1:
        return (filteredListCampSites.get(row).getCost(filteredListCampSites.
            get(row).estimatedCheckOut));

      case 2:
        return (formatter.format(filteredListCampSites.get(row).checkIn.getTime()));

      case 3:
        if (filteredListCampSites.get(row).estimatedCheckOut == null)
          return "-";

        return (formatter.format(filteredListCampSites.get(row).estimatedCheckOut.
            getTime()));

      case 4:
      case 5:
        if (filteredListCampSites.get(row) instanceof RV)
          if (col == 4)
            return (((RV) filteredListCampSites.get(row)).getPower());
          else
            return "";

        else {
          if (col == 5)
            return (((TentOnly) filteredListCampSites.get(row)).
                getNumberOfTenters());
          else
            return "";
        }
      default:
        throw new RuntimeException("Row,col out of range: " + row + " " + col);
    }
  }

  private Object checkOutScreen(int row, int col) {
    switch (col) {
      case 0:
        return (filteredListCampSites.get(row).guestName);

      case 1:
        return (filteredListCampSites.
            get(row).getCost(filteredListCampSites.get(row).
            estimatedCheckOut));
      case 2:
        return (formatter.format(filteredListCampSites.get(row).checkIn.
            getTime()));

      case 3:
        return (formatter.format(filteredListCampSites.get(row).actualCheckOut.
            getTime()));

      case 4:
        return (filteredListCampSites.
            get(row).getCost(filteredListCampSites.get(row).
            actualCheckOut
        ));

      default:
        throw new RuntimeException("Row,col out of range: " + row + " " + col);
    }
  }

  private Object overDueScreen(int row, int col) {
    switch (col) {
      case 0:
        return (filteredListCampSites.get(row).guestName);

      case 1:
        return (filteredListCampSites.get(row).getCost(filteredListCampSites.
            get(row).estimatedCheckOut));

      case 2:
        if (filteredListCampSites.get(row).estimatedCheckOut == null)
          return "-";

        return (formatter.format(filteredListCampSites.get(row).estimatedCheckOut.
            getTime()));


      case 3:
        if (filteredListCampSites.get(row).daysSince(refDate) != 0)
          return filteredListCampSites.get(row).daysSince(refDate) * -1;
        return filteredListCampSites.get(row).daysSince(refDate);
    }
    return null;
  }

  private Object rvTentScreen(int row, int col) {
    switch (col) {
      case 0:
        return (filteredListCampSites.get(row).guestName);

      case 1:
        return (filteredListCampSites.get(row).getCost(filteredListCampSites.
            get(row).estimatedCheckOut));

      case 2:
        return (formatter.format(filteredListCampSites.get(row).checkIn.getTime()));

      case 3:
        if (filteredListCampSites.get(row).estimatedCheckOut == null)
          return "-";

        return (formatter.format(filteredListCampSites.get(row).estimatedCheckOut.
            getTime()));

      case 4:
      case 5:
        if (filteredListCampSites.get(row) instanceof RV)
          if (col == 4)
            return (((RV) filteredListCampSites.get(row)).getPower());
          else
            return "";

        else {
          if (col == 5)
            return (((TentOnly) filteredListCampSites.get(row)).
                getNumberOfTenters());
          else
            return "";
        }
      default:
        throw new RuntimeException("Row,col out of range: " + row + " " + col);
    }
  }

    private Object tentRvScreen(int row, int col) {
      switch (col) {
        case 0:
          return (filteredListCampSites.get(row).guestName);

        case 1:
          return (filteredListCampSites.get(row).getCost(filteredListCampSites.
              get(row).estimatedCheckOut));

        case 2:
          return (formatter.format(filteredListCampSites.get(row).checkIn.getTime()));

        case 3:
          if (filteredListCampSites.get(row).estimatedCheckOut == null)
            return "-";

          return (formatter.format(filteredListCampSites.get(row).estimatedCheckOut.
              getTime()));

        case 4:
        case 5:
          if (filteredListCampSites.get(row) instanceof RV)
            if (col == 4)
              return (((RV) filteredListCampSites.get(row)).getPower());
            else
              return "";

          else {
            if (col == 5)
              return (((TentOnly) filteredListCampSites.get(row)).
                  getNumberOfTenters());
            else
              return "";
          }
        default:
          throw new RuntimeException("Row,col out of range: " + row + " " + col);
      }
  }

  public void add(CampSite a) {
    listCampSites.add(a);
    UpdateScreen();
  }

  public void setRefDate(Date refDate) {
    this.refDate = refDate;
  }

  public CampSite get(int i) {
    return filteredListCampSites.get(i);
  }

  public void upDate(int index, CampSite unit) {
    UpdateScreen();
  }

  public void saveDatabase(String filename) {
    try {
      FileOutputStream fos = new FileOutputStream(filename);
      ObjectOutputStream os = new ObjectOutputStream(fos);
      os.writeObject(listCampSites);
      os.close();
    } catch (IOException ex) {
      throw new RuntimeException("Saving problem! " + display);

    }
  }

  public void saveTextFile (String filename) {
    try {

      FileOutputStream file = new FileOutputStream(filename);
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(file));

      for (int i = 0; i < listCampSites.size(); ++i) {
        writer.write(listCampSites.get(i).getGuestType() + ", ");
        writer.write(listCampSites.get(i).getGuestName() + ", ");

        writer.write(listCampSites.get(i).getCheckIn().get(Calendar.MONTH) + "/" +
            listCampSites.get(i).getCheckIn().get(Calendar.DAY_OF_MONTH) + "/" +
            listCampSites.get(i).getCheckIn().get(Calendar.YEAR) + ", ");

        writer.write(listCampSites.get(i).getEstimatedCheckOut().get(Calendar.MONTH) + "/" +
            listCampSites.get(i).getEstimatedCheckOut().get(Calendar.DAY_OF_MONTH) + "/" +
            listCampSites.get(i).getEstimatedCheckOut().get(Calendar.YEAR) + ", ");

        if (listCampSites.get(i).getActualCheckOut() != null) {
          writer.write(listCampSites.get(i).getActualCheckOut().get(Calendar.MONTH) + "/" +
              listCampSites.get(i).getActualCheckOut().get(Calendar.DAY_OF_MONTH) + "/" +
              listCampSites.get(i).getActualCheckOut().get(Calendar.YEAR) + ", ");
        } else {
          writer.write(listCampSites.get(i).getActualCheckOut() + ", ");
        }

        if (listCampSites.get(i).getGuestType() == 0) {
          TentOnly t = (TentOnly) listCampSites.get(i);
          writer.write(t.getNumberOfTenters() + "");
        }

        if (listCampSites.get(i).getGuestType() == 1) {
          RV r = (RV) listCampSites.get(i);
          writer.write(r.getPower() + "");
        }

        writer.newLine();
      }
      writer.close();
      file.close();

    } catch (IOException ex) {
      throw new RuntimeException("Saving problem! " + display);
    }

  }

  public void loadDatabase(String filename) {
    listCampSites.clear();

    try {
      FileInputStream fis = new FileInputStream(filename);
      ObjectInputStream is = new ObjectInputStream(fis);

      listCampSites = (ArrayList<CampSite>) is.readObject();
      UpdateScreen();
      is.close();
    } catch (Exception ex) {
      throw new RuntimeException("Loading problem: " + display);

    }
  }

  public void loadTextFile (String filename) {
    String line = null;

    String[] lineArray;
    String[] checkInArray;
    String[] estCheckOutArray;
    String[] checkOutArray;

    listCampSites.clear();

    try {

      FileReader file = new FileReader(filename);
      BufferedReader reader = new BufferedReader(file);

      while ((line = reader.readLine()) != null) {
        lineArray = line.split(", ");

        checkInArray = lineArray[2].split("/");
        estCheckOutArray = lineArray[3].split("/");
        checkOutArray = lineArray[4].split("/");

          if (Integer.parseInt(lineArray[0]) == 0) {    // IF TENTONLY
            TentOnly temp = new TentOnly();

            temp.setGuestType(Integer.parseInt(lineArray[0])); // Guest Type

            temp.setGuestName(lineArray[1]); // Guest Name

            temp.setCheckIn(new GregorianCalendar( // Check In
                Integer.parseInt(checkInArray[2]),
                Integer.parseInt(checkInArray[0]),
                Integer.parseInt(checkInArray[1])
            ));


            temp.setEstimatedCheckOut(new GregorianCalendar( // Est Check Out
                Integer.parseInt(estCheckOutArray[2]),
                Integer.parseInt(estCheckOutArray[0]),
                Integer.parseInt(estCheckOutArray[1])
            ));

            if (lineArray[4].contains("null")) {
              temp.setActualCheckOut(null);
            } else {
              temp.setEstimatedCheckOut(new GregorianCalendar( // Check Out
                  Integer.parseInt(checkOutArray[2]),
                  Integer.parseInt(checkOutArray[0]),
                  Integer.parseInt(checkOutArray[1])
              ));
            }

            temp.setNumberOfTenters(Integer.parseInt(lineArray[5]));  // Tenters

            add(temp);
          }

          if (Integer.parseInt(lineArray[0]) == 1) {    // IF RV
            RV temp = new RV();


            temp.setGuestType(Integer.parseInt(lineArray[0])); // Guest Type

            temp.setGuestName(lineArray[1]); // Guest Name

            temp.setCheckIn(new GregorianCalendar( // Check In
                Integer.parseInt(checkInArray[2]),
                Integer.parseInt(checkInArray[0]),
                Integer.parseInt(checkInArray[1])
            ));


            temp.setEstimatedCheckOut(new GregorianCalendar( // Est Check Out
                Integer.parseInt(estCheckOutArray[2]),
                Integer.parseInt(estCheckOutArray[0]),
                Integer.parseInt(estCheckOutArray[1])
            ));

            if (lineArray[4].contains("null")) {
              temp.setActualCheckOut(null);
            } else {
              temp.setEstimatedCheckOut(new GregorianCalendar( // Check Out
                  Integer.parseInt(checkOutArray[2]),
                  Integer.parseInt(checkOutArray[0]),
                  Integer.parseInt(checkOutArray[1])
              ));
            }

            temp.setPower(Integer.parseInt(lineArray[5]));

            add(temp);
          }
        }

      UpdateScreen();
      reader.close();

    } catch (FileNotFoundException ex) {
      throw new RuntimeException("Error reading file! " + display);
    } catch (Exception ex) {
      throw new RuntimeException("Loading problem! " + display);
    }
  }

  public void createList() {
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    GregorianCalendar g1 = new GregorianCalendar();
    GregorianCalendar g2 = new GregorianCalendar();
    GregorianCalendar g3 = new GregorianCalendar();
    GregorianCalendar g4 = new GregorianCalendar();
    GregorianCalendar g5 = new GregorianCalendar();
    GregorianCalendar g6 = new GregorianCalendar();

    try {
      Date d1 = df.parse("1/20/2020");
      g1.setTime(d1);
      Date d2 = df.parse("12/22/2020");
      g2.setTime(d2);
      Date d3 = df.parse("12/20/2019");
      g3.setTime(d3);
      Date d4 = df.parse("3/25/2020");
      g4.setTime(d4);
      Date d5 = df.parse("1/20/2010");
      g5.setTime(d5);
      Date d6 = df.parse("3/29/2020");
      g6.setTime(d6);

      TentOnly tentOnly1 = new TentOnly("T1", g3, g2, null, 4);
      TentOnly tentOnly11 = new TentOnly("T1", g3, g1, null, 8);
      TentOnly tentOnly111 = new TentOnly("T1", g5, g3, null, 8);
      TentOnly tentOnly2 = new TentOnly("T2", g5, g3, null, 5);
      TentOnly tentOnly3 = new TentOnly("T3", g3, g1, g1, 7);

      RV RV1 = new RV("RV1", g4, g6, null, 1000);
      RV RV2 = new RV("RV2", g5, g3, null, 1000);
      RV RV22 = new RV("RV2", g3, g1, null, 2000);
      RV RV222 = new RV("RV2", g3, g6, null, 2000);
      RV RV3 = new RV("RV3", g5, g4, g3, 1000);

      add(tentOnly1);
      add(tentOnly2);
      add(tentOnly3);
      add(tentOnly11);
      add(tentOnly111);

      add(RV1);
      add(RV2);
      add(RV3);
      add(RV22);
      add(RV222);

    } catch (ParseException e) {
      throw new RuntimeException("Error in testing, creation of list");
    }
  }
}


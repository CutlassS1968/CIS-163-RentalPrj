import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**********************************************************************************************
 *
 * ListModel contains the core calculations, sorting, organization,
 * and displaying of campsites in the CampSite database
 *
 * @author Evan Johns
 * @author Austin Ackerman
 * @version 03/23/2020
 *
 **********************************************************************************************/

public class ListModel extends AbstractTableModel {


  /**
   * lists used for organizing camp sites
   */
  private ArrayList<CampSite> listCampSites;
  private ArrayList<CampSite> filteredListCampSites;


  /**
   * current display active
   */
  private ScreenDisplay display;


  /**
   * Column names for all displays
   */
  private String[] columnNamesForCurrentPark = {"Guest Name", "Est. Cost",
      "Check In Date", "Est. Check Out Date", "Max Power", "Num. of Tenters"};

  private String[] columnNamesForCheckouts = {"Guest Name", "Est. Cost",
      "Check In Date", "Actual Check Out Date", "Real Cost"};

  private String[] columnNamesForOverDue = {"Guest Name", "Est. Cost",
      "Est. Check Out Date", "Days Over Due"};

  private String[] columnNamesForRvTent = columnNamesForCurrentPark;

  private String[] columnNamesForTentRv = columnNamesForCurrentPark;


  /**
   * Date formatter for quick formatting of dates
   */
  private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");


  /**
   * User Input reference date used for calculating days overdue
   */
  private Date refDate;


  /**
   * Emergency break, use only in emergencies (testing)
   */
  private Boolean eBreak;


  /*********************************************************************************************
   *
   * Instantiates ListModel's instance variables
   *
   *********************************************************************************************/
  public ListModel() {
    super();
    display = ScreenDisplay.CurrentParkStatus;
    listCampSites = new ArrayList<CampSite>();
    eBreak = false;
    UpdateScreen();
    createList();
  }

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
    throw new RuntimeException("Undefined column count for Display: " + display);
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
    throw new RuntimeException("Undefined state for Display: " + display);
  }

  public void setDisplay(ScreenDisplay selected) {
    display = selected;
    UpdateScreen();
  }

  /**
   * Updates screen based on the current display status that is set in GUICampReservationSystem
   * .java and sorts the list of Camp Sites based on the current display
   */
  private void UpdateScreen() {
    switch (display) {
      case CurrentParkStatus: // Organizes by Guest name
        filteredListCampSites = (ArrayList<CampSite>) listCampSites.stream().
            filter(n -> n.actualCheckOut == null).collect(Collectors.toList());

        Collections.sort(filteredListCampSites, (n1, n2) -> n1.getGuestName().compareTo(n2.guestName));
        break;

      case CheckOutGuest: // Organizes by Guest name
        filteredListCampSites = (ArrayList<CampSite>) listCampSites.stream().
            filter(n -> n.getActualCheckOut() != null).collect(Collectors.toList());

        Collections.sort(filteredListCampSites, new Comparator<CampSite>() {
          @Override
          public int compare(CampSite n1, CampSite n2) {
            return n1.getGuestName().compareTo(n2.guestName);
          }
        });

        break;

      case OverDueGuest:  // Organizes by Estimated Cost
        filteredListCampSites = (ArrayList<CampSite>) listCampSites.stream().
            filter(n -> n.actualCheckOut == null).collect(Collectors.toList());

        Collections.sort(filteredListCampSites,
            (n1, n2) -> n1.getEstimatedCheckOut().compareTo(n2.getEstimatedCheckOut()));

        break;

      case RvTent:  // Organizes first by Rv>Tent and then by Guest name
        filteredListCampSites = (ArrayList<CampSite>) listCampSites.stream().
            filter(n -> n.actualCheckOut == null).collect(Collectors.toList());

//         Alternatively you could use streams to sort the function, which is much easier than
//         using lambdas or anonymous functions
//
//         filteredListCampSites.stream().sorted(Comparator.comparing(CampSite::getGuestType)
//         .thenComparing(CampSite::getGuestName));

        Collections.sort(filteredListCampSites,
            (n1, n2) -> {
              if (n1.getGuestType() != 1 && n2.getGuestType() != 1) {
                return Integer.toString(n1.getGuestType()).compareTo(Integer.toString(n2.getGuestType()));
              } else {
                return n1.getGuestName().compareTo(n2.getGuestName());
              }
            });
        break;

      case TentRv:  // Organizes first by Tent>Rv and then by Guest name
        filteredListCampSites = (ArrayList<CampSite>) listCampSites.stream().
            filter(n -> n.actualCheckOut == null).collect(Collectors.toList());

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

  /**
   * Returns the value in the table at a given row and column for the Current Park Screen
   *
   * @param row Row in table
   * @param col Column in table
   * @return returns the object at that index
   */
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
        if (filteredListCampSites.get(row).estimatedCheckOut == null) {
          return "-";
        }

        return (formatter.format(filteredListCampSites.get(row).estimatedCheckOut.
            getTime()));

      case 4:
      case 5:
        if (filteredListCampSites.get(row) instanceof RV) {
          if (col == 4) {
            return (((RV) filteredListCampSites.get(row)).getPower());
          } else {
            return "";
          }
        } else {
          if (col == 5) {
            return (((TentOnly) filteredListCampSites.get(row)).
                getNumberOfTenters());
          } else {
            return "";
          }
        }
      default:
        throw new RuntimeException("Row,col out of range: " + row + " " + col);
    }
  }

  /**
   * Returns the value in the table at a given row and column for the Check Out Screen
   *
   * @param row Row in table
   * @param col Column in table
   * @return returns the object at that index
   */
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

  /**
   * Returns the value in the table at a given row and column for the Over Due Screen
   *
   * @param row Row in table
   * @param col Column in table
   * @return returns the object at that index
   */
  private Object overDueScreen(int row, int col) {
    switch (col) {
      case 0:
        return (filteredListCampSites.get(row).guestName);

      case 1:
        return (filteredListCampSites.get(row).getCost(filteredListCampSites.
            get(row).estimatedCheckOut));

      case 2:
        if (filteredListCampSites.get(row).estimatedCheckOut == null) {
          return "-";
        }

        return (formatter.format(filteredListCampSites.get(row).estimatedCheckOut.
            getTime()));


      case 3:
        if (filteredListCampSites.get(row).daysSince(refDate) != 0) {
          return filteredListCampSites.get(row).daysSince(refDate) * -1;
        }
        return filteredListCampSites.get(row).daysSince(refDate);

      default:
        throw new RuntimeException("Row,col out of range: " + row + " " + col);
    }
  }

  /**
   * Returns the value in the table at a given row and column for the RvTent Screen
   *
   * @param row Row in table
   * @param col Column in table
   * @return returns the object at that index
   */
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
        if (filteredListCampSites.get(row).estimatedCheckOut == null) {
          return "-";
        }

        return (formatter.format(filteredListCampSites.get(row).estimatedCheckOut.
            getTime()));

      case 4:
      case 5:
        if (filteredListCampSites.get(row) instanceof RV) {
          if (col == 4) {
            return (((RV) filteredListCampSites.get(row)).getPower());
          } else {
            return "";
          }
        } else {
          if (col == 5) {
            return (((TentOnly) filteredListCampSites.get(row)).
                getNumberOfTenters());
          } else {
            return "";
          }
        }
      default:
        throw new RuntimeException("Row,col out of range: " + row + " " + col);
    }
  }

  /**
   * Returns the value in the table at a given row and column for the TentRv Screen
   *
   * @param row Row in table
   * @param col Column in table
   * @return returns the object at that index
   */
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
        if (filteredListCampSites.get(row).estimatedCheckOut == null) {
          return "-";
        }
        return (formatter.format(filteredListCampSites.get(row).estimatedCheckOut.getTime()));

      case 4:
      case 5:
        if (filteredListCampSites.get(row) instanceof RV) {
          if (col == 4) {
            return (((RV) filteredListCampSites.get(row)).getPower());
          } else {
            return "";
          }
        } else {
          if (col == 5) {
            return (((TentOnly) filteredListCampSites.get(row)).
                getNumberOfTenters());
          } else {
            return "";
          }
        }
      default:
        throw new RuntimeException("Row,col out of range: " + row + " " + col);
    }
  }

  /**
   * Saves current database to a serialized file
   *
   * @param filename name of serialized file
   */
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

  /**
   * Saves current database to a text file
   *
   * @param filename name of text file
   */
  public void saveTextFile(String filename) {
    try {

      FileOutputStream file = new FileOutputStream(filename);
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(file));

      for (int i = 0; i < listCampSites.size(); ++i) {
        writer.write(listCampSites.get(i).getGuestType() + ", ");
        writer.write(listCampSites.get(i).getGuestName() + ", ");

        writer.write(formatter.format(listCampSites.get(i).getCheckIn().getTime()) + ", ");

        writer.write(formatter.format
            (listCampSites.get(i).getEstimatedCheckOut().getTime()) + "," + " ");


        if (listCampSites.get(i).getActualCheckOut() != null) {
          writer.write(formatter.format
              (listCampSites.get(i).getActualCheckOut().getTime()) + ", ");
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

  /**
   * Loads a serialized database file
   *
   * @param filename name of database file
   */
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

  /**
   * Loads a text database file
   *
   * @param filename name of database file
   */
  public void loadTextFile(String filename) {
    String line = null;
    String[] lineArray;

    GregorianCalendar checkIn = new GregorianCalendar();
    GregorianCalendar estCheckOut = new GregorianCalendar();
    GregorianCalendar checkOut = new GregorianCalendar();

    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

    listCampSites.clear();

    try {

      FileReader file = new FileReader(filename);
      BufferedReader reader = new BufferedReader(file);

      while ((line = reader.readLine()) != null) {

        lineArray = line.split(", ");

        checkIn.setTime(df.parse(lineArray[2]));

        estCheckOut.setTime(df.parse(lineArray[3]));

        if (!lineArray[4].contains("null")) {
          checkOut.setTime(df.parse(lineArray[4]));
        } else checkOut = null;

        if (Integer.parseInt(lineArray[0]) == 0) {
          TentOnly temp = new TentOnly(lineArray[1], checkIn, estCheckOut, checkOut,
              Integer.parseInt(lineArray[5]));
          temp.setNumberOfTenters(Integer.parseInt(lineArray[5]));
          add(temp);
        }

        if (Integer.parseInt(lineArray[0]) == 1) {
          RV temp = new RV(lineArray[1], checkIn, estCheckOut, checkOut,
              Integer.parseInt(lineArray[5]));
          temp.setPower(Integer.parseInt(lineArray[5]));
          add(temp);
        }

        checkOut = new GregorianCalendar();
        checkIn = new GregorianCalendar();
        estCheckOut = new GregorianCalendar();
      }

      UpdateScreen();
      reader.close();

    } catch (FileNotFoundException ex) {
      throw new RuntimeException("Error reading file! " + display);
    } catch (Exception ex) {
      throw new RuntimeException("Loading problem! " + display);
    }
  }

  public void add(CampSite a) {
    listCampSites.add(a);
    UpdateScreen();
  }

  public CampSite get(int i) {
    return filteredListCampSites.get(i);
  }

  public void setRefDate(Date refDate) {
    this.refDate = refDate;
  }

  public Date getRefDate() {
    return refDate;
  }

  public void setListCampSites(ArrayList<CampSite> listCampSites) {
    this.listCampSites = listCampSites;
  }

  public ArrayList<CampSite> getListCampSites() {
    return listCampSites;
  }

  public void setEBreak(Boolean eBreak) {
    this.eBreak = eBreak;
  }

  public Boolean getEBreak() {
    return eBreak;
  }

  public void setFilteredListCampSites(ArrayList<CampSite> filteredListCampSites) {
    this.filteredListCampSites = filteredListCampSites;
  }

  public ArrayList<CampSite> getFilteredListCampSites() {
    return filteredListCampSites;
  }

  public void upDate(int index, CampSite unit) {
    UpdateScreen();
  }

  /**
   * Creates the current list of CampSites in the Database
   */
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

      if (eBreak) {
        throw new ParseException("Oops", 0);
      }

    } catch (ParseException e) {
      throw new RuntimeException("Error in testing, creation of list");
    }
  }
}


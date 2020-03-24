import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

public class ListModelTest {

  private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

  @Test
  public void setUp() {
  }

  // Checking that setDisplays are sorted correctly
  @Test
  public void setDisplay() {
    ListModel list = new ListModel();

    for (int i = 0; i < list.getFilteredListCampSites().size(); ++i) {
      list.setDisplay(ScreenDisplay.CurrentParkStatus);
      assert (list.getFilteredListCampSites().get(i).getActualCheckOut() == null);
      list.setDisplay(ScreenDisplay.CheckOutGuest);
      assert (list.getFilteredListCampSites().get(i).getActualCheckOut() != null);
    }

    for (int i = 0; i < 4; ++i) {
      list.setDisplay(ScreenDisplay.TentRv);
      assert (list.getFilteredListCampSites().get(i).getGuestType() == 0);
      list.setDisplay(ScreenDisplay.RvTent);
      assert (list.getFilteredListCampSites().get(i).getGuestType() == 1);
    }
  }

  // Makes sure that there are the proper amount
  // of columns for each screen
  @Test
  public void getColumnCount() {
    ListModel list = new ListModel();

    list.setDisplay(ScreenDisplay.CurrentParkStatus);
    assertEquals(list.getColumnCount(), 6);

    list.setDisplay(ScreenDisplay.CheckOutGuest);
    assertEquals(list.getColumnCount(), 5);

    list.setDisplay(ScreenDisplay.RvTent);
    assertEquals(list.getColumnCount(), 6);

    list.setDisplay(ScreenDisplay.TentRv);
    assertEquals(list.getColumnCount(), 6);
  }

  // Makes sure that there are the proper amount
  // of columns for each screen
  @Test
  public void getRowCount() {
    ListModel list = new ListModel();

    list.setDisplay(ScreenDisplay.CurrentParkStatus);
    assertEquals(list.getRowCount(), 8);

    list.setDisplay(ScreenDisplay.CheckOutGuest);
    assertEquals(list.getRowCount(), 2);

    list.setDisplay(ScreenDisplay.RvTent);
    assertEquals(list.getRowCount(), 8);

    list.setDisplay(ScreenDisplay.TentRv);
    assertEquals(list.getRowCount(), 8);
  }

  @Test
  public void getValueAtCurrentPark() {
    ListModel list = new ListModel();

    list.setDisplay(ScreenDisplay.CurrentParkStatus);
    for (int r = 0; r < list.getFilteredListCampSites().size(); ++r) {

      if (r < 4) {
        assert (list.getValueAt(r, 0).toString().contains("RV"));
        assertEquals(list.getValueAt(r, 4),
            ((RV) list.getFilteredListCampSites().get(r)).getPower());
        assertEquals(list.getValueAt(r, 5), "");
      } else {
        assert (list.getValueAt(r, 0).toString().contains("T"));
        assertEquals(list.getValueAt(r, 4), "");
        assertEquals(list.getValueAt(r, 5),
            ((TentOnly) list.getFilteredListCampSites().get(r)).getNumberOfTenters());
      }

      assertEquals(list.getValueAt(r, 1),
          list.getFilteredListCampSites().get(r).
              getCost(list.getFilteredListCampSites().get(r).getEstimatedCheckOut()));

      assertEquals(list.getValueAt(r, 2),
          formatter.format(list.getFilteredListCampSites().get(r).checkIn.getTime()));

      assertEquals(list.getValueAt(r, 3),
          formatter.format(list.getFilteredListCampSites().get(r).getEstimatedCheckOut().getTime()));
    }
  }

  @Test
  public void getValueAtCheckOut() {
    ListModel list = new ListModel();

    list.setDisplay(ScreenDisplay.CheckOutGuest);
    for (int r = 0; r < list.getFilteredListCampSites().size(); ++r) {

      if (r < 1) {
        assert (list.getValueAt(r, 0).toString().contains("RV"));
      } else {
        assert (list.getValueAt(r, 0).toString().contains("T"));
      }

      assertEquals(list.getValueAt(r, 1),
          list.getFilteredListCampSites().get(r).
              getCost(list.getFilteredListCampSites().get(r).getEstimatedCheckOut()));

      assertEquals(list.getValueAt(r, 2),
          formatter.format(list.getFilteredListCampSites().get(r).checkIn.getTime()));

      assertEquals(list.getValueAt(r, 3),
          formatter.format(list.getFilteredListCampSites().get(r).getActualCheckOut().getTime()));
    }
  }

  @Test
  public void getValueAtOverDue() {
    ListModel list = new ListModel();

    list.setDisplay(ScreenDisplay.OverDueGuest);
    list.setRefDate(new GregorianCalendar(2000, 1, 29).getTime());

    for (int r = 0; r < list.getFilteredListCampSites().size(); ++r) {

      assertEquals(list.getValueAt(r, 1),
          list.getFilteredListCampSites().get(r).
              getCost(list.getFilteredListCampSites().get(r).getEstimatedCheckOut()));

      assertEquals(list.getValueAt(r, 2),
          formatter.format(list.getFilteredListCampSites().get(r).getEstimatedCheckOut().getTime()));

      assertEquals(list.getValueAt(r, 3),
          (-1 * list.getFilteredListCampSites().get(r).daysSince(list.getRefDate())));
    }
  }

  @Test
  public void getValueAtRvTent() {
    ListModel list = new ListModel();

    list.setDisplay(ScreenDisplay.RvTent);
    for (int r = 0; r < list.getFilteredListCampSites().size(); ++r) {

      if (r < 4) {
        assert (list.getValueAt(r, 0).toString().contains("RV"));
        assertEquals(list.getValueAt(r, 4),
            ((RV) list.getFilteredListCampSites().get(r)).getPower());
        assertEquals(list.getValueAt(r, 5), "");
      } else {
        assert (list.getValueAt(r, 0).toString().contains("T"));
        assertEquals(list.getValueAt(r, 4), "");
        assertEquals(list.getValueAt(r, 5),
            ((TentOnly) list.getFilteredListCampSites().get(r)).getNumberOfTenters());
      }

      assertEquals(list.getValueAt(r, 1),
          list.getFilteredListCampSites().get(r).
              getCost(list.getFilteredListCampSites().get(r).getEstimatedCheckOut()));

      assertEquals(list.getValueAt(r, 2),
          formatter.format(list.getFilteredListCampSites().get(r).checkIn.getTime()));

      assertEquals(list.getValueAt(r, 3),
          formatter.format(list.getFilteredListCampSites().get(r).getEstimatedCheckOut().getTime()));
    }
  }

  @Test
  public void getValueAtTentRv() {
    ListModel list = new ListModel();

    list.setDisplay(ScreenDisplay.TentRv);
    for (int r = 0; r < list.getFilteredListCampSites().size(); ++r) {

      if (r < 4) {
        assert (list.getValueAt(r, 0).toString().contains("T"));
        assertEquals(list.getValueAt(r, 5),
            ((TentOnly) list.getFilteredListCampSites().get(r)).getNumberOfTenters());
        assertEquals(list.getValueAt(r, 4), "");
      } else {
        assert (list.getValueAt(r, 0).toString().contains("RV"));
        assertEquals(list.getValueAt(r, 4),
            ((RV) list.getFilteredListCampSites().get(r)).getPower());
        assertEquals(list.getValueAt(r, 5), "");

      }

      assertEquals(list.getValueAt(r, 1),
          list.getFilteredListCampSites().get(r).
              getCost(list.getFilteredListCampSites().get(r).getEstimatedCheckOut()));

      assertEquals(list.getValueAt(r, 2),
          formatter.format(list.getFilteredListCampSites().get(r).checkIn.getTime()));

      assertEquals(list.getValueAt(r, 3),
          formatter.format(list.getFilteredListCampSites().get(r).getEstimatedCheckOut().getTime()));
    }
  }

  @Test
  public void getColumnCountOverDue() {
    ListModel list = new ListModel();
    list.setDisplay(ScreenDisplay.OverDueGuest);
    list.setRefDate(new GregorianCalendar().getTime());

    assertEquals(list.getColumnCount(), 4);
  }

  @Test
  public void currentParkScreenEstimatedCheckOutNull() {
    ListModel list = new ListModel();

    GregorianCalendar g1 = new GregorianCalendar(2020, 2, 20);

    list.add(new TentOnly("ABC", g1, null, null, 10));

    list.setDisplay(ScreenDisplay.CurrentParkStatus);

    assertEquals(list.getValueAt(0, 3), "-");
  }

  @Test
  public void checkOutScreenCost() {
    ListModel list = new ListModel();
    list.setDisplay(ScreenDisplay.CheckOutGuest);

    assertEquals(list.getValueAt(0, 4), 72430.0);
  }

  @Test
  public void overDueScreenLooseEnds() {
    ListModel list = new ListModel();
    list.setRefDate(new GregorianCalendar(2000, 1, 29).getTime());
    list.setDisplay(ScreenDisplay.OverDueGuest);


    assertEquals(list.getValueAt(0, 0), "T2");
    System.out.println(list.getValueAt(0, 3));
    assertEquals(list.getValueAt(0, 3), -7234);

    list.setRefDate(new GregorianCalendar(2021, 1, 29).getTime());

    assertEquals(list.getValueAt(0, 3), 437);
  }

  @Test
  public void getColumnNameCurrentParkStatus() {
    ListModel list = new ListModel();

    list.setDisplay(ScreenDisplay.CurrentParkStatus);

    assert (list.getColumnName(0).equals("Guest Name"));
    assert (list.getColumnName(1).equals("Est. Cost"));
    assert (list.getColumnName(2).equals("Check In Date"));
    assert (list.getColumnName(3).equals("Est. Check Out Date"));
    assert (list.getColumnName(4).equals("Max Power"));
    assert (list.getColumnName(5).equals("Num. of Tenters"));

  }

  @Test
  public void getColumnNameCheckOutGuest() {
    ListModel list = new ListModel();

    list.setDisplay(ScreenDisplay.CheckOutGuest);

    assert (list.getColumnName(0).equals("Guest Name"));
    assert (list.getColumnName(1).equals("Est. Cost"));
    assert (list.getColumnName(2).equals("Check In Date"));
    assert (list.getColumnName(3).equals("Actual Check Out Date"));
    assert (list.getColumnName(4).equals("Real Cost"));
  }

  @Test
  public void getColumnNameOverDue() {
    ListModel list = new ListModel();
    list.setRefDate(new GregorianCalendar().getTime());

    list.setDisplay(ScreenDisplay.OverDueGuest);

    assert (list.getColumnName(0).equals("Guest Name"));
    assert (list.getColumnName(1).equals("Est. Cost"));
    assert (list.getColumnName(2).equals("Est. Check Out Date"));
    assert (list.getColumnName(3).equals("Days Over Due"));
  }

  @Test
  public void getColumnNameRvTent() {
    ListModel list = new ListModel();

    list.setDisplay(ScreenDisplay.RvTent);

    assert (list.getColumnName(0).equals("Guest Name"));
    assert (list.getColumnName(1).equals("Est. Cost"));
    assert (list.getColumnName(2).equals("Check In Date"));
    assert (list.getColumnName(3).equals("Est. Check Out Date"));
    assert (list.getColumnName(4).equals("Max Power"));
    assert (list.getColumnName(5).equals("Num. of Tenters"));
  }

  @Test
  public void getColumnNameTentRv() {
    ListModel list = new ListModel();

    list.setDisplay(ScreenDisplay.TentRv);

    assert (list.getColumnName(0).equals("Guest Name"));
    assert (list.getColumnName(1).equals("Est. Cost"));
    assert (list.getColumnName(2).equals("Check In Date"));
    assert (list.getColumnName(3).equals("Est. Check Out Date"));
    assert (list.getColumnName(4).equals("Max Power"));
    assert (list.getColumnName(5).equals("Num. of Tenters"));
  }

  @Test
  public void checkedOutAllGuests() {
    ListModel list = new ListModel();
    int guests;

    list.setDisplay(ScreenDisplay.CheckOutGuest);
    guests = list.getFilteredListCampSites().size();
    assert (guests == 2);

    list.setDisplay(ScreenDisplay.CurrentParkStatus);
    guests = list.getFilteredListCampSites().size();
    assert (guests == 8);

    for (int i = 0; i < guests; ++i) {
      list.get(0).setActualCheckOut(new GregorianCalendar());
      list.upDate(i, list.getFilteredListCampSites().get(0));
      assertEquals(list.getFilteredListCampSites().size(), (guests - (i + 1)));
    }

    guests = list.getFilteredListCampSites().size();
    assert (guests == 0);

    list.setDisplay(ScreenDisplay.CheckOutGuest);

    guests = list.getFilteredListCampSites().size();
    assert (guests == 10);
  }


  @Test
  public void add() {
    ListModel list = new ListModel();

    list.setDisplay(ScreenDisplay.CurrentParkStatus);

    for (int i = 0; i < 10; ++i) {
      list.add(new TentOnly(String.valueOf(i), new GregorianCalendar(),
          new GregorianCalendar(), null, 10));
      list.add(new RV(String.valueOf(i), new GregorianCalendar(),
          new GregorianCalendar(), null, 1000));
    }

    assertEquals(28, list.getFilteredListCampSites().size());
  }

  // Testing the get() method by getting campsites
  // from the default filtered CampSite list
  @Test
  public void get() {
    ListModel list = new ListModel();
    list.setDisplay(ScreenDisplay.CurrentParkStatus);

    for (int i = 0; i < list.getFilteredListCampSites().size(); ++i) {
      if (i < 4) assert (list.get(i) instanceof RV);
      else assert (list.get(i) instanceof TentOnly);
    }
  }

  @Test
  public void saveDatabase() {
    GregorianCalendar g1 = new GregorianCalendar(2000, 1, 29);
    GregorianCalendar g2 = new GregorianCalendar(2020, 3, 23);
    RV rv = new RV("Test_RV", g1, g2, null, 1000);

    ListModel l1 = new ListModel();
    l1.add(rv);

    ListModel l2 = new ListModel();

    l1.saveDatabase("saveDatabase_Test");
    l2.loadDatabase("saveDatabase_Test");

    for (int i = 0; i < l1.getListCampSites().size(); ++i) {
      assertEquals(l1.getListCampSites().get(i).getActualCheckOut(),
          l2.getListCampSites().get(i).getActualCheckOut());

      assertEquals(l1.getListCampSites().get(i).getGuestType(),
          l2.getListCampSites().get(i).getGuestType());

      assertEquals(l1.getListCampSites().get(i).getEstimatedCheckOut(),
          l2.getListCampSites().get(i).getEstimatedCheckOut());

      assertEquals(l1.getListCampSites().get(i).getGuestName(),
          l2.getListCampSites().get(i).getGuestName());

      assertEquals(l1.getListCampSites().get(i).getCheckIn(),
          l2.getListCampSites().get(i).getCheckIn());
    }
  }

  @Test
  public void loadDatabase() {
    GregorianCalendar g1 = new GregorianCalendar(2000, 1, 29);
    GregorianCalendar g2 = new GregorianCalendar(2020, 3, 23);
    RV rv = new RV("Test_RV", g1, g2, null, 1000);

    ListModel l1 = new ListModel();
    l1.add(rv);

    l1.saveDatabase("loadDatabase_Test");

    ListModel l2 = new ListModel();
    l2.add(rv);

    l1 = new ListModel();
    l1.loadDatabase("loadDatabase_Test");

    for (int i = 0; i < l1.getListCampSites().size(); ++i) {
      assertEquals(l1.getListCampSites().get(i).getActualCheckOut(),
          l2.getListCampSites().get(i).getActualCheckOut());

      assertEquals(l1.getListCampSites().get(i).getGuestType(),
          l2.getListCampSites().get(i).getGuestType());

      assertEquals(l1.getListCampSites().get(i).getEstimatedCheckOut(),
          l2.getListCampSites().get(i).getEstimatedCheckOut());

      assertEquals(l1.getListCampSites().get(i).getGuestName(),
          l2.getListCampSites().get(i).getGuestName());

      assertEquals(l1.getListCampSites().get(i).getCheckIn(),
          l2.getListCampSites().get(i).getCheckIn());
    }
  }

  @Test
  public void saveAsText() {
    GregorianCalendar g1 = new GregorianCalendar(2000, 1, 29);
    GregorianCalendar g2 = new GregorianCalendar(2020, 3, 23);

    RV rv = new RV("Test", g1, g2, null, 10);

    ListModel l1 = new ListModel();
    l1.add(rv);

    ListModel l2 = new ListModel();

    l1.saveTextFile("SaveAsTextTest.txt");
    l2.loadTextFile("SaveAsTextTest.txt");

    for (int i = 0; i < l1.getListCampSites().size(); ++i) {
      assertEquals(l1.getListCampSites().get(i).getActualCheckOut(),
          l2.getListCampSites().get(i).getActualCheckOut());

      assertEquals(l1.getListCampSites().get(i).getGuestType(),
          l2.getListCampSites().get(i).getGuestType());

      assertEquals(l1.getListCampSites().get(i).getEstimatedCheckOut(),
          l2.getListCampSites().get(i).getEstimatedCheckOut());

      assertEquals(l1.getListCampSites().get(i).getGuestName(),
          l2.getListCampSites().get(i).getGuestName());

      assertEquals(l1.getListCampSites().get(i).getCheckIn(),
          l2.getListCampSites().get(i).getCheckIn());
    }
  }

  @Test
  public void loadFromText() {
    GregorianCalendar g1 = new GregorianCalendar(2000, 1, 29);
    GregorianCalendar g2 = new GregorianCalendar(2020, 3, 23);

    RV rv = new RV("Test", g1, g2, null, 10);

    ListModel l1 = new ListModel();
    l1.add(rv);

    l1.saveTextFile("LoadTextTest.txt");

    ListModel l2 = new ListModel();
    l2.add(rv);

    l1 = new ListModel();
    l1.loadTextFile("LoadTextTest.txt");

    for (int i = 0; i < l1.getListCampSites().size(); ++i) {
      assertEquals(l1.getListCampSites().get(i).getActualCheckOut(),
          l2.getListCampSites().get(i).getActualCheckOut());

      assertEquals(l1.getListCampSites().get(i).getGuestType(),
          l2.getListCampSites().get(i).getGuestType());

      assertEquals(l1.getListCampSites().get(i).getEstimatedCheckOut(),
          l2.getListCampSites().get(i).getEstimatedCheckOut());

      assertEquals(l1.getListCampSites().get(i).getGuestName(),
          l2.getListCampSites().get(i).getGuestName());

      assertEquals(l1.getListCampSites().get(i).getCheckIn(),
          l2.getListCampSites().get(i).getCheckIn());
    }
  }

  @Test(expected = Exception.class)
  public void updateScreenUndefinedDisplayError() {
    ListModel list = new ListModel();
    list.setDisplay(ScreenDisplay.Oopsie);
  }

  @Test(expected = Exception.class)
  public void getColumnNameUndefinedStateError() {
    ListModel list = new ListModel();
    try {
      list.setDisplay(ScreenDisplay.Oopsie);
    } catch (RuntimeException e) {
      list.getColumnName(1);
    }
  }

  @Test(expected = Exception.class)
  public void getColumnCountUndefinedDisplayError() {
    ListModel list = new ListModel();
    try {
      list.setDisplay(ScreenDisplay.Oopsie);
    } catch (RuntimeException e) {
      list.getColumnCount();
    }
  }

  @Test(expected = Exception.class)
  public void getRowCountUndefinedDisplayError() {
    ListModel list = new ListModel();
    try {
      list.setDisplay(ScreenDisplay.Oopsie);
    } catch (RuntimeException e) {
      list.getValueAt(0, 0);
    }
  }

  @Test(expected = Exception.class)
  public void createListError() {
    ListModel list = new ListModel();
    list.setEBreak(true);
    list.createList();
  }

  @Test(expected = RuntimeException.class)
  public void CurrentParkScreenError() {
    ListModel list = new ListModel();
    list.setDisplay(ScreenDisplay.CurrentParkStatus);

    list.getValueAt(500, 500);
  }

  @Test(expected = RuntimeException.class)
  public void CheckoutScreenError() {
    ListModel list = new ListModel();
    list.setDisplay(ScreenDisplay.CheckOutGuest);

    list.getValueAt(500, 500);
  }

  @Test(expected = RuntimeException.class)
  public void RvTentScreenError() {
    ListModel list = new ListModel();
    list.setDisplay(ScreenDisplay.RvTent);


    list.getValueAt(500, 500);
  }

  @Test(expected = RuntimeException.class)
  public void TentRvScreenError() {
    ListModel list = new ListModel();
    list.setDisplay(ScreenDisplay.TentRv);

    list.getValueAt(500, 500);
  }

  // Tests saving database file will illegal characters
  @Test(expected = RuntimeException.class)
  public void saveDatabaseCharacterError() {
    ListModel list = new ListModel();
    list.saveDatabase("|/*?");
  }

  // Tests loading a illegal database file
  @Test(expected = RuntimeException.class)
  public void loadDatabaseFileError() throws IOException {
    PrintWriter writer = new PrintWriter(new BufferedWriter(
        new FileWriter("rip_ur_ded_program")));

    writer.println("haha, you fool!");
    writer.close();

    ListModel list = new ListModel();
    list.loadDatabase("rip_ur_ded_program");
  }

  // Tests saving text file with illegal characters
  @Test(expected = RuntimeException.class)
  public void saveTextFileError() {
    ListModel list1 = new ListModel();
    list1.saveTextFile("|/*?.txt");
  }

  //Tests trying to load a illegal text file
  @Test(expected = RuntimeException.class)
  public void loadTextFileError() throws IOException {
    PrintWriter writer = new PrintWriter(new BufferedWriter(
        new FileWriter("haha_ive_fooled_you_now.txt")));

    writer.println("muahahahah");
    writer.close();

    ListModel list = new ListModel();
    list.loadTextFile("haha_ive_fooled_you_now.txt");
  }

  @Test(expected = Exception.class)
  public void loadTextFileFileNotFoundError() {
    ListModel list = new ListModel();
    list.loadTextFile("fakestuff");
  }
}
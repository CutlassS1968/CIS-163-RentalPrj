import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/*****************************************************************
 *
 *  Campers Reservation System
 *
 *****************************************************************/

/**
 * The GUICampReservationSystem class is the class that displays the GUI to the user and allows
 * the user to reserve an RV or tentOnly site, and allows the user to check out too. In addition,
 * the GUI allows the user to save and load the database using serialized and text files.
 */
public class GUICampReservationSystem extends JFrame implements ActionListener{
    // declare GUI components (menu items, buttons, etc.) needed
    // constructor method that prepares the GUI
    // event handlers and other methods need to build the GUIdd


    /** Holds menu bar */
    private JMenuBar menus;

    /** menus in the menu bar */
    private JMenu fileMenu;
    private JMenu actionMenu;

    /** menu items in each of the menus */
    private JMenuItem openSerItem;
    private JMenuItem exitItem;
    private JMenuItem saveSerItem;
    private JMenuItem openTextItem;
    private JMenuItem saveTextItem;
    private JMenuItem reserveRVItem;
    private JMenuItem reserveTentOnlyItem;
    private JMenuItem checkOutItem;

    private JMenuItem currentParkItemScn;
    private JMenuItem checkOUtItemScn;
    private JMenuItem overDueItemScn;
    private JMenuItem sortTentRvItemScn;
    private JMenuItem sortRvTentItemScn;

    private JPanel panel;

    /** Holds the list engine */
    private ListModel DList;

    /** Holds jTable */
    private JTable jTable;

    /** Scroll pane */
    private JScrollPane scrollList;

    /*****************************************************************
     *
     * A constructor that starts a new GUI1024 for the rental store
     *
     *****************************************************************/
    public GUICampReservationSystem(){
        //adding menu bar and menu items
        menus = new JMenuBar();
        fileMenu = new JMenu("File");
        actionMenu = new JMenu("Action");
        openSerItem = new JMenuItem("Open File");
        exitItem = new JMenuItem("Exit");
        saveSerItem = new JMenuItem("Save File");
        openTextItem = new JMenuItem("Open Text");
        saveTextItem = new JMenuItem("Save Text");
        reserveRVItem = new JMenuItem("Reserve a RV Site");
        reserveTentOnlyItem = new JMenuItem("Reserve a TentOnly site");
        checkOutItem = new JMenuItem("CheckOut of TentOnly or RV");

        currentParkItemScn = new JMenuItem("Current Park Screen");
        checkOUtItemScn = new JMenuItem("Check out screen");
        overDueItemScn = new JMenuItem("OverDue Screen");
        sortRvTentItemScn = new JMenuItem("Sort RV, tent Screen");
        sortTentRvItemScn = new JMenuItem("Sort tent, RV Screen");

        //adding items to bar
        fileMenu.add(openSerItem);
        fileMenu.add(saveSerItem);
        fileMenu.addSeparator();
        fileMenu.add(openTextItem);
        fileMenu.add(saveTextItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        fileMenu.addSeparator();
        fileMenu.add(currentParkItemScn);
        fileMenu.add(checkOUtItemScn);
        fileMenu.add(overDueItemScn);
        fileMenu.add (sortRvTentItemScn);
        fileMenu.add(sortTentRvItemScn);

        actionMenu.add(reserveRVItem);
        actionMenu.add(reserveTentOnlyItem);
        actionMenu.addSeparator();
        actionMenu.add(checkOutItem);

        menus.add(fileMenu);
        menus.add(actionMenu);

        //adding actionListener
        openSerItem.addActionListener(this);
        saveSerItem.addActionListener(this);
        openTextItem.addActionListener(this);
        saveTextItem.addActionListener(this);
        exitItem.addActionListener(this);
        reserveRVItem.addActionListener(this);
        reserveTentOnlyItem.addActionListener(this);
        checkOutItem.addActionListener(this);

        currentParkItemScn.addActionListener(this);
        checkOUtItemScn.addActionListener(this);
        overDueItemScn.addActionListener(this);
        sortRvTentItemScn.addActionListener(this);
        sortTentRvItemScn.addActionListener(this);

        setJMenuBar(menus);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        DList = new ListModel();
        jTable = new JTable(DList);
        scrollList = new JScrollPane(jTable);
        panel.add(scrollList);
        add(panel);
        scrollList.setPreferredSize(new Dimension(800,300));

        setVisible(true);
        setSize(950,450);
    }

    public void actionPerformed(ActionEvent e) {
        Object comp = e.getSource();

        if (currentParkItemScn == comp)
            DList.setDisplay(ScreenDisplay.CurrentParkStatus);

        if (checkOUtItemScn == comp)
            DList.setDisplay(ScreenDisplay.CheckOutGuest);

        if (openSerItem == comp || openTextItem == comp) {
            JFileChooser chooser = new JFileChooser();
            int status = chooser.showOpenDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                if (openSerItem == comp)
                    DList.loadDatabase(filename);
            }
        }

        if (saveSerItem == comp || saveTextItem == comp) {
            JFileChooser chooser = new JFileChooser();
            int status = chooser.showSaveDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                if (saveSerItem == e.getSource())
                    DList.saveDatabase(filename);
            }
        }

        //MenuBar options
        if(e.getSource() == exitItem){
            System.exit(1);
        }
        if(e.getSource() == reserveRVItem){
            RV RV = new RV();
            ReservationRVDialog dialog = new ReservationRVDialog(this, RV);
            if(dialog.getCloseStatus() == ReservationRVDialog.OK){
                DList.add(RV);
            }
        }
        if(e.getSource() == reserveTentOnlyItem){
            TentOnly tentOnly = new TentOnly();
            ReservationTentOnlyDialog dialog = new ReservationTentOnlyDialog(this, tentOnly);
            if(dialog.getCloseStatus() == ReservationTentOnlyDialog.OK){
                DList.add(tentOnly);
            }
        }

        if (checkOutItem == e.getSource()) {
            int index = jTable.getSelectedRow();
            if (index != -1) {
                GregorianCalendar dat = new GregorianCalendar();

                CampSite unit = DList.get(index);
                CheckOutOnDialog dialog = new CheckOutOnDialog(this, unit);

                JOptionPane.showMessageDialog(null,
                        "  Be sure to thank " + unit.getGuestName() +
                        "\n for camping with us and the price is:  " +
                        unit.getCost(unit.getActualCheckOut()) +
                        " dollars");
                DList.upDate(index, unit);
            }
        }
    }

    public static void main(String[] args) {
        new GUICampReservationSystem();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import java.util.List;
import javax.swing.JPanel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hin
 */
public class LondonMusicalsTest {
    
    public LondonMusicalsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of connect method, of class LondonMusicals.
     */
    @Test
    public void testConnect() {
        System.out.println("connect");
        LondonMusicals instance = new LondonMusicals();
        instance.connect();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of music_table method, of class LondonMusicals.
     */
    @Test
    public void testMusic_table() {
        System.out.println("music_table");
        LondonMusicals instance = new LondonMusicals();
        instance.music_table();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of music_table2 method, of class LondonMusicals.
     */
    @Test
    public void testMusic_table2() {
        System.out.println("music_table2");
        String action = "";
        LondonMusicals instance = new LondonMusicals();
        instance.music_table2(action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of search_music_table2 method, of class LondonMusicals.
     */
    @Test
    public void testSearch_music_table2() {
        System.out.println("search_music_table2");
        JPanel cardPanel = null;
        String table_name = "";
        LondonMusicals instance = new LondonMusicals();
        instance.search_music_table2(cardPanel, table_name);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addTickets method, of class LondonMusicals.
     */
    @Test
    public void testAddTickets() {
        System.out.println("addTickets");
        Music musicalItem = null;
        Schedule scheduleItem = null;
        int spinnerValue = 0;
        LondonMusicals instance = new LondonMusicals();
        instance.addTickets(musicalItem, scheduleItem, spinnerValue);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of calculateTicketPrice method, of class LondonMusicals.
     */
    @Test
    public void testCalculateTicketPrice() {
        System.out.println("calculateTicketPrice");
        String ticketType = "";
        Schedule scheduleItem = null;
        LondonMusicals instance = new LondonMusicals();
        double expResult = 0.0;
        double result = instance.calculateTicketPrice(ticketType, scheduleItem);
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTicketCount method, of class LondonMusicals.
     */
    @Test
    public void testGetTicketCount() {
        System.out.println("getTicketCount");
        int musicId = 0;
        int scheduleId = 0;
        LondonMusicals instance = new LondonMusicals();
        int expResult = 0;
        int result = instance.getTicketCount(musicId, scheduleId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of schedule_table method, of class LondonMusicals.
     */
    @Test
    public void testSchedule_table() {
        System.out.println("schedule_table");
        LondonMusicals instance = new LondonMusicals();
        instance.schedule_table();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertSchedule method, of class LondonMusicals.
     */
    @Test
    public void testInsertSchedule() {
        System.out.println("insertSchedule");
        List<Ticket> ticketList = null;
        LondonMusicals instance = new LondonMusicals();
        instance.insertSchedule(ticketList);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSpinnerValue method, of class LondonMusicals.
     */
    @Test
    public void testSetSpinnerValue() {
        System.out.println("setSpinnerValue");
        int seatsLeft = 0;
        LondonMusicals instance = new LondonMusicals();
        int expResult = 0;
        int result = instance.setSpinnerValue(seatsLeft);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class LondonMusicals.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        LondonMusicals.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

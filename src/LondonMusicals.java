/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.time.Instant;
import java.util.UUID;

public class LondonMusicals extends javax.swing.JFrame {

    /**
     * Creates new form LondonMusicals
     */
    public LondonMusicals() {

        initComponents();
        connect();
        music_table();
        ticketList = new ArrayList<>();
        populateSearchColumnComboBox("musics");
        // Get the table model
        DefaultTableModel tickets = (DefaultTableModel) ticketTable.getModel();

        // Clear existing rows from the table
        tickets.setRowCount(0);

    }

    Color customColor_bg = new Color(61, 61, 61);
    Color customColor_text = new Color(254, 214, 23);

    Connection con;
    PreparedStatement query;
    Music musicItem;
    Schedule scheduleItem;
    Ticket ticketItem;
    List<Ticket> ticketList;
    DefaultTableModel tickets;
    int totalTickets;
    double totalPrice;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/musicals", "root", "");
            System.out.println("Connected");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LondonMusicals.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LondonMusicals.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void music_table() {
        try {
            if (con == null || con.isClosed()) {
                connect();
            }

            PreparedStatement query = con.prepareStatement("SELECT * FROM musics");
            ResultSet table_results = query.executeQuery();

            // Get the table model
            DefaultTableModel musicTable = (DefaultTableModel) jTable1.getModel();

            // Clear existing rows from the table
            musicTable.setRowCount(0);

            // Populate the table with data from the ResultSet
            while (table_results.next()) {
                Object[] row = {
                    table_results.getInt("id"),
                    table_results.getString("title"),
                    table_results.getString("runtime"),
                    table_results.getString("category"),
                    table_results.getString("venue"),
                    table_results.getInt("age")
                // Add more columns as needed
                };
                musicTable.addRow(row);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createMusicCard(int id, String title, String runtime, String category, String venue, int age) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(customColor_bg);

        // Add margin to the card
        cardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel detailsPanel = new JPanel(new GridLayout(5, 1));
        detailsPanel.setBackground(Color.BLACK);

        Font boldFont = new Font("Helvetica Neue", Font.BOLD, 14);

        JLabel titleLabel = createStyledLabel("Title: " + title);
        JLabel runtimeLabel = createStyledLabel("Runtime: " + runtime);
        JLabel categoryLabel = createStyledLabel("Category: " + category);
        JLabel venueLabel = createStyledLabel("Venue: " + venue);
        JLabel ageLabel = createStyledLabel("Age: " + age);

// Apply the styled font to the JLabels
        titleLabel.setFont(boldFont);
        // Set foreground color
        titleLabel.setForeground(customColor_text);
        runtimeLabel.setForeground(customColor_text);
        categoryLabel.setForeground(customColor_text);
        venueLabel.setForeground(customColor_text);
        ageLabel.setForeground(customColor_text);

        detailsPanel.add(titleLabel);
        detailsPanel.add(runtimeLabel);
        detailsPanel.add(categoryLabel);
        detailsPanel.add(venueLabel);
        detailsPanel.add(ageLabel);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(detailsPanel, BorderLayout.CENTER);

// Load a random image icon from the "images" folder and scale it
        String[] imageFiles = {"image1.jpg", "image2.jpg", "image3.jpg", "image4.jpg", "image5.jpg"};
        String randomImageFile = imageFiles[new Random().nextInt(imageFiles.length)];
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/" + randomImageFile));
        Image scaledImage = originalIcon.getImage().getScaledInstance(400, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);
        contentPanel.add(imageLabel, BorderLayout.BEFORE_FIRST_LINE);
        JButton detailsButton = new JButton("Details");
        detailsButton.setForeground(customColor_bg);
        detailsButton.setBackground(customColor_text);

        detailsButton.addActionListener(e -> showDetailsDialog(id, title, runtime, category, venue, age));

        cardPanel.add(contentPanel, BorderLayout.CENTER);
        cardPanel.add(detailsButton, BorderLayout.SOUTH);

        // Apply shadow effect
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                // Add padding to the card
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                // Add shadow effect
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK),
                        BorderFactory.createMatteBorder(10, 10, 10, 10, Color.BLACK)
                )
        ));
        cardPanel.setPreferredSize(new Dimension(50, 400)); // Adjust the width as needed

        return cardPanel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Helvetica Neue", Font.PLAIN, 12)); // Adjust font size if needed
        return label;
    }

    private void showDetailsDialog(int id, String title, String runtime, String category, String venue, int age) {
        // Display details dialog or perform other actions
        JOptionPane.showMessageDialog(this,
                "ID: " + id + "\nTitle: " + title + "\nRuntime: " + runtime
                + "\nCategory: " + category + "\nVenue: " + venue + "\nAge: " + age,
                "Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void music_table2(String action) {
        try {
            if (con == null || con.isClosed()) {
                connect();
            }

            String selectedColumn = searchColumnComboBox.getSelectedItem().toString();
            String searchData = searchDataField.getText();
            System.out.println(selectedColumn + " " + searchData + " " + action);

            // Create the SQL query with a WHERE clause
            String search_query = "SELECT * FROM " + "musics" + "";
            if (!searchData.isEmpty()) {
                search_query += " WHERE " + selectedColumn + " LIKE '%" + searchData + "%'";
            }

            PreparedStatement queryStmt = con.prepareStatement(search_query);
            ResultSet search_table_results = queryStmt.executeQuery();

//            initSearchComponents("musics");
            PreparedStatement query = con.prepareStatement("SELECT * FROM musics");
            ResultSet table_results = query.executeQuery();

            // Get the panel where the cards will be added
            JPanel cardPanel = new JPanel(new GridLayout(0, 3)); // 0 rows, 3 columns
            cardPanel.setBackground(customColor_bg);

            ResultSet musicList = null;
            if (action == "search") {
                musicList = search_table_results;
            } else if (action == "get") {
                musicList = table_results;
            }

            // Populate the panel with cards for each music entry
            while (musicList.next()) {
                int id = musicList.getInt("id");
                String title = musicList.getString("title");
                String runtime = musicList.getString("runtime");
                String category = musicList.getString("category");
                String venue = musicList.getString("venue");
                int age = musicList.getInt("age");

                JPanel musicCard = createMusicCard(id, title, runtime, category, venue, age);
                jPanel3.add(musicCard);
                cardPanel.add(musicCard);
            }

            // Add the cardPanel to a JScrollPane for scrolling
            JScrollPane scrollPane = new JScrollPane(cardPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            // Display the cards in the jPanel3
            jPanel3.removeAll();
            jPanel3.setLayout(new BorderLayout());
            jPanel3.add(scrollPane, BorderLayout.CENTER);
            jPanel3.revalidate();
            jPanel3.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modify your music_table2 method
    public void search_music_table2(JPanel cardPanel, String table_name) {

        try {
            if (con == null || con.isClosed()) {
                connect();
            }

            String selectedColumn = searchColumnComboBox.getSelectedItem().toString();
            String searchData = searchDataField.getText();
            System.out.println(selectedColumn + " " + searchData);

            // Create the SQL query with a WHERE clause
            String query = "SELECT * FROM " + table_name + "";
            if (!searchData.isEmpty()) {
                query += " WHERE " + selectedColumn + " LIKE '%" + searchData + "%'";
            }

            PreparedStatement queryStmt = con.prepareStatement(query);
            ResultSet table_results = queryStmt.executeQuery();

            // Rest of the method remains the same...
            // Populate the panel with cards for each music entry
            while (table_results.next()) {
                int id = table_results.getInt("id");
                String title = table_results.getString("title");
                String runtime = table_results.getString("runtime");
                String category = table_results.getString("category");
                String venue = table_results.getString("venue");
                int age = table_results.getInt("age");

                JPanel musicCard = createMusicCard(id, title, runtime, category, venue, age);
                jPanel3.add(musicCard);
                cardPanel.add(musicCard);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateSearchColumnComboBox(String table_name) {
        try {
            if (con == null || con.isClosed()) {
                connect();
            }

            DatabaseMetaData metaData = con.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, table_name, null);

            // Extract column names and add them to the searchColumnComboBox
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                searchColumnComboBox.addItem(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateSchedulesComboBox(int musicId) {
        try {
            if (con == null || con.isClosed()) {
                connect();
            }

            // Fetch schedules data from the database based on the musicId
            PreparedStatement query = con.prepareStatement("SELECT * FROM schedules WHERE music_id = ?");
            query.setInt(1, musicId);

            ResultSet scheduleResults = query.executeQuery();

            // Clear the existing items in the schedules ComboBox
            // Assuming schedulesCombo is the name of your schedules ComboBox
            schedulesCombo.removeAllItems();

            while (scheduleResults.next()) {
                int id = scheduleResults.getInt("schedule_id");
                Date date = scheduleResults.getDate("show_date");
                String timeSlot = scheduleResults.getString("show_time");
                int availableSeats = scheduleResults.getInt("available_seats");
                double price = scheduleResults.getDouble("price");

                // Create a new Schedule object using the provided constructor
                LocalDateTime localDateTime = convertDateToLocalDateTime(date);
                scheduleItem = new Schedule(id, musicId, localDateTime, timeSlot, availableSeats, price);

                // Add the Schedule object to the schedules ComboBox
                schedulesCombo.addItem(scheduleItem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private LocalDateTime convertDateToLocalDateTime(Date date) {
        // Convert java.sql.Date to java.util.Date
        java.util.Date utilDate = new java.util.Date(date.getTime());

        // Convert java.util.Date to Instant
        Instant instant = utilDate.toInstant();

        // Convert Instant to LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        return localDateTime;
    }

    private void populateMusicComboBox(String table_name) {
        try {
            if (con == null || con.isClosed()) {
                connect();
            }

            // Fetch column names from the database table (replace "yourTableName" with your actual table name)
            DatabaseMetaData metaData = con.getMetaData();
            PreparedStatement query = con.prepareStatement("SELECT * FROM musics");

            ResultSet table_results = query.executeQuery();

            while (table_results.next()) {
                int id = table_results.getInt("id");
                String title = table_results.getString("title");
                String runtime = table_results.getString("runtime");
                String category = table_results.getString("category");
                String venue = table_results.getString("venue");
                int age = table_results.getInt("age");

                musicItem = new Music(id, title, runtime, category, venue, age);

                String musicItemText = id + "-" + title + "-" + runtime;
                pickMusicalsCombo.addItem(musicItem);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTickets(Music musicalItem, Schedule scheduleItem, int spinnerValue) {

        String ticketType = (String) pickTicketType.getSelectedItem();
        calculateTicketPrice(ticketType, scheduleItem);
        ticketItem = new Ticket(musicalItem, scheduleItem, ticketType, LocalDateTime.now(), musicalItem.getId(), scheduleItem.getId());

        ticketList.add(ticketItem);

        tickets = (DefaultTableModel) ticketTable.getModel();

        // Add row to the table model
        for (int i = 1; i <= spinnerValue; i++) {
            tickets.addRow(new Object[]{
                ticketItem.toString()
            });
        }

    }

    public double calculateTicketPrice(String ticketType, Schedule scheduleItem) {
        double price = scheduleItem.getPrice();
        if (ticketType == "Adult") {
            price = scheduleItem.getPrice() + 0;

        } else if (ticketType == "Senior") {
            price = scheduleItem.getPrice() - 5;
            scheduleItem.setPrice(price);
        } else if (ticketType == "Student") {
            price = scheduleItem.getPrice() - 10;
            scheduleItem.setPrice(price);
        }
        return price;
    }

    public int getTicketCount(int musicId, int scheduleId) {
        int ticketCount = 0;

        try {
            // Write your SQL query with placeholders for parameters
            String query = "SELECT COUNT(*) AS ticket_count FROM tickets WHERE music_id = ? AND schedule_id = ?";

            // Create a PreparedStatement with the query
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                // Set values for placeholders
                preparedStatement.setInt(1, musicId);
                preparedStatement.setInt(2, scheduleId);

                // Execute the query and retrieve the result set
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Check if there is a result
                    if (resultSet.next()) {
                        // Retrieve the ticket count from the result set
                        ticketCount = resultSet.getInt("ticket_count");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately
        }

        return ticketCount;
    }

    public void schedule_table() {
        try {
            if (con == null || con.isClosed()) {
                connect();
            }

            PreparedStatement query = con.prepareStatement("SELECT * FROM schedules");
            ResultSet table_results = query.executeQuery();

            // Get the table model
            DefaultTableModel scheduleTable = (DefaultTableModel) jTable2.getModel();

            // Clear existing rows from the table
            scheduleTable.setRowCount(0);

            // Populate the table with data from the ResultSet
            while (table_results.next()) {
                Object[] row = {
                    table_results.getInt("schedule_id"),
                    table_results.getString("musical_title"),
                    table_results.getString("show_date"),
                    table_results.getString("show_time"),
                    table_results.getInt("available_seats"),
                    table_results.getDouble("price")
                // Add more columns as needed
                };
                scheduleTable.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void exportToPDF() {
        try {
            Document document = new Document(PageSize.A4);
            String fileName = UUID.randomUUID().toString().substring(0, 8);
            PdfWriter.getInstance(document, new FileOutputStream(fileName + "table.pdf"));

            document.open();

            PdfPTable pdfTable = new PdfPTable(ticketTable.getColumnCount());
            addTableHeader(pdfTable);
            addRows(pdfTable);

            document.add(pdfTable);
            document.close();

            JOptionPane.showMessageDialog(this, "Table exported to PDF successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error exporting table to PDF", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTableHeader(PdfPTable pdfTable) {
        for (int column = 0; column < ticketTable.getColumnCount(); column++) {
            PdfPCell cell = new PdfPCell(new Paragraph(ticketTable.getColumnName(column)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            pdfTable.addCell(cell);
        }
    }

    private void addRows(PdfPTable pdfTable) {
        for (int row = 0; row < ticketTable.getRowCount(); row++) {
            for (int column = 0; column < ticketTable.getColumnCount(); column++) {
                PdfPCell cell = new PdfPCell(new Paragraph(ticketTable.getValueAt(row, column).toString()));
                pdfTable.addCell(cell);
            }
        }
    }

    public void insertSchedule(List<Ticket> ticketList) {
        try {
            if (con == null || con.isClosed()) {
                connect();
            }

            // Create a prepared statement for insertion
            String insertQuery = "INSERT INTO tickets (ticketNumber, seatNumber, schedule_id, music_id, ticket_type) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = con.prepareStatement(insertQuery);

            for (Ticket ticket : ticketList) {
                insertStatement.setString(1, ticket.getTicketNumber());
                insertStatement.setString(2, ticket.getSeatNumber());
                insertStatement.setInt(3, ticket.getSchedule_id());
                insertStatement.setInt(4, ticket.getMusic_id());
                insertStatement.setString(5, ticket.getTicketType());
                insertStatement.addBatch();
            }
            int[] result = insertStatement.executeBatch();
            System.out.println("Rows affected: " + result.length);
            JOptionPane.showMessageDialog(this, "Data Inserted. Rows affected: " + result.length);
            // Set values for the parameters
            // Close the statement
            insertStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        panel_musicList = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panel_scheduleList = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        panel_bookig = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        searchColumnComboBox = new javax.swing.JComboBox<>();
        searchDataField = new javax.swing.JTextField();
        btn_search = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        pickMusicalsCombo = new javax.swing.JComboBox<>();
        schedulesCombo = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        pickTicketType = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        ticketSpinner = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        btn_add_tickets = new javax.swing.JButton();
        seatCount = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        ticketPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        ticketTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        clearAll = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        coverImage = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        btn_musicList = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        jPanel11.setBackground(new java.awt.Color(255, 255, 153));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 153));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(63, 63, 63));

        jLabel1.setBackground(new java.awt.Color(237, 106, 182));
        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(174, 43, 114));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-music-48.png"))); // NOI18N
        jLabel1.setText("London Musicals");

        jTabbedPane2.setBackground(new java.awt.Color(61, 61, 61));
        jTabbedPane2.setForeground(new java.awt.Color(254, 214, 14));
        jTabbedPane2.setBounds(new java.awt.Rectangle(61, 61, 61, 0));
        jTabbedPane2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane2MouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Title", "Runtime", "Category", "Venue", "Age"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout panel_musicListLayout = new javax.swing.GroupLayout(panel_musicList);
        panel_musicList.setLayout(panel_musicListLayout);
        panel_musicListLayout.setHorizontalGroup(
            panel_musicListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_musicListLayout.createSequentialGroup()
                .addGroup(panel_musicListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_musicListLayout.createSequentialGroup()
                        .addGap(804, 804, 804)
                        .addComponent(jLabel3)
                        .addGap(0, 611, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        panel_musicListLayout.setVerticalGroup(
            panel_musicListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_musicListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Music List", panel_musicList);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Musical Title", "Date", "Time", "Seats"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout panel_scheduleListLayout = new javax.swing.GroupLayout(panel_scheduleList);
        panel_scheduleList.setLayout(panel_scheduleListLayout);
        panel_scheduleListLayout.setHorizontalGroup(
            panel_scheduleListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1421, Short.MAX_VALUE)
        );
        panel_scheduleListLayout.setVerticalGroup(
            panel_scheduleListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_scheduleListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jTabbedPane2.addTab("Show Schedules", panel_scheduleList);

        jPanel3.setForeground(new java.awt.Color(61, 61, 61));
        jPanel3.setMaximumSize(new java.awt.Dimension(800, 800));
        jPanel3.setPreferredSize(new java.awt.Dimension(400, 400));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1415, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        searchColumnComboBox.setSelectedItem(jPanel3);
        searchColumnComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchColumnComboBoxActionPerformed(evt);
            }
        });

        searchDataField.setText("Seach...");

        btn_search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-filter-48.png"))); // NOI18N
        btn_search.setText("Search");
        btn_search.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                search(evt);
            }
        });
        btn_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_bookigLayout = new javax.swing.GroupLayout(panel_bookig);
        panel_bookig.setLayout(panel_bookigLayout);
        panel_bookigLayout.setHorizontalGroup(
            panel_bookigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_bookigLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1415, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panel_bookigLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(searchColumnComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(searchDataField, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addComponent(btn_search)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_bookigLayout.setVerticalGroup(
            panel_bookigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_bookigLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panel_bookigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panel_bookigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(searchColumnComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                        .addComponent(searchDataField, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_search))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jTabbedPane2.addTab("All Musicals", panel_bookig);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(204, 0, 102)));

        jLabel4.setText("Select Tickets");

        jLabel5.setText("Musicals");

        pickMusicalsCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pickMusicalsComboActionPerformed(evt);
            }
        });

        jLabel6.setText("Schedule");

        pickTicketType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Adult", "Senior", "Student" }));

        jLabel7.setText("Type");

        ticketSpinner.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                validateInput(evt);
            }
        });

        jLabel8.setText("Tickets");

        btn_add_tickets.setBackground(new java.awt.Color(0, 204, 51));
        btn_add_tickets.setForeground(new java.awt.Color(255, 255, 255));
        btn_add_tickets.setText("Add");
        btn_add_tickets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_ticketsActionPerformed(evt);
            }
        });

        seatCount.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        seatCount.setText("Seats Left : ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pickTicketType, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(ticketSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(54, 54, 54)
                                .addComponent(seatCount)
                                .addGap(130, 130, 130)
                                .addComponent(btn_add_tickets)))
                        .addContainerGap(578, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(schedulesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 671, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pickMusicalsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 671, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(356, Short.MAX_VALUE))))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(191, 191, 191)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(pickMusicalsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(schedulesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(pickTicketType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ticketSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(btn_add_tickets)
                    .addComponent(seatCount))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(255, 0, 255)));

        ticketTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ticket#"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(ticketTable);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ticketPanelLayout = new javax.swing.GroupLayout(ticketPanel);
        ticketPanel.setLayout(ticketPanelLayout);
        ticketPanelLayout.setHorizontalGroup(
            ticketPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ticketPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1351, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(322, 322, 322)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        ticketPanelLayout.setVerticalGroup(
            ticketPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ticketPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ticketPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ticketPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ticketPanelLayout.createSequentialGroup()
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(20, 20, 20)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ticketPanelLayout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        jScrollPane4.setViewportView(ticketPanel);

        clearAll.setBackground(new java.awt.Color(254, 214, 18));
        clearAll.setForeground(new java.awt.Color(255, 255, 255));
        clearAll.setText("Clear All");
        clearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearAllActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(102, 102, 255));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Save & Download");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1353, Short.MAX_VALUE)
                .addGap(40, 40, 40))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(429, 429, 429)
                .addComponent(clearAll)
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearAll)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("List", jPanel2);

        jTabbedPane2.addTab("Buy Ticket", jTabbedPane1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addGap(62, 62, 62))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        coverImage.setBackground(new java.awt.Color(121, 109, 109));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/image2.jpg"))); // NOI18N
        jButton1.setText("jButton1");
        jButton1.setMaximumSize(new java.awt.Dimension(400, 400));
        jButton1.setMinimumSize(new java.awt.Dimension(400, 400));
        jButton1.setPreferredSize(new java.awt.Dimension(400, 400));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout coverImageLayout = new javax.swing.GroupLayout(coverImage);
        coverImage.setLayout(coverImageLayout);
        coverImageLayout.setHorizontalGroup(
            coverImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        coverImageLayout.setVerticalGroup(
            coverImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(coverImageLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_musicList.setBackground(new java.awt.Color(61, 61, 61));
        btn_musicList.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btn_musicList.setForeground(new java.awt.Color(254, 214, 14));
        btn_musicList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-list-48.png"))); // NOI18N
        btn_musicList.setText("Music List");
        btn_musicList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_musicListActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(61, 61, 61));
        jButton2.setForeground(new java.awt.Color(254, 214, 14));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-schedule-48.png"))); // NOI18N
        jButton2.setText("Show Schedule");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(61, 61, 61));
        jButton3.setForeground(new java.awt.Color(254, 214, 14));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-add-bookmark-48.png"))); // NOI18N
        jButton3.setText("Book Ticket");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-close-48.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(coverImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_musicList)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addGap(12, 12, 12)
                .addComponent(jButton5)
                .addGap(113, 113, 113))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(coverImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jButton5)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(btn_musicList)
                        .addComponent(jButton3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 559, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_musicListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_musicListActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedIndex(0);
        music_table();
    }//GEN-LAST:event_btn_musicListActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedIndex(1);
        schedule_table();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedIndex(2);
        music_table2("get");

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTabbedPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane2MouseClicked
        // TODO add your handling code here:
//        searchColumnComboBox.setName("Select Musical");
        populateMusicComboBox("musics");
        schedule_table();
    }//GEN-LAST:event_jTabbedPane2MouseClicked

    private void searchColumnComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchColumnComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchColumnComboBoxActionPerformed

    private void search(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_search
        // TODO add your handling code here:

        music_table2("search");
    }//GEN-LAST:event_search

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btn_searchActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btn_add_ticketsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_ticketsActionPerformed
        // TODO add your handling code here:
        Music selectedMusic = (Music) pickMusicalsCombo.getSelectedItem();
        Schedule selectedSchedule = (Schedule) schedulesCombo.getSelectedItem();
        int bookedSeats = getTicketCount(selectedMusic.getId(), selectedSchedule.getId());
        int totalSeats = selectedSchedule.getAvailableSeats();
        int seatsLeft = totalSeats - bookedSeats;

        seatCount.setText("Seats Left: " + seatsLeft + "");

        addTickets(selectedMusic, selectedSchedule, setSpinnerValue(seatsLeft));

    }//GEN-LAST:event_btn_add_ticketsActionPerformed

    public int setSpinnerValue(int seatsLeft) {
        SpinnerNumberModel model = (SpinnerNumberModel) ticketSpinner.getModel();
        model.setMinimum(0);
        model.setMaximum(seatsLeft);
        validateSpinnerValue(seatsLeft);
        int spinnerValue = (int) model.getNumber(); // spinner is for integers
        
        return spinnerValue;
    }
    private void pickMusicalsComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pickMusicalsComboActionPerformed
        // TODO add your handling code here:
        // Get the selected Music from the pickMusicalsCombo
        Music selectedMusic = (Music) pickMusicalsCombo.getSelectedItem();

        if (selectedMusic != null) {
            int musicId = selectedMusic.getId();
            System.out.println("Selected Music ID: " + musicId);

            // Call method to populate schedules ComboBox based on the selected musicId
            populateSchedulesComboBox(musicId);
        }
    }//GEN-LAST:event_pickMusicalsComboActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        insertSchedule(ticketList);
        exportToPDF();
        DefaultTableModel model = (DefaultTableModel) ticketTable.getModel();
        model.setRowCount(0);
        ticketList.clear();

    }//GEN-LAST:event_jButton4ActionPerformed

    private void clearAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearAllActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) ticketTable.getModel();
        model.setRowCount(0);
        ticketList.clear();
    }//GEN-LAST:event_clearAllActionPerformed

    private void validateInput(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_validateInput
        // TODO add your handling code here:

    }//GEN-LAST:event_validateInput

    private void validateSpinnerValue(int seatsLeft) {
        try {
            int enteredValue = (int) ticketSpinner.getValue();

            // Perform your validation checks here
            if (enteredValue <= 0 || enteredValue > seatsLeft) {
                // Display an error message or take appropriate action
                JOptionPane.showMessageDialog(null, "Invalid value. Please enter a value between 1 and " + seatsLeft);
                // Reset the spinner to a valid value
                ticketSpinner.setValue(0);
            }
        } catch (NumberFormatException ex) {
            // Handle non-integer input (shouldn't happen with a SpinnerNumberModel)
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
            // Reset the spinner to a valid value
            ticketSpinner.setValue(0);
        }
    }

    public void testSetSpinnerValue() {
        // Test case 1: Seats selected greater than seats left
        ticketSpinner.setModel(new SpinnerNumberModel(11, 0, 11, 1));
        int result1 = setSpinnerValue(5);
        if (result1 != 0) {
            throw new AssertionError("Test case 1 failed: Expected 0 as reset, got " + result1);
        }

        // Test case 2: Seats not slected 0
        ticketSpinner.setModel(new SpinnerNumberModel(0, 0, 8, 1));
        int result2 = setSpinnerValue(8);
        if (result2 != 0) {
            throw new AssertionError("Test case 2 failed: Expected 0 as reset, got " + result2);
        }

        // Test case 3: Seats selected less than or equal 0
        ticketSpinner.setModel(new SpinnerNumberModel(-1, -1, 8, 1));
        int result3 = setSpinnerValue(8);
        if (result3 != 0) {
            throw new AssertionError("Test case 3 failed: Expected 0 as reset, got " + result3);
        }

        
    }

    public void testCalculateTicketPrice() {
        Schedule scheduleItemTest = new Schedule(1, 1, LocalDateTime.now(), "18:00", 100, 50.0);

        LondonMusicals instance = new LondonMusicals();

        //  check if it calculates the price correctly
        double adultPrice = instance.calculateTicketPrice("Adult", scheduleItemTest);
        if (adultPrice != 50) {
            throw new AssertionError("testCalculateTicketPrice Test case 3 failed: Expected 50, got " + adultPrice);
        }

        scheduleItemTest = new Schedule(1, 1, LocalDateTime.now(), "18:00", 100, 50.0);
        double seniorPrice = instance.calculateTicketPrice("Senior", scheduleItemTest);
        if (seniorPrice != 45) {
            throw new AssertionError("testCalculateTicketPrice Test case 3 failed: Expected 45, got " + seniorPrice);
        }
        scheduleItemTest = new Schedule(1, 1, LocalDateTime.now(), "18:00", 100, 50.0);
        double studentPrice = instance.calculateTicketPrice("Student", scheduleItemTest);
        if (studentPrice != 40) {
            throw new AssertionError("testCalculateTicketPrice Test case 3 failed: Expected 40, got " + studentPrice);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LondonMusicals.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LondonMusicals.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LondonMusicals.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LondonMusicals.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        LondonMusicals londonMusicals = new LondonMusicals();

        // Run different test methods
        try {
            londonMusicals.testCalculateTicketPrice();
            londonMusicals.testSetSpinnerValue();       
            System.out.println("All test cases passed.");
        } catch (AssertionError e) {
            System.out.println("Test case failed: " + e.getMessage());
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new LondonMusicals().setVisible(true);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add_tickets;
    private javax.swing.JButton btn_musicList;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton clearAll;
    private javax.swing.JPanel coverImage;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JPanel panel_bookig;
    private javax.swing.JPanel panel_musicList;
    private javax.swing.JPanel panel_scheduleList;
    private javax.swing.JComboBox<Music> pickMusicalsCombo;
    private javax.swing.JComboBox<String> pickTicketType;
    private javax.swing.JComboBox<Schedule> schedulesCombo;
    private javax.swing.JComboBox<String> searchColumnComboBox;
    private javax.swing.JTextField searchDataField;
    private javax.swing.JLabel seatCount;
    private javax.swing.JPanel ticketPanel;
    private javax.swing.JSpinner ticketSpinner;
    private javax.swing.JTable ticketTable;
    // End of variables declaration//GEN-END:variables

}

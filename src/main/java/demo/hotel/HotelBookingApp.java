package demo.hotel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class HotelBookingApp {
    private static final Color INK = new Color(32, 48, 49);
    private static final Color MUTED = new Color(101, 116, 113);
    private static final Color GREEN = new Color(39, 105, 83);
    private static final Color PAPER = new Color(248, 247, 241);

    private final JComboBox<Room> roomPicker = new JComboBox<>(new Room[] {
        new Room("Garden King", "King bed - Garden view", 189, 2),
        new Room("Harbor Suite", "Queen bed - Balcony", 265, 3),
        new Room("Family Loft", "Two beds - City view", 320, 4)
    });
    private final JTextField checkInField = new JTextField(LocalDate.now().plusDays(7).toString(), 12);
    private final JTextField checkOutField = new JTextField(LocalDate.now().plusDays(9).toString(), 12);
    private final JSpinner guestSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 8, 1));
    private final JLabel estimateLabel = new JLabel(" ");
    private final JLabel statusLabel = new JLabel("Choose your stay details to see an estimate.");

    private HotelBookingApp() {
        JFrame frame = new JFrame("Juniper House | Stay booking");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(620, 560));
        frame.setContentPane(buildContent());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel buildContent() {
        JPanel page = new JPanel(new BorderLayout(0, 24));
        page.setBackground(PAPER);
        page.setBorder(BorderFactory.createEmptyBorder(34, 42, 30, 42));

        JPanel heading = new JPanel(new BorderLayout(0, 8));
        heading.setOpaque(false);
        JLabel brand = new JLabel("JUNIPER HOUSE  /  PORTLAND, MAINE");
        brand.setFont(new Font("SansSerif", Font.BOLD, 11));
        brand.setForeground(GREEN);
        JLabel title = new JLabel("Make room for a getaway.");
        title.setFont(new Font("Serif", Font.PLAIN, 31));
        title.setForeground(INK);
        JLabel subtitle = new JLabel("A quiet corner by the coast, ready when you are.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);
        JPanel titleGroup = new JPanel(new BorderLayout(0, 8));
        titleGroup.setOpaque(false);
        titleGroup.add(title, BorderLayout.NORTH);
        titleGroup.add(subtitle, BorderLayout.CENTER);
        heading.add(brand, BorderLayout.NORTH);
        heading.add(titleGroup, BorderLayout.CENTER);
        page.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 0, 8, 12);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        addField(form, constraints, 0, "ROOM", roomPicker);
        addField(form, constraints, 1, "CHECK IN  (YYYY-MM-DD)", checkInField);
        addField(form, constraints, 2, "CHECK OUT  (YYYY-MM-DD)", checkOutField);
        guestSpinner.setFont(new Font("SansSerif", Font.PLAIN, 14));
        addField(form, constraints, 3, "GUESTS", guestSpinner);

        JPanel actions = new JPanel(new BorderLayout(0, 14));
        actions.setOpaque(false);
        estimateLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        estimateLabel.setForeground(INK);
        JButton bookButton = new JButton("Reserve this stay");
        bookButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        bookButton.setForeground(Color.WHITE);
        bookButton.setBackground(GREEN);
        bookButton.setFocusPainted(false);
        bookButton.setBorder(BorderFactory.createEmptyBorder(13, 18, 13, 18));
        bookButton.addActionListener(event -> createBooking());
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setForeground(MUTED);
        actions.add(estimateLabel, BorderLayout.NORTH);
        actions.add(bookButton, BorderLayout.CENTER);
        actions.add(statusLabel, BorderLayout.SOUTH);
        page.add(form, BorderLayout.CENTER);
        page.add(actions, BorderLayout.SOUTH);

        roomPicker.addActionListener(event -> updateEstimate());
        checkInField.addActionListener(event -> updateEstimate());
        checkOutField.addActionListener(event -> updateEstimate());
        guestSpinner.addChangeListener(event -> updateEstimate());
        updateEstimate();
        return page;
    }

    private void addField(JPanel form, GridBagConstraints constraints, int row, String label, java.awt.Component input) {
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0;
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        fieldLabel.setForeground(MUTED);
        form.add(fieldLabel, constraints);
        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.insets = new Insets(8, 16, 8, 0);
        input.setFont(new Font("SansSerif", Font.PLAIN, 14));
        if (input instanceof JTextField textField) {
            textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 220, 211)),
                BorderFactory.createEmptyBorder(9, 10, 9, 10)));
        }
        form.add(input, constraints);
        constraints.insets = new Insets(8, 0, 8, 12);
    }

    private void updateEstimate() {
        try {
            BookingDetails details = readBookingDetails();
            estimateLabel.setText(String.format("%d nights  |  $%,.2f total", details.nights(), details.total()));
            statusLabel.setText("Rates include your full stay. No payment is collected in this demo.");
        } catch (IllegalArgumentException exception) {
            estimateLabel.setText(" ");
            statusLabel.setText(exception.getMessage());
        }
    }

    private BookingDetails readBookingDetails() {
        LocalDate checkIn;
        LocalDate checkOut;
        try {
            checkIn = LocalDate.parse(checkInField.getText().trim());
            checkOut = LocalDate.parse(checkOutField.getText().trim());
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException("Enter valid dates in YYYY-MM-DD format.");
        }
        if (!checkIn.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in must be a future date.");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out must be after check-in.");
        }
        Room room = (Room) roomPicker.getSelectedItem();
        int guests = (Integer) guestSpinner.getValue();
        if (guests > room.capacity()) {
            throw new IllegalArgumentException(room.name() + " accommodates up to " + room.capacity() + " guests.");
        }
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return new BookingDetails(room, checkIn, checkOut, guests, nights, nights * room.rate());
    }

    private void createBooking() {
        try {
            BookingDetails details = readBookingDetails();
            String reference = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            statusLabel.setText("Booking confirmed | " + reference + " | " + details.checkIn() + " to " + details.checkOut());
            JOptionPane.showMessageDialog(null,
                "Your stay is reserved.\n\n" + details.room().name() + " for " + details.guests() + " guests\n"
                    + details.nights() + " nights | $" + String.format("%,.2f", details.total())
                    + "\nReference: " + reference,
                "Booking confirmed", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Check your booking", JOptionPane.WARNING_MESSAGE);
        }
    }

    private record Room(String name, String description, double rate, int capacity) {
        @Override
        public String toString() {
            return String.format("%s  |  $%.0f/night  |  %s", name, rate, description);
        }
    }

    private record BookingDetails(Room room, LocalDate checkIn, LocalDate checkOut, int guests, long nights, double total) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Use Swing's default look and feel when the platform theme is unavailable.
            }
            new HotelBookingApp();
        });
    }
}
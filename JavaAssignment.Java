# JavaAssignment.java
Unveiling Hidden Talents in Tanzania
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class TalentShowcaseApp {
    private JFrame frame;
    private JTextField nameField, talentField, videoLinkField, searchField;
    private JTextArea storyField, bioField, talentDisplayArea;
    private JComboBox<String> categoryComboBox, levelComboBox;
    private JLabel quoteLabel;
    private JButton submitButton, searchButton;
    private JProgressBar progressBar;
    private ArrayList<TalentEntry> talents;

    private static final String FILE_NAME = "talents.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                TalentShowcaseApp window = new TalentShowcaseApp();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public TalentShowcaseApp() {
        talents = new ArrayList<>();
        loadTalentEntries(); // Load existing entries from file
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Unveiling Hidden Talents In Tanzania"); // Updated title
        frame.setBounds(100, 100, 600, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add form fields
        addFormFields(panel, gbc);

        // Talent display area
        talentDisplayArea = new JTextArea();
        talentDisplayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(talentDisplayArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Quote section
        quoteLabel = new JLabel();
        frame.getContentPane().add(quoteLabel, BorderLayout.SOUTH);
        updateQuote();

        // Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        panel.add(progressBar, gbc);

        // Search bar
        addSearchFunctionality(panel, gbc);

        // Update talent display and progress bar
        updateTalentDisplay();
        updateProgressBar();
    }

    private void addFormFields(JPanel panel, GridBagConstraints gbc) {
        // Name field
        addLabelAndField(panel, gbc, "Enter Your Name:", nameField = new JTextField(), 0);

        // Talent category
        addLabelAndComboBox(panel, gbc, "Select Talent Category: ",
                categoryComboBox = new JComboBox<>(new String[]{"Music", "Art", "Dance", "Sports", "Writing", "Others"}), 1);

        // Talent field
        addLabelAndField(panel, gbc, "Enter Your Talent:", talentField = new JTextField(), 2);

        // Talent story
        addLabelAndTextArea(panel, gbc, "Tell Your Talent Journey:", storyField = new JTextArea(), 3);

        // Bio field
        addLabelAndTextArea(panel, gbc, "Enter Your Bio:", bioField = new JTextArea(), 4);

        // Video link
        addLabelAndField(panel, gbc, "Enter Video Link (Optional):", videoLinkField = new JTextField(), 5);

        // Talent level
        addLabelAndComboBox(panel, gbc, "Select Talent Level:",
                levelComboBox = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Expert"}), 6);

        // Submit button
        submitButton = new JButton("Submit and Show Talent");
        submitButton.addActionListener(e -> handleSubmitAction());
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(submitButton, gbc);
    }

    private void addSearchFunctionality(JPanel panel, GridBagConstraints gbc) {
        searchField = new JTextField(15);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> handleSearchAction());
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        panel.add(searchField, gbc);
        gbc.gridx = 1;
        panel.add(searchButton, gbc);
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, JTextField textField, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        panel.add(textField, gbc);
    }

    private void addLabelAndComboBox(JPanel panel, GridBagConstraints gbc, String labelText, JComboBox<String> comboBox, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        comboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        comboBox.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        panel.add(comboBox, gbc);
    }

    private void addLabelAndTextArea(JPanel panel, GridBagConstraints gbc, String labelText, JTextArea textArea, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(250, 80));
        gbc.gridx = 1;
        panel.add(scrollPane, gbc);
    }

    private void handleSubmitAction() {
        String name = nameField.getText().trim();
        String talent = talentField.getText().trim();
        String story = storyField.getText().trim();
        String bio = bioField.getText().trim();
        String videoLink = videoLinkField.getText().trim();
        String category = (String) categoryComboBox.getSelectedItem();
        String level = (String) levelComboBox.getSelectedItem();

        if (name.isEmpty() || talent.isEmpty() || story.isEmpty() || bio.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all required fields.");
        } else {
            TalentEntry entry = new TalentEntry(name, talent, story, bio, videoLink, category, level);
            talents.add(entry);
            saveTalentEntries();
            clearFields();
            updateTalentDisplay();
            updateProgressBar();
            JOptionPane.showMessageDialog(frame, "Talent submitted successfully!");
        }
    }

    private void handleSearchAction() {
        String query = searchField.getText().trim().toLowerCase();
        talentDisplayArea.setText(""); // Clear previous results
        boolean found = false;
        for (TalentEntry talent : talents) {
            if (talent.getName().toLowerCase().contains(query) || talent.getTalent().toLowerCase().contains(query)) {
                found = true;
                talentDisplayArea.append(talent.toString());
                talentDisplayArea.append("-------------------------------\n");
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(frame, "No results found for your search.");
        }
    }

    private void updateTalentDisplay() {
        talentDisplayArea.setText("");
        for (TalentEntry talent : talents) {
            talentDisplayArea.append(talent.toString());
            talentDisplayArea.append("-------------------------------\n");
        }
    }

    private void updateQuote() {
        String[] quotes = {
                "Believe in yourself, and you're halfway there!",
                "The only limit to your impact is your imagination and commitment.",
                "Great things never come from comfort zones."
        };
        quoteLabel.setText("<html><i>" + quotes[new Random().nextInt(quotes.length)] + "</i></html>");
    }

    private void updateProgressBar() {
        progressBar.setValue(talents.size() * 10);
    }

    private void clearFields() {
        nameField.setText("");
        talentField.setText("");
        storyField.setText("");
        bioField.setText("");
        videoLinkField.setText("");
        categoryComboBox.setSelectedIndex(0);
        levelComboBox.setSelectedIndex(0);
    }

    private void saveTalentEntries() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(talents);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving data: " + e.getMessage());
        }
    }

    private void loadTalentEntries() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                talents = (ArrayList<TalentEntry>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                talents = new ArrayList<>();
            }
        }
    }

    private static class TalentEntry implements Serializable {
        private String name, talent, story, bio, videoLink, category, level;

        public TalentEntry(String name, String talent, String story, String bio, String videoLink, String category, String level) {
            this.name = name;
            this.talent = talent;
            this.story = story;
            this.bio = bio;
            this.videoLink = videoLink;
            this.category = category;
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public String getTalent() {
            return talent;
        }

        @Override
        public String toString() {
            return String.format("Name: %s\nTalent: %s\nCategory: %s\nLevel: %s\nStory: %s\nBio: %s\nVideo Link: %s\n",
                    name, talent, category, level, story, bio, videoLink);
        }
    }
}

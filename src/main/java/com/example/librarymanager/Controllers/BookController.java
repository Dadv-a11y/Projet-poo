package com.example.librarymanager.Controllers;

import com.example.librarymanager.Database.BooksTable;
import com.example.librarymanager.Database.CategoryTable;
import com.example.librarymanager.Models.Books;
import com.example.librarymanager.Models.Category;
import com.example.librarymanager.utils.Alertmessage;
import com.example.librarymanager.utils.FormValidation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * JavaFX controller for managing the book grid and the book addition form.
 *
 * This controller handles:
 * - Displaying all books in a grid layout.
 * - Adding new books via a form.
 * - Creating categories on the fly if they do not exist.
 * - Validating user input before adding a book.
 * - Refreshing the grid after any change (add, edit, delete).
 *
 * Main features:
 * - Reads and displays books from the database.
 * - Allows users to add a new book, including category management.
 * - Ensures available copies do not exceed total copies.
 * - Handles image paths and default images.
 * - Provides feedback to the user via alerts.
 *
 * Dependencies:
 * - BooksTable: for book database operations.
 * - CategoryTable: for category database operations.
 * - BookCardController: for displaying each book in the grid.
 * - Alertmessage & FormValidation: for user feedback and input validation.
 *
 * Usage:
 * - The controller is initialized automatically by JavaFX.
 * - Call handleAddBook(ActionEvent) when the user submits the add book form.
 * - The grid is refreshed automatically after any change.
 *
 * FXML requirements:
 * - TextFields: titleField, authorField, categoryField, isbnField, yearField,
 * totalCopiesField, availableCopiesField, imageNameField
 * - TextArea: bookDescriptionArea
 * - Button: addBookButton, chooseImageButton
 * - GridPane: bookGrid
 */
public class BookController implements Initializable {

    /**
     * Button to add a new book.
     */
    @FXML
    private Button addBookButton;

    /**
     * Text field for the author's name.
     */
    @FXML
    private TextField authorField;

    /**
     * Text field for the number of available copies.
     */
    @FXML
    private TextField availableCopiesField;

    /**
     * Text field for the book's category.
     */
    @FXML
    private TextField categoryField;

    /**
     * Button to choose an image for the book.
     */
    @FXML
    private Button chooseImageButton;

    /**
     * Text field for the book's ISBN.
     */
    @FXML
    private TextField isbnField;

    /**
     * Text field for the book's title.
     */
    @FXML
    private TextField titleField;

    /**
     * Text field for the total number of copies.
     */
    @FXML
    private TextField totalCopiesField;

    /**
     * Text field for the book's publication year.
     */
    @FXML
    private TextField yearField;

    /**
     * GridPane displaying all book cards.
     */
    @FXML
    private GridPane bookGrid;

    /**
     * Text field for the image file name.
     */
    @FXML
    private TextField imageNameField;

    /**
     * Text area for the book's description.
     */
    @FXML
    private TextArea bookDescriptionArea;

    /**
     * List of all books displayed in the grid.
     */
    private List<Books> books;

    /**
     * List of all categories.
     */
    private List<Category> categories;

    /**
     * Table handler for category operations.
     */
    private CategoryTable categoryTable = new CategoryTable();

    /**
     * Table handler for book operations.
     */
    private BooksTable booksTable = new BooksTable();

    /**
     * Initializes the controller, loads categories and books, and populates the
     * grid.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categories = getCategories();
        try {
            // Initialize the grid with proper error handling
            if (bookGrid == null) {
                System.err.println("Error: bookGrid is null. FXML might not be loaded correctly.");
                return;
            }

            // Configure grid properties
            bookGrid.setHgap(20); // Set horizontal gap between columns
            bookGrid.setVgap(20); // Set vertical gap between rows
            bookGrid.setPadding(new Insets(20)); // Set padding around the grid

            // Initialize books list and populate grid
            books = new ArrayList<>(data());
            refreshBookGrid();

        } catch (Exception e) {
            System.err.println("Error initializing BookController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads all books from the database and ensures image paths are valid.
     * 
     * @return list of books
     */
    private List<Books> data() {
        List<Books> books = new ArrayList<>();
        try {
            books = booksTable.listAll();
            for (Books book : books) {
                // Verify and fix image path
                String imagePath = book.getImage_path();
                if (!imagePath.startsWith("/")) {
                    imagePath = "/" + imagePath;
                    book.setImage_path(imagePath);
                }
                if (getClass().getResource(imagePath) == null) {
                    System.err.println("Warning: Image not found in classpath: " + imagePath);
                    book.setImage_path("/Images/test1.jpg"); // Set default image if not found
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching data from the database: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error creating sample book data: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    /**
     * Returns the list of books currently managed by the controller.
     * 
     * @return list of books
     */
    public List<Books> getBooks() {
        return books;
    }

    /**
     * Handles the add book button click event.
     * Validates input, creates the book and category if needed, and refreshes the
     * grid.
     * 
     * @param event the action event
     */
    @FXML
    void handleAddBook(ActionEvent event) {
        // Validate input fields
        if (!FormValidation.isValidInput(titleField, authorField, isbnField, yearField, categoryField, totalCopiesField,
                availableCopiesField, imageNameField, bookDescriptionArea)) {
            return; // Stop submission if validation fails
        }

        String title = titleField.getText();
        String author = authorField.getText();
        String category = categoryField.getText();
        String isbn = isbnField.getText();
        String year = yearField.getText();
        String totalCopies = totalCopiesField.getText();
        String availableCopies = availableCopiesField.getText();
        String imageName = imageNameField.getText();
        String description = bookDescriptionArea.getText();
        int totalCopies_parseInt;
        int availableCopies_parseInt;
        try {
            totalCopies_parseInt = Integer.parseInt(totalCopies);
            availableCopies_parseInt = Integer.parseInt(availableCopies);
        } catch (NumberFormatException e) {
            Alertmessage.showAlert(AlertType.ERROR, "error", "Invalid copies format : Must be numbers");
            return;
        }

        if (availableCopies_parseInt > totalCopies_parseInt) {
            Alertmessage.showAlert(AlertType.ERROR, "Error", "available Copies should be less than total Copies");
            return;
        }

        Books newBook = new Books();
        newBook.setTitle(title);
        newBook.setAuthor(author);
        int category_id = getCategoryIdByName(category);
        newBook.setCategory_id(category_id);
        newBook.setIsbn(isbn);
        newBook.setYear(year);
        newBook.setTotal_copies(totalCopies_parseInt);
        newBook.setAvailable_copies(availableCopies_parseInt);
        if (!imageName.trim().isEmpty()) {
            newBook.setImage_path("/Images/" + imageName.trim());
        } else {
            newBook.setImage_path(""); // or set a default image path if desired
        }
        newBook.setDescription(description);

        // Add the new book to the database
        try {
            booksTable.create(newBook);
            Alertmessage.showAlert(AlertType.INFORMATION, "Success", "Book added successfully");
            if (books == null) {
                books = new ArrayList<>();
            }
            books.add(newBook);
            books = new ArrayList<>(data());
            refreshBookGrid();
            clearForm();

        } catch (SQLException e) {
            System.err.println("Error adding book to database: " + e.getMessage());
            Alertmessage.showAlert(AlertType.ERROR, "Error", "Failed to add book to database" + e.getMessage());
            return;
        } catch (Exception e) {
            System.err.println("Error adding book to database: " + e.getMessage());
            Alertmessage.showAlert(AlertType.ERROR, "Error", "Failed to add book to database" + e.getMessage());
            return;
        }
    }

    /**
     * Refreshes the book grid with the current list of books.
     */
    public void refreshBookGrid() {
        bookGrid.getChildren().clear();
        int columns = 0;
        int rows = 0;

        for (Books book : books) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Fxml/BookCard.fxml"));

                if (fxmlLoader.getLocation() == null) {
                    System.err.println("Error: Could not find BookCard.fxml resource");
                    continue;
                }

                VBox bookBox = fxmlLoader.load();
                BookCardController bookCardController = fxmlLoader.getController();

                if (bookCardController == null) {
                    System.err.println("Error: BookCardController is null");
                    continue;
                }

                // Pass the current BookController to the BookCardController
                bookCardController.setBookController(this);

                bookCardController.setData(book);

                // Configure book card layout
                bookBox.setPrefWidth(200);
                bookBox.setPrefHeight(300);
                bookBox.setAlignment(Pos.CENTER);
                bookBox.setStyle("-fx-padding: 10;");

                // Add to grid with proper positioning
                GridPane.setMargin(bookBox, new Insets(10));
                bookGrid.add(bookBox, columns, rows);

                // Update grid position
                columns++;
                if (columns == 3) {
                    columns = 0;
                    rows++;
                }

            } catch (IOException e) {
                System.err.println("Error loading book card: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads all categories from the database.
     * 
     * @return list of categories
     */
    private List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        try {
            // Fetch categories from the database
            CategoryTable CategoryTable = new CategoryTable();
            categories = CategoryTable.listAll();
        } catch (SQLException e) {
            System.err.println("Error fetching categories: " + e.getMessage());
        }
        return categories;
    }

    /**
     * Gets the category ID by name, creating the category if it does not exist.
     * 
     * @param name the category name
     * @return the category ID
     */
    private int getCategoryIdByName(String name) {
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category.getCategory_id();
            }
        }
        try {
            categoryTable.create(new Category(name));
        } catch (SQLException e) {
            Alertmessage.showAlert(AlertType.ERROR, "Error", "Failed to create category");
            return -1; // Return -1 in case of an error
        } catch (Exception e) {
            Alertmessage.showAlert(AlertType.ERROR, "Error", "Failed to create category");
            return -1;
        }
        categories = getCategories();
        return getCategoryIdByName(name);
    }

    /**
     * Clears all fields in the add book form.
     */
    public void clearForm() {
        titleField.clear();
        authorField.clear();
        categoryField.clear();
        isbnField.clear();
        yearField.clear();
        totalCopiesField.clear();
        availableCopiesField.clear();
        imageNameField.clear();
        bookDescriptionArea.clear();
    }
}
package com.example.librarymanager.Models;

import java.util.Objects;

/**
 * Data model class representing a book in the library.
 *
 * This class encapsulates all properties of a book, including its ID, title,
 * author,
 * ISBN, category, publication year, total and available copies, image path, and
 * description.
 *
 * Main features:
 * - Provides constructors for creating book instances.
 * - Getters and setters for all book properties.
 * - Utility methods for setting year and copies from string or integer values.
 * - Overrides equals and hashCode for proper comparison based on title, author,
 * and ISBN.
 *
 * Fields:
 * - int book_id: Unique identifier for the book.
 * - String title: Title of the book.
 * - String author: Author of the book.
 * - String isbn: ISBN number.
 * - int category_id: Category identifier.
 * - int published_year: Year the book was published.
 * - int copies_total: Total number of copies.
 * - int copies_available: Number of available copies.
 * - String image_path: Path to the book's cover image.
 * - String description: Description of the book.
 */
public class Books {
    private int book_id;
    private String title;
    private String author;
    private String isbn;
    private int category_id;
    private int published_year;
    private int copies_total;
    private int copies_available;
    private String image_path;
    private String description;

    public Books() {
    }

    public Books(int book_id, String title, String author, String isbn, int category_id, int published_year,
            int copies_total, int copies_available, String image_path, String description) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category_id = category_id;
        this.published_year = published_year;
        this.copies_total = copies_total;
        this.copies_available = copies_available;
        this.image_path = image_path;
        this.description = description;
    }

    // Getters and Setters

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getPublished_year() {
        return published_year;
    }

    public void setPublished_year(int published_year) {
        this.published_year = published_year;
    }

    public int getCopies_total() {
        return copies_total;
    }

    public void setCopies_total(int copies_total) {
        this.copies_total = copies_total;
    }

    public int getCopies_available() {
        return copies_available;
    }

    public void setCopies_available(int copies_available) {
        this.copies_available = copies_available;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Method to handle string year input
    public void setYear(String year) {
        try {
            this.published_year = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid year format");
        }
    }

    /**
     * Sets the total number of copies.
     * 
     * @param totalCopies the total number of copies
     */
    public void setTotal_copies(int totalCopies) {
        this.copies_total = totalCopies;
    }

    /**
     * Sets the number of available copies.
     * 
     * @param availableCopies the number of available copies
     */
    public void setAvailable_copies(int availableCopies) {
        this.copies_available = availableCopies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Books books = (Books) o;
        return Objects.equals(title, books.title) &&
                Objects.equals(author, books.author) &&
                Objects.equals(isbn, books.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, isbn);
    }
}
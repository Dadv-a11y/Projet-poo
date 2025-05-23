package com.example.librarymanager.Database;

import com.example.librarymanager.Models.Books;

import com.example.librarymanager.utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object (DAO) for managing books in the database.
 *
 * This class provides CRUD operations and search functionality for books,
 * as well as methods to retrieve books by ISBN or title.
 *
 * Main features:
 * - Create, update, delete, and list all books.
 * - Search books by title, category, and author.
 * - Retrieve a book by its ISBN or title.
 * - Count the total number of books in the database.
 *
 * Dependencies:
 * - Books: the book data model.
 * - DatabaseUtils: utility for obtaining database connections.
 * - Repository<Books>: interface for generic CRUD operations.
 *
 * SQL:
 * - Uses prepared statements for all queries.
 * - Joins with the category table to fetch category names.
 */
public class BooksTable implements Repository<Books> {

    private static final String QUERY_LIST_ALL_BOOKS = "SELECT b.* , c.name as category FROM books b  left join category c on b.category_id = c.category_id ;";
    private static final String QUERY_SEARCH_BOOKS = "SELECT b.*, c.name as category FROM books b LEFT JOIN category c ON b.category_id = c.category_id WHERE b.title LIKE ? AND c.name LIKE ? AND b.author LIKE ?;";
    private static final String QUERY_ADD_BOOK = "INSERT INTO books (title, author, isbn, category_id, published_year, copies_total, copies_available,image_path,description) VALUES (?, ?, ?, ?, ?, ?, ?,?,?);";
    private static final String QUERY_UPDATE_BOOK = "UPDATE books SET title = ?, author = ?, isbn = ?, category_id = ?, published_year = ?, copies_total = ?, copies_available = ? , image_path = ? WHERE book_id = ?;";
    private static final String QUERY_DELETE_BOOK = "DELETE FROM books WHERE book_id = ?;";
    private static final String COUNT_BOOKS = "SELECT COUNT(*) FROM books;";

    /**
     * Adds a new book to the database.
     * 
     * @param book the book to add
     * @throws SQLException if a database error occurs
     */
    @Override
    public void create(Books book) throws SQLException {
        Connection conn = DatabaseUtils.getConnection();
        PreparedStatement stmt = conn.prepareStatement(QUERY_ADD_BOOK);
        stmt.setString(1, book.getTitle());
        stmt.setString(2, book.getAuthor());
        stmt.setString(3, book.getIsbn());
        stmt.setInt(4, book.getCategory_id());
        stmt.setInt(5, book.getPublished_year());
        stmt.setInt(6, book.getCopies_total());
        stmt.setInt(7, book.getCopies_available());
        stmt.setString(8, book.getImage_path());
        stmt.setString(9, book.getDescription());
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    /**
     * Updates an existing book in the database.
     * 
     * @param book the book to update
     * @throws SQLException if a database error occurs
     */
    @Override
    public void Update(Books book) throws SQLException {
        Connection conn = DatabaseUtils.getConnection();
        PreparedStatement stmt = conn.prepareStatement(QUERY_UPDATE_BOOK);
        stmt.setString(1, book.getTitle());
        stmt.setString(2, book.getAuthor());
        stmt.setString(3, book.getIsbn());
        stmt.setInt(4, book.getCategory_id());
        stmt.setInt(5, book.getPublished_year());
        stmt.setInt(6, book.getCopies_total());
        stmt.setInt(7, book.getCopies_available());
        stmt.setString(8, book.getImage_path());
        stmt.setInt(9, book.getBook_id());
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    /**
     * Deletes a book from the database by its ID.
     * 
     * @param id the book's ID
     * @throws SQLException if a database error occurs
     */
    @Override
    public void Delete(int id) throws SQLException {
        Connection conn = DatabaseUtils.getConnection();
        PreparedStatement stmt = conn.prepareStatement(QUERY_DELETE_BOOK);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    /**
     * Retrieves all books from the database.
     * 
     * @return a list of all books
     * @throws SQLException if a database error occurs
     */
    @Override
    public List<Books> listAll() throws SQLException {
        List<Books> books = new ArrayList<>();
        Connection conn = DatabaseUtils.getConnection();
        PreparedStatement stmt = conn.prepareStatement(QUERY_LIST_ALL_BOOKS);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Books book = new Books();
            book.setBook_id(rs.getInt("book_id"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setIsbn(rs.getString("isbn"));
            book.setCategory_id(rs.getInt("category_id"));
            book.setPublished_year(rs.getInt("published_year"));
            book.setCopies_total(rs.getInt("copies_total"));
            book.setCopies_available(rs.getInt("copies_available"));
            book.setImage_path(rs.getString("image_path"));
            books.add(book);
        }
        rs.close();
        stmt.close();
        conn.close();
        return books;
    }

    /**
     * Searches for books by title, category, and author.
     * 
     * @param title    the book title (partial match)
     * @param category the category name (partial match)
     * @param author   the author name (partial match)
     * @return a list of matching books
     * @throws SQLException if a database error occurs
     */
    public List<Books> search(String title, String category, String author) throws SQLException {
        List<Books> books = new ArrayList<>();
        Connection conn = DatabaseUtils.getConnection();
        PreparedStatement stmt = conn.prepareStatement(QUERY_SEARCH_BOOKS);
        stmt.setString(1, "%" + title + "%");
        stmt.setString(2, "%" + category + "%");
        stmt.setString(3, "%" + author + "%");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Books book = new Books();
            book.setBook_id(rs.getInt("book_id"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setIsbn(rs.getString("isbn"));
            book.setCategory_id(rs.getInt("category_id"));
            book.setPublished_year(rs.getInt("published_year"));
            book.setCopies_total(rs.getInt("copies_total"));
            book.setCopies_available(rs.getInt("copies_available"));
            book.setImage_path(rs.getString("image_path"));
            books.add(book);
        }
        rs.close();
        stmt.close();
        conn.close();
        return books;
    }

    /**
     * Counts the total number of books in the database.
     * 
     * @return the number of books
     * @throws SQLException if a database error occurs
     */
    @Override
    public int Count() throws SQLException {
        Connection conn = DatabaseUtils.getConnection();
        PreparedStatement stmt = conn.prepareStatement(COUNT_BOOKS);
        ResultSet rs = stmt.executeQuery();
        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }
        rs.close();
        stmt.close();
        conn.close();
        return count;
    }

    private static final String QUERY_GET_BOOK_BY_ISBN = "SELECT b.*, c.name as category FROM books b LEFT JOIN category c ON b.category_id = c.category_id WHERE b.isbn = ?;";

    /**
     * Retrieves a book by its ISBN.
     * 
     * @param isbn the book's ISBN
     * @return the matching book, or null if not found
     * @throws SQLException if a database error occurs
     */
    public Books getBookByISBN(String isbn) throws SQLException {
        Books book = null;
        Connection conn = DatabaseUtils.getConnection();
        PreparedStatement stmt = conn.prepareStatement(QUERY_GET_BOOK_BY_ISBN);
        stmt.setString(1, isbn);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            book = new Books();
            book.setBook_id(rs.getInt("book_id"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setIsbn(rs.getString("isbn"));
            book.setCategory_id(rs.getInt("category_id"));
            book.setPublished_year(rs.getInt("published_year"));
            book.setCopies_total(rs.getInt("copies_total"));
            book.setCopies_available(rs.getInt("copies_available"));
            book.setImage_path(rs.getString("image_path"));
        }

        rs.close();
        stmt.close();
        conn.close();
        return book;
    }

    private static final String QUERY_GET_BOOK_BY_TITLE = "SELECT b.*, c.name as category FROM books b LEFT JOIN category c ON b.category_id = c.category_id WHERE b.title = ?;";

    /**
     * Retrieves a book by its title.
     * 
     * @param title the book's title
     * @return the matching book, or null if not found
     * @throws SQLException if a database error occurs
     */
    public static Books getBookByTitle(String title) throws SQLException {
        Books book = null;
        Connection conn = DatabaseUtils.getConnection();
        PreparedStatement stmt = conn.prepareStatement(QUERY_GET_BOOK_BY_TITLE);
        stmt.setString(1, title);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            book = new Books();
            book.setBook_id(rs.getInt("book_id"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setIsbn(rs.getString("isbn"));
            book.setCategory_id(rs.getInt("category_id"));
            book.setPublished_year(rs.getInt("published_year"));
            book.setCopies_total(rs.getInt("copies_total"));
            book.setCopies_available(rs.getInt("copies_available"));
            book.setImage_path(rs.getString("image_path"));
        }

        rs.close();
        stmt.close();
        conn.close();
        return book;
    }
}
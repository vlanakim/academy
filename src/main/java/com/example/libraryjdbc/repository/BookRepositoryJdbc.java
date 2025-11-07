package com.example.libraryjdbc.repository;

import com.example.libraryjdbc.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJdbc implements BookRepository {
    private final JdbcTemplate jdbc;
    private static final BeanPropertyRowMapper<Book> MAPPER =
            new BeanPropertyRowMapper<>(Book.class);

    @Override
    public List<Book> findAll() {
        return jdbc.query("""
            SELECT id, title, author, publication_year
            FROM books ORDER BY id
            """, MAPPER);
    }

    @Override
    public Optional<Book> findById(Long id) {
        var list = jdbc.query("""
            SELECT id, title, author, publication_year
            FROM books WHERE id = ?
            """, MAPPER, id);
        return list.stream().findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        Integer cnt = jdbc.queryForObject(
                "SELECT COUNT(*) FROM books WHERE id = ?", Integer.class, id);
        return cnt != null && cnt > 0;
    }

    @Override
    public Book save(Book book) {
        String sql = "INSERT INTO books (title, author, publication_year) VALUES (?, ?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getPublicationYear());
            return ps;
        }, kh);
        Number key = kh.getKey();
        if (key != null) book.setId(key.longValue());
        return book;
    }

    @Override
    public Book update(Long id, Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, publication_year = ? WHERE id = ?";
        jdbc.update(sql, book.getTitle(), book.getAuthor(), book.getPublicationYear(), id);
        book.setId(id);
        return book;
    }

    @Override
    public void deleteById(Long id) {
        jdbc.update("DELETE FROM books WHERE id = ?", id);
    }
}

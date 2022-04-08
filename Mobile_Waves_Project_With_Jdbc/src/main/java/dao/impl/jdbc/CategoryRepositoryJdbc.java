package dao.impl.jdbc;

import dao.CategoryRepository;
import exception.EntityPersistenceException;
import exception.NonExistingEntityException;
import lombok.extern.slf4j.Slf4j;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class CategoryRepositoryJdbc implements CategoryRepository {
    private Connection connection;
    public static final String DOES_CATEGORY_EXIST = "SELECT categories.* from categories where id = ?";
    public static final String FIND_CATEGORY_BY_ID = "SELECT categories.*, users.email, users.username, users.password, roles.name from categories join users on categories.user_id=users.id join roles on users.role_id=roles.id\n" +
            "where categories.id like ?";
    public static final String INSERT_NEW_CATEGORY = "INSERT INTO `categories` (`name`, `user_id`) VALUES (?, ?);";
    public static final String SELECT_ALL_CATEGORIES = "SELECT categories.id, categories.name FROM categories";
    public static final String UPDATE_CATEGORY_BY_ID = "update categories set name = ? where id = ?;\n";
    public static final String UPDATE_CATEGORY_USER_BY_ID = "Update categories set user_id = ? where id = ?;";
    public static final String UPDATE_REMOVE_USER_FROM_CATEGORY = "Update categories set user_id = NULL where id = ?";
    public static final String DELETE_CATEGORY_BY_ID = "DELETE FROM categories WHERE id=?;";


    public CategoryRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Category update(Long oldCategoryId, Category newCategory) throws NonExistingEntityException {
        try {
            var stmt = connection.prepareStatement(UPDATE_CATEGORY_BY_ID);

            var existingCategory = findById(oldCategoryId);

            if (existingCategory == null) {
                throw new NonExistingEntityException("Category with ID='" + oldCategoryId + "' does not exist.");
            }

            String name = newCategory.getName();

            if (name != null) {
                stmt.setString(1, name);
                stmt.setLong(2, oldCategoryId);
                stmt.execute();
            }

            return existingCategory;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new EntityPersistenceException("Error rolling back SQL query: " + ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + ex);
        }
    }

    @Override
    public Collection<Category> findAll() {
        try (var stmt = connection.prepareStatement(SELECT_ALL_CATEGORIES)) {
            // 4. Set params and execute SQL query
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            return toCategories(rs);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_CATEGORIES, ex);
        }
    }

    @Override
    public Category findById(Long id) throws NonExistingEntityException {
        try {
            var stmtExist = connection.prepareStatement(DOES_CATEGORY_EXIST);
            stmtExist.setLong(1, id);
            var rsExist = stmtExist.executeQuery();
            if(!rsExist.isBeforeFirst())
            {
                throw new NonExistingEntityException("Category with Id='" + id + "' doesn't exist");
            }

            var stmt = connection.prepareStatement(DOES_CATEGORY_EXIST);
            stmt.setLong(1, id);
            var rs = stmt.executeQuery();
            Administrator administrator;
            rs.next();
            Category category = null;
                category = new Category(
                        rs.getLong(1),
                        rs.getString("name"));
                rs.getInt("categories.user_id");
                var anotherBool = rs.wasNull();
                if (!anotherBool) {
                    var stmtAnother = connection.prepareStatement(FIND_CATEGORY_BY_ID);
                    stmtAnother.setLong(1, id);
                    var rsAnother = stmtAnother.executeQuery();
                    rsAnother.next();
                    administrator = new Administrator(
                            rsAnother.getLong(3),
                            rsAnother.getString("email"),
                            rsAnother.getString("username"),
                            rsAnother.getString("password"),
                            Role.valueOf(rsAnother.getString("roles.name")));

                    category.setAuthor(administrator);


            }
            return category;

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + FIND_CATEGORY_BY_ID, ex);
        }
    }

    @Override
    public Category create(Category entity, Long adminId) {
        try (var stmt = connection.prepareStatement(INSERT_NEW_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            // 4. Set params and execute SQL query
            stmt.setString(1, entity.getName());
            stmt.setLong(2, adminId);

            // 5. Execute insert statement
            connection.setAutoCommit(false);
            var affectedRows = stmt.executeUpdate();
            // more updates here ...
            connection.commit();
            connection.setAutoCommit(true);

            // 6. Check results and Get generated primary ke
            if (affectedRows == 0) {
                throw new EntityPersistenceException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                    return entity;
                } else {
                    throw new EntityPersistenceException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new EntityPersistenceException("Error rolling back SQL query: " + INSERT_NEW_CATEGORY, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + INSERT_NEW_CATEGORY, ex);
        }
    }

    @Override
    public Category deleteById(Long id) {
        try {
            PreparedStatement stmt = connection.prepareStatement(DELETE_CATEGORY_BY_ID);

            var category = findById(id);

            stmt.setLong(1, id);
            stmt.execute();

            return category;
        } catch (SQLException | NonExistingEntityException ex) {
            throw new EntityPersistenceException("Error executing SQL query: " + DELETE_CATEGORY_BY_ID, ex);

        }
    }

    public List<Category> toCategories(ResultSet rs) throws SQLException {
        List<Category> results = new ArrayList<>();
        while (rs.next()) {
            results.add(new Category(
                    rs.getLong(1),
                    rs.getString("name")));
        }
        return results;
    }

}

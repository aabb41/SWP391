/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import entity.Category;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nam
 */
public class DAOCategory extends DBConnect {
    
    /**
     * Updates an existing category in the database
     * @param category The category object with updated information
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateCategory(Category category) {
        int n = 0;
        String sql = "UPDATE [Category] SET [CategoryName] = ?, [Status] = ? WHERE CategoryId = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, category.getCategoryName());
            pre.setInt(2, category.getStatus());
            pre.setInt(3, category.getCategoryId());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Inserts a new category into the database
     * @param category The category object to insert
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int insertCategory(Category category) {
        int n = 0;
        String sql = "INSERT INTO [Category]([CategoryName], [Status]) VALUES(?, ?)";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, category.getCategoryName());
            pre.setInt(2, category.getStatus());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Removes a category from the database
     * @param categoryId The ID of the category to remove
     * @return Number of rows affected (1 if successful, 0 if not, -1 if category has products)
     */
    public int removeCategory(int categoryId) {
        int n = 0;
        String sql = "DELETE FROM Category WHERE CategoryId = " + categoryId;
        
        try {
            // Check if category has products
            String sqlCheck = "SELECT ProductId FROM [Products] WHERE CategoryId = " + categoryId;
            ResultSet rs = getData(sqlCheck);
            if (rs.next()) {
                return -1; // Category has products, can't delete
            }
            
            // If no products, proceed with deletion
            Statement state = conn.createStatement();
            n = state.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DAOCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Retrieves categories from the database based on the provided SQL query
     * @param sql The SQL query to execute
     * @return Vector of Category objects
     */
    public Vector<Category> getCategories(String sql) {
        Vector<Category> vector = new Vector<Category>();
        
        try {
            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = state.executeQuery(sql);
            
            while (rs.next()) {
                int categoryId = rs.getInt("CategoryId");
                String categoryName = rs.getString("CategoryName");
                int status = rs.getInt("Status");
                
                Category category = new Category(categoryId, categoryName, status);
                vector.add(category);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vector;
    }
    
    /**
     * Retrieves all categories from the database
     * @return Vector of all Category objects
     */
    public Vector<Category> getAllCategories() {
        return getCategories("SELECT * FROM Category");
    }
    
    /**
     * Retrieves active categories from the database (Status = 1)
     * @return Vector of active Category objects
     */
    public Vector<Category> getActiveCategories() {
        return getCategories("SELECT * FROM Category WHERE Status = 1");
    }
    
    /**
     * Retrieves a category by its ID
     * @param categoryId The ID of the category to retrieve
     * @return The Category object if found, null otherwise
     */
    public Category getCategoryById(int categoryId) {
        String sql = "SELECT * FROM Category WHERE CategoryId = " + categoryId;
        Vector<Category> categories = getCategories(sql);
        
        if (categories.size() > 0) {
            return categories.get(0);
        }
        
        return null;
    }
    
    /**
     * Checks if a category name already exists in the database
     * @param categoryName The category name to check
     * @return true if the category name exists, false otherwise
     */
    public boolean categoryNameExists(String categoryName) {
        String sql = "SELECT * FROM Category WHERE CategoryName = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, categoryName);
            ResultSet rs = pre.executeQuery();
            
            return rs.next(); // Returns true if there's at least one result
        } catch (SQLException ex) {
            Logger.getLogger(DAOCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * Updates the status of a category
     * @param categoryId The ID of the category to update
     * @param status The new status value (0 for inactive, 1 for active)
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateCategoryStatus(int categoryId, int status) {
        int n = 0;
        String sql = "UPDATE Category SET Status = ? WHERE CategoryId = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, status);
            pre.setInt(2, categoryId);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Searches for categories by name
     * @param keyword The search keyword
     * @return Vector of matching Category objects
     */
    public Vector<Category> searchCategoriesByName(String keyword) {
        String sql = "SELECT * FROM Category WHERE CategoryName LIKE '%" + keyword + "%'";
        return getCategories(sql);
    }
    
    /**
     * Main method for testing the DAOCategory functionality
     */
    public static void main(String[] args) {
        DAOCategory dao = new DAOCategory();
        
        // Test getting all categories
        System.out.println("All categories:");
        Vector<Category> categories = dao.getAllCategories();
        for (Category category : categories) {
            System.out.println(category);
        }
        
        // Test inserting a new category
        Category newCategory = new Category(0, "Test Category", 1);
        int result = dao.insertCategory(newCategory);
        System.out.println("\nInsert result: " + (result > 0 ? "Success" : "Failed"));
        
        // Test getting active categories
        System.out.println("\nActive categories:");
        Vector<Category> activeCategories = dao.getActiveCategories();
        for (Category category : activeCategories) {
            System.out.println(category);
        }
        
        // Test searching categories
        System.out.println("\nSearch results for 'Test':");
        Vector<Category> searchResults = dao.searchCategoriesByName("Test");
        for (Category category : searchResults) {
            System.out.println(category);
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import entity.Products;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Vector;
import model.DAOProducts;

/**
 *
 * @author nam
 */
@WebServlet(name = "ProductController", urlPatterns = {"/ProductURL"})
public class ProductController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        DAOProducts dao = new DAOProducts();
        try (PrintWriter out = response.getWriter()) {
            String service = request.getParameter("service");
            
            if(service.equals("listProduct")) {
                Vector<Products> vector;
                String submit = request.getParameter("submit");
                if (submit == null) { // list all
                    vector = dao.getProducts("select * from Products");
                } else { // search
                    String searchField = request.getParameter("searchField");
                    String searchValue = request.getParameter("searchValue");
                    
                    String sql = "select * from Products where 1=1";
                    if (searchField != null && !searchField.isEmpty() && searchValue != null && !searchValue.isEmpty()) {
                        if (searchField.equals("ProductID")) {
                            sql += " AND ProductID = " + searchValue;
                        } else if (searchField.equals("ProductName")) {
                            sql += " AND ProductName like '%" + searchValue + "%'";
                        }else if (searchField.equals("SupplierID")) {
                            sql += " AND SupplierID = " + searchValue;
                        }else if (searchField.equals("CategoryID")) {
                            sql += " AND CategoryID = " + searchValue;
                        }
                    }
                    vector = dao.getProducts(sql);
                }
                
                request.setAttribute("productData", vector);
                request.setAttribute("titleTable", "Product List");
                
                RequestDispatcher dis = request.getRequestDispatcher("/jsp/listProduct.jsp");
                dis.forward(request, response);
            }
            
        }
    }
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

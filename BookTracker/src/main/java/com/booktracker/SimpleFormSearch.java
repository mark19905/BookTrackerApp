package com.booktracker;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SimpleFormSearch")
public class SimpleFormSearch extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public SimpleFormSearch() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	      String keyword = request.getParameter("keyword");
	      search(keyword, response);
	   }

	   void search(String keyword, HttpServletResponse response) throws IOException {
	      response.setContentType("text/html");
	      PrintWriter out = response.getWriter();
	      String title = "Database Result";
	      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n"; 
	      out.println(docType + "<html>\n" + "<head><title>" + title + "</title></head>\n" + "<body bgcolor=\"#f0f0f0\">\n" + "<h1 align=\"center\">" + title + "</h1>\n");

	      Connection connection = null;
	      PreparedStatement preparedStatement = null;
	      try {
	         DBConnection.getDBConnection();
	         connection = DBConnection.connection;

	         if (keyword.isEmpty()) {
	            String selectSQL = "SELECT * FROM books";
	            preparedStatement = connection.prepareStatement(selectSQL);
	         } else {
	            String selectSQL = "SELECT * FROM books WHERE title LIKE ?";
	            preparedStatement = connection.prepareStatement(selectSQL);
	            preparedStatement.setString(1, "%" + keyword + "%");  // Searching in title column
	         }
	         ResultSet rs = preparedStatement.executeQuery();

	         while (rs.next()) {
	        	    int id = rs.getInt("id");
	        	    String bookTitle = rs.getString("title").trim(); // Rename to bookTitle
	        	    String isbn = rs.getString("isbn").trim();
	        	    String author_name = rs.getString("author_name").trim();
	        	    String genre = rs.getString("genre").trim();
	        	    String reading_status = rs.getString("reading_status").trim();
	        	    int rating = rs.getInt("rating");

	        	    out.println("ID: " + id + ", ");
	        	    out.println("Title: " + bookTitle + ", "); // Use bookTitle here
	        	    out.println("ISBN: " + isbn + ", ");
	        	    out.println("Author: " + author_name + ", ");
	        	    out.println("Genre: " + genre + ", ");
	        	    out.println("Reading Status: " + reading_status + ", ");
	        	    out.println("Rating: " + rating + "<br>");
	        	}
	         out.println("<a href=/BookTracker/SimpleFormSearch.html>Search Data</a> <br>");
	         out.println("</body></html>");
	         rs.close();
	         preparedStatement.close();
	         connection.close();
	      } catch (SQLException se) {
	         se.printStackTrace();
	      } catch (Exception e) {
	         e.printStackTrace();
	      } finally {
	         try {
	            if (preparedStatement != null)
	               preparedStatement.close();
	         } catch (SQLException se2) {
	         }
	         try {
	            if (connection != null)
	               connection.close();
	         } catch (SQLException se) {
	            se.printStackTrace();
	         }
	      }
	   }

	   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	      doGet(request, response);
	   }
	}

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
	    String title = "Your Book Library";
	    String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
	    
	    // Add CSS
	    String styles = "<style>" + 
                "table { width: 100%; border-collapse: collapse; }" +
                "thead { background-color: green; color: white; }" +  // This line has been modified
                "tr { color: black; }" +  // This line has been added
                "td:nth-child(odd), th:nth-child(odd) { background-color: white; }" +
                "td:nth-child(even), th:nth-child(even) { background-color: lightgrey; }" +
                "</style>";

	    out.println(docType + 
	                "<html>\n" + 
	                "<head><title>" + title + "</title>" + styles + "</head>\n" + 
	                "<body bgcolor=\"#f0f0f0\">\n" + 
	                "<h1 align=\"center\">" + title + "</h1>\n");
	    
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet rs = null;  // Declare rs here

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
	        rs = preparedStatement.executeQuery();  // Initialize rs here

	        out.println("<table border='1' align='center'>");
	        out.println("<tr><th>ID</th><th>Title</th><th>ISBN</th><th>Author</th><th>Genre</th><th>Reading Status</th><th>Rating</th></tr>");
	        while (rs.next()) {
	            int id = rs.getInt("id");
	            String bookTitle = rs.getString("title").trim();
	            String isbn = rs.getString("isbn").trim();
	            String author_name = rs.getString("author_name").trim();
	            String genre = rs.getString("genre").trim();
	            String reading_status = rs.getString("reading_status").trim();
	            int rating = rs.getInt("rating");

	            out.println("<tr>");
	            out.println("<td>" + id + "</td>");
	            out.println("<td>" + bookTitle + "</td>");
	            out.println("<td>" + isbn + "</td>");
	            out.println("<td>" + author_name + "</td>");
	            out.println("<td>" + genre + "</td>");
	            out.println("<td>" + reading_status + "</td>");
	            out.println("<td>" + rating + "</td>");
	            out.println("</tr>");
	        }
	        out.println("</table>");
	        out.println("<a href=/BookTracker/SimpleFormSearch.html>Search Data</a> <br>");
	        out.println("</body></html>");

	    } catch (SQLException se) {
	        se.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) {
	                rs.close();
	            }
	            if (preparedStatement != null) {
	                preparedStatement.close();
	            }
	            if (connection != null) {
	                connection.close();
	            }
	        } catch (SQLException se) {
	            se.printStackTrace();
	        }
	    }
	}

	   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	      doGet(request, response);
	   }
	}

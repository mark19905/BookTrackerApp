package com.booktracker;


/**
 * @file SimpleFormInsert.java
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SimpleFormInsert")
public class SimpleFormInsert extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public SimpleFormInsert() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	      String title = request.getParameter("title");
	      String isbn = request.getParameter("isbn");
	      String author_name = request.getParameter("author_name");
	      String genre = request.getParameter("genre");
	      String reading_status = request.getParameter("reading_status");
	      int rating = Integer.parseInt(request.getParameter("rating"));

	      Connection connection = null;
	      String insertSql = "INSERT INTO books (title, isbn, author_name, genre, reading_status, rating) VALUES (?, ?, ?, ?, ?, ?)";

	      try {
	         DBConnection.getDBConnection();
	         connection = DBConnection.connection;
	         PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
	         preparedStmt.setString(1, title);
	         preparedStmt.setString(2, isbn);
	         preparedStmt.setString(3, author_name);
	         preparedStmt.setString(4, genre);
	         preparedStmt.setString(5, reading_status);
	         preparedStmt.setInt(6, rating);
	         preparedStmt.execute();
	         connection.close();
	      } catch (Exception e) {
	         e.printStackTrace();
	      }

	      // Set response content type
	      response.setContentType("text/html");
	      PrintWriter out = response.getWriter();
	      String titleMsg = "Insert Data to DB table";
	      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
	      out.println(docType + //
	            "<html>\n" + //
	            "<head><title>" + titleMsg + "</title></head>\n" + //
	            "<body bgcolor=\"#f0f0f0\">\n" + //
	            "<h2 align=\"center\">" + titleMsg + "</h2>\n" + //
	            "<ul>\n" + //

	            "  <li><b>Title</b>: " + title + "\n" + //
	            "  <li><b>ISBN</b>: " + isbn + "\n" + //
	            "  <li><b>Author Name</b>: " + author_name + "\n" + //
	            "  <li><b>Genre</b>: " + genre + "\n" + //
	            "  <li><b>Reading Status</b>: " + reading_status + "\n" + //
	            "  <li><b>Rating</b>: " + rating + "\n" + //

	            "</ul>\n");

	      out.println("<a href=/BookTracker/SimpleFormInsert.html>Insert Data</a> <br>");
	      out.println("</body></html>");
	   }

	   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	      doGet(request, response);
	   }
	}

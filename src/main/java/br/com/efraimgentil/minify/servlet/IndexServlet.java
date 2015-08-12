package br.com.efraimgentil.minify.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/index.jsp" })
public class IndexServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String requestURL = req.getRequestURL().toString();
		requestURL= requestURL.replaceAll("index.jsp", "source");
		if(requestURL.endsWith("/")){
			requestURL += "source";
		}
		URL url = new URL( requestURL.replaceAll("index.jsp", "source") );
		HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
		urlCon.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(urlCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
			response.append("\n");
		}
		in.close();
		urlCon.disconnect();
		System.out.println( response.toString() );
		
		req.setAttribute("sources", response.toString() );
		req.getRequestDispatcher("/WEB-INF/index.jsp").forward( req , resp );
	}
}

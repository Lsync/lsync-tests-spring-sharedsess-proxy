package net.molchat.proxy;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 
 * @author Valentine
 * @created 2011.06.04
 * @version 2012.10.29
 * 
 */


//============================================================================================== Shared sessions manager
@WebServlet("/*")
public final class SharedSessProxyServlet extends HttpServlet {

private static final long serialVersionUID = 5487335614546325815L;
private static final String DEFAULT_PATH = "/"; // Default root application path
private static final String ROOT_APP_NAME = "/"; // Default root application name


//============================================================================================================== service
@Override
public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	// Wrap a request
	HttpServletRequest wrappedReq = new AuthRequest(request);

	// Detect URL parts for forwarding
	String appName;
	StringBuilder path = new StringBuilder();

	String requestUri = request.getRequestURI(); // requestUri like /proxyApp/requestedApp/...

	// Divide URI to the app name and path
	int appNamePos = requestUri.indexOf('/', 2);
	int pathPos = requestUri.indexOf('/', appNamePos + 2);

	if (appNamePos > 0) {
		appName = requestUri.substring(appNamePos);
		if (pathPos > 0) {
			path.append(requestUri.substring(pathPos));
		} else {
			path.append(DEFAULT_PATH);
		}
	} else {
		appName = ROOT_APP_NAME;
		path.append(DEFAULT_PATH);
	}

	// Attach parameters to path (to get correct RequestDispatcher)
	String queryString = request.getQueryString();
	if (queryString != null && queryString.length() > 0) {
		path.append('?').append(queryString);
	}

	// Forward a request to an appropriate application
	forward(wrappedReq, response, appName, path.toString());

}


//============================================================================================================ doRequest
private void forward(HttpServletRequest req, HttpServletResponse res, String appName, String path) throws IOException {

	try {
		ServletContext scAuth = this.getServletContext().getContext(appName);
		if (scAuth != null) {
			RequestDispatcher dis = scAuth.getRequestDispatcher(path);
			if (dis != null) {
				dis.forward(req, res);
			} else {
				res.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} else {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	} catch (Exception e) {
		res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}
}


}

package net.molchat.proxy;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;


//============================================================================================ HttpServletRequestWrapper
/**
 * @author Valentin
 * 
 */
public final class AuthRequest extends HttpServletRequestWrapper {

private final HttpServletRequest upperReq; // Original request (before forwarding)


//========================================================================================================== Constructor
/**
 * @param request
 */
public AuthRequest(HttpServletRequest request) {

	super(request);
	// We should memorize the original request because of its context, it will be used for shared session creation
	upperReq = request;
}


//=========================================================================================================== getSession
@Override
public HttpSession getSession() {

	if (upperReq != null) {
		return upperReq.getSession();
	} else {
		return super.getSession();
	}
}


@Override
public HttpSession getSession(boolean f) {

	if (upperReq != null) {
		return upperReq.getSession(f);
	} else {
		return super.getSession(f);
	}
}


}

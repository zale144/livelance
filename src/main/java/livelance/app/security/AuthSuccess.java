package livelance.app.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import livelance.app.service.AccountService;

@Component
public class AuthSuccess extends SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired
	AccountService accountService;
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
    		HttpServletResponse response, Authentication authentication) 
    				throws IOException, ServletException {
    	String username = request.getParameter("username");
    	response.setStatus(HttpServletResponse.SC_OK);
    	response.setHeader("username", username);
    }
}

package livelance.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
	
	@Autowired
	private AccountAuthenticationProvider accountAuthenticationProvider;
		
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(accountAuthenticationProvider);
	}
	
	@Configuration
	public static class ApiWebSecurityConfigurerAdapter 
		extends WebSecurityConfigurerAdapter {
		
		@Autowired
	    private AuthFailure authFailure;

	    @Autowired
	    private AuthSuccess authSuccess;

	    @Autowired
	    private EntryPointUnauthorizedHandler unauthorizedHandler;
	    
	    @Autowired
	    private Environment environment;

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			// @formatter:off
			 http
             .csrf().disable()
             .authorizeRequests()
             	 .antMatchers("/management/**").hasRole("SYSADMIN")
                 .antMatchers("/**").permitAll()
    		     .anyRequest().authenticated()  
    					 .and()
	    		.exceptionHandling()
    	             .authenticationEntryPoint(unauthorizedHandler)
    	                 .and()
    	             .formLogin()
    	                 .successHandler(authSuccess)
    	                 .failureHandler(authFailure)
    	                 .and()
                 .logout().logoutSuccessUrl("/").permitAll();
			 
//			 http.portMapper()
//             	.http(Integer.parseInt(environment.getProperty("server.http.port"))) 
//             	.mapsTo(Integer.parseInt(environment.getProperty("server.port"))); 

			// @formatter: on
		}
	}
	
}

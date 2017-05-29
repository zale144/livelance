package livelance.app;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.FileSystemUtils;

import livelance.app.web.controller.FileUploadController;

@SpringBootApplication
public class LiveLanceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {

		SpringApplication.run(LiveLanceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init() {
		return (args) -> {
            FileSystemUtils.deleteRecursively(new File(FileUploadController.ROOT));

            Files.createDirectory(Paths.get(FileUploadController.ROOT));
		};
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	@Bean
//	  public EmbeddedServletContainerFactory servletContainer() {
//	    TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
//	        @Override
//	        protected void postProcessContext(Context context) {
//	          SecurityConstraint securityConstraint = new SecurityConstraint();
//	          securityConstraint.setUserConstraint("CONFIDENTIAL");
//	          SecurityCollection collection = new SecurityCollection();
//	          collection.addPattern("/*");
//	          securityConstraint.addCollection(collection);
//	          context.addConstraint(securityConstraint);
//	        }
//	      };
//	    
//	    tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
//	    return tomcat;
//	  }
//	  
//	  private Connector initiateHttpConnector() {
//	    Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//	    connector.setScheme("http");
//	    connector.setPort(8080);
//	    connector.setSecure(false);
//	    connector.setRedirectPort(443);
//	    
//	    return connector;
//	  }
}
 
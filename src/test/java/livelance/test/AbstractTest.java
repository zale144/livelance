package livelance.test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;
import livelance.app.HireMeApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HireMeApplication.class)
public abstract class AbstractTest extends TestCase {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
}

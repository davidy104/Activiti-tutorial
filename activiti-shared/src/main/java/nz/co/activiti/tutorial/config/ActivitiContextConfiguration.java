package nz.co.activiti.tutorial.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({ "classpath:activitiAppContext.xml" })
public class ActivitiContextConfiguration {

}

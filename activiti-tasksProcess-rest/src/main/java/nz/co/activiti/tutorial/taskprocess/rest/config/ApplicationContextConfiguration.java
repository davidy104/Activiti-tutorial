package nz.co.activiti.tutorial.taskprocess.rest.config;

import nz.co.activiti.tutorial.config.InfrastructureContextConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan({ "nz.co.activiti.tutorial.taskprocess.rest",
		"nz.co.activiti.tutorial.laptop", "nz.co.activiti.tutorial.rest" })
@Import({ InfrastructureContextConfiguration.class })
@ImportResource({ "classpath:activitiAppContext.xml" })
public class ApplicationContextConfiguration {

}

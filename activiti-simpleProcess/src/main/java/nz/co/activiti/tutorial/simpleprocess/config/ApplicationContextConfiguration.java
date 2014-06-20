package nz.co.activiti.tutorial.simpleprocess.config;

import nz.co.activiti.tutorial.config.ActivitiContextConfiguration;
import nz.co.activiti.tutorial.config.InfrastructureContextConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({ "nz.co.activiti.tutorial.simpleprocess",
		"nz.co.activiti.tutorial.ds" })
@Import({ ActivitiContextConfiguration.class,
		InfrastructureContextConfiguration.class })
public class ApplicationContextConfiguration {

}

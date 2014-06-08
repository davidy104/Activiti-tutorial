package nz.co.activiti.tutorial.traningprocess.config;

import nz.co.activiti.tutorial.config.ActivitiContextConfiguration;
import nz.co.activiti.tutorial.config.InfrastructureContextConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "nz.co.activiti.tutorial.traningprocess")
@Import({ ActivitiContextConfiguration.class,
		InfrastructureContextConfiguration.class })
public class ApplicationContextConfiguration {

}

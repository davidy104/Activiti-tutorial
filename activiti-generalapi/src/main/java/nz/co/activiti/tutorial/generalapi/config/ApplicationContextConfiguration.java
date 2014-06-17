package nz.co.activiti.tutorial.generalapi.config;

import nz.co.activiti.tutorial.config.InfrastructureContextConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan(basePackages = "nz.co.activiti.tutorial.generalapi")
@Import({ InfrastructureContextConfiguration.class })
@ImportResource({ "classpath:activitiAppContext.xml" })
public class ApplicationContextConfiguration {

}

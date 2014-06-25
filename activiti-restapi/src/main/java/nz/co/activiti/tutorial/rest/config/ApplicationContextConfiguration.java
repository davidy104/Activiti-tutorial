package nz.co.activiti.tutorial.rest.config;

import nz.co.activiti.tutorial.config.InfrastructureContextConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan(basePackages = "nz.co.activiti.tutorial.rest")
@Import({ InfrastructureContextConfiguration.class })
@ImportResource({ "classpath:activitiAppContext.xml" })
public class ApplicationContextConfiguration {

	// please check ActivitiContextConfig xml file
	// @Bean
	// public static PropertyPlaceholderConfigurer properties() {
	// PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
	// org.springframework.core.io.Resource[] resources = new
	// ClassPathResource[] { new ClassPathResource(
	// "activiti-auth.properties") };
	// ppc.setLocations(resources);
	// ppc.setIgnoreUnresolvablePlaceholders(true);
	// return ppc;
	// }

}

package nz.co.activiti.tutorial.rest.config;

import nz.co.activiti.tutorial.config.ActivitiContextConfiguration;
import nz.co.activiti.tutorial.config.InfrastructureContextConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "nz.co.activiti.tutorial.rest")
@Import({ ActivitiContextConfiguration.class,
		InfrastructureContextConfiguration.class })
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

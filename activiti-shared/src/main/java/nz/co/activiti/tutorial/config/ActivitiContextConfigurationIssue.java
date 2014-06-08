package nz.co.activiti.tutorial.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.deploy.Deployer;
import org.activiti.engine.impl.rules.RulesDeployer;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.PlatformTransactionManager;

//issue: groovy not work in spring context,alternative: change programming configuration to xml file
@Configuration
@PropertySource("classpath:activiti.properties")
public class ActivitiContextConfigurationIssue {

	@Resource
	private Environment environment;

	@Resource
	private DataSource dataSource;

	@Resource
	private PlatformTransactionManager transactionManager;

	private static final String PROPERTY_JOBEXECUTOR_ACTIVATOR = "activiti.jobExecutorActivate";
	private static final String PROPERTY_HISTORY = "activiti.history";
	private static final String PROPERTY_DATABASE_SCHEMA_UPDATE = "activiti.databaseSchemaUpdate";
	private static final String PROPERTY_DATABASE_TYPE = "activiti.databaseType";

	private static final String PROPERTY_MAIL_SERVER_HOST = "activiti.mailServerHost";
	private static final String PROPERTY_MAIL_SERVER_PORT = "activiti.mailServerPort";
	private static final String PROPERTY_MAIL_SERVER_USERNAME = "activiti.mailServerUsername";
	private static final String PROPERTY_MAIL_SERVER_PASSWORD = "activiti.mailServerPassword";
	private static final String PROPERTY_MAIL_SERVER_SSL = "activiti.mailServerUseSSL";

	@Bean
	public SpringProcessEngineConfiguration processEngineConfiguration() {
		SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
		processEngineConfiguration.setDataSource(dataSource);
		processEngineConfiguration.setTransactionManager(transactionManager);
		processEngineConfiguration.setJobExecutorActivate(Boolean
				.valueOf(environment
						.getRequiredProperty(PROPERTY_JOBEXECUTOR_ACTIVATOR)));
		processEngineConfiguration.setHistory(environment
				.getRequiredProperty(PROPERTY_HISTORY));
		processEngineConfiguration.setDatabaseSchemaUpdate(environment
				.getRequiredProperty(PROPERTY_DATABASE_SCHEMA_UPDATE));

		processEngineConfiguration.setDatabaseType(environment
				.getRequiredProperty(PROPERTY_DATABASE_TYPE));

		processEngineConfiguration.setMailServerHost(environment
				.getRequiredProperty(PROPERTY_MAIL_SERVER_HOST));

		processEngineConfiguration.setMailServerPort(Integer
				.valueOf(environment
						.getRequiredProperty(PROPERTY_MAIL_SERVER_PORT)));

		processEngineConfiguration.setMailServerUsername(environment
				.getRequiredProperty(PROPERTY_MAIL_SERVER_USERNAME));
		processEngineConfiguration.setMailServerPassword(environment
				.getRequiredProperty(PROPERTY_MAIL_SERVER_PASSWORD));
		processEngineConfiguration.setMailServerUseSSL(Boolean
				.valueOf(environment
						.getRequiredProperty(PROPERTY_MAIL_SERVER_SSL)));

		List<Deployer> customPostDeployers = new ArrayList<Deployer>();
		customPostDeployers.add(new RulesDeployer());
		processEngineConfiguration.setCustomPostDeployers(customPostDeployers);

		return processEngineConfiguration;
	}

	@Bean
	public ProcessEngine processEngine() throws Exception {
		ProcessEngine processEngine = null;
		ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
		processEngineFactoryBean
				.setProcessEngineConfiguration(processEngineConfiguration());
		processEngine = processEngineFactoryBean.getObject();
		return processEngine;
	}

	@Bean
	public RepositoryService repositoryService() throws Exception {
		RepositoryService repositoryService = processEngine()
				.getRepositoryService();
		return repositoryService;
	}

	@Bean
	public RuntimeService runtimeService() throws Exception {
		RuntimeService runtimeService = processEngine().getRuntimeService();
		return runtimeService;
	}

	@Bean
	public TaskService taskService() throws Exception {
		TaskService taskService = processEngine().getTaskService();
		return taskService;
	}

	@Bean
	public HistoryService historyService() throws Exception {
		HistoryService historyService = processEngine().getHistoryService();
		return historyService;
	}

	@Bean
	public ManagementService managementService() throws Exception {
		ManagementService managementService = processEngine()
				.getManagementService();
		return managementService;
	}

	@Bean
	public IdentityService identityService() throws Exception {
		IdentityService identityService = processEngine().getIdentityService();
		return identityService;
	}

	@Bean
	public FormService formService() throws Exception {
		FormService formService = processEngine().getFormService();
		return formService;
	}

}

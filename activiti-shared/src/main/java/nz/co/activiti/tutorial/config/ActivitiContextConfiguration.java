package nz.co.activiti.tutorial.config;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.utils.ActivitiFacade;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({ "classpath:activitiAppContext.xml" })
public class ActivitiContextConfiguration {
	@Resource
	private RuntimeService runtimeService;

	@Resource
	private TaskService taskService;

	@Resource
	private HistoryService historyService;

	@Resource
	private RepositoryService repositoryService;

	@Resource
	private ManagementService managementService;

	@Resource
	private IdentityService identityService;

	@Resource
	private FormService formService;

	@Bean
	public ActivitiFacade activitiFacade() {
		return ActivitiFacade
				.getBuilder(runtimeService, taskService, historyService,
						repositoryService, identityService, formService)
				.build();
	}

}

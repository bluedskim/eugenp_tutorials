package com.baeldung.processes;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.test.Deployment;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(FlowableSpringExtension.class)
@SpringBootTest
public class ArticleWorkflowIntegrationTest {
	static Logger logger = LoggerFactory.getLogger(ArticleWorkflowIntegrationTest.class);

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;

	@Test
	@Deployment(resources = { "processes/article-workflow.bpmn20.xml" })
	void articleApprovalTest() {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("author", "test@baeldung.com");
		variables.put("url", "http://baeldung.com/dummy");
		runtimeService.startProcessInstanceByKey("articleReview", variables);
		Task task = taskService.createTaskQuery().singleResult();
		logger.info("task.getName()={}", task.getName());
		assertEquals("Review the submitted tutorial", task.getName());
		variables.put("approved", true);
		logger.info("before complete={}", runtimeService.createProcessInstanceQuery().count());
		taskService.complete(task.getId(), variables);
		logger.info("after complete={}", runtimeService.createProcessInstanceQuery().count());
		assertEquals(0, runtimeService.createProcessInstanceQuery().count());
	}
}
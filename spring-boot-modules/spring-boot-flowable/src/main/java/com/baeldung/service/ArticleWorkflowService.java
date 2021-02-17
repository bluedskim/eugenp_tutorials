package com.baeldung.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baeldung.domain.Approval;
import com.baeldung.domain.Article;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleWorkflowService {
	static Logger logger = LoggerFactory.getLogger(ArticleWorkflowService.class);

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;

	@Transactional
	public void startProcess(Article article) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("author", article.getAuthor());
		variables.put("url", article.getUrl());
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("articleReview", variables);
		logger.info("processInstance={}", processInstance);
	}

	@Transactional
	public List<Article> getTasks(String assignee) {
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(assignee).list();

		List<Article> articles = tasks.stream().map(task -> {
			Map<String, Object> variables = taskService.getVariables(task.getId());
			return new Article(task.getId(), (String) variables.get("author"), (String) variables.get("url"));
		}).collect(Collectors.toList());
		return articles;
	}

	@Transactional
	public List<Article> getTasks() {
		List<Task> tasks = taskService.createTaskQuery().list();

		List<Article> articles = tasks.stream().map(task -> {
			Map<String, Object> variables = taskService.getVariables(task.getId());
			return new Article(task.getId(), (String) variables.get("author"), (String) variables.get("url"));
		}).collect(Collectors.toList());
		return articles;
	}

	@Transactional
	public void submitReview(Approval approval) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("approved", approval.isStatus());
		taskService.complete(approval.getId(), variables);
	}
}
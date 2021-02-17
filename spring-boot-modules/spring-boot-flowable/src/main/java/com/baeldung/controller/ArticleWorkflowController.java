package com.baeldung.controller;

import java.util.List;

import com.baeldung.domain.Approval;
import com.baeldung.domain.Article;
import com.baeldung.service.ArticleWorkflowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleWorkflowController {
	@Autowired
	private ArticleWorkflowService service;

	@PostMapping("/submit")
	public void submit(@RequestBody Article article) {
		service.startProcess(article);
	}

	@GetMapping("/tasks/{assignee}")
	public List<Article> getTasksOfAssignee(@PathVariable String assignee) {
		return service.getTasks(assignee);
	}

	@GetMapping("/tasks")
	public List<Article> getAllTasks() {
		return service.getTasks();
	}

	@PostMapping("/review")
	public void review(@RequestBody Approval approval) {
		service.submitReview(approval);
	}
}
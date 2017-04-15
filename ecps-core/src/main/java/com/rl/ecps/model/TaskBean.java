package com.rl.ecps.model;

import java.util.List;

import org.activiti.engine.task.Task;

public class TaskBean {

	private Task task;
	
	private EbOrder order;
	
	private String businessKey;
	
	/**
	 * 处理当前节点自动生成分支按钮
	 */
	private List<String> outcome;
	
	/**
	 * 可以区分当前节点的状态
	 */
	private String income;

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public EbOrder getOrder() {
		return order;
	}

	public void setOrder(EbOrder order) {
		this.order = order;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public List<String> getOutcome() {
		return outcome;
	}

	public void setOutcome(List<String> outcome) {
		this.outcome = outcome;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}
	
}

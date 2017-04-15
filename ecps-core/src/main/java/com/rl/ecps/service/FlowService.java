package com.rl.ecps.service;

import java.util.List;

import com.rl.ecps.model.TaskBean;

public interface FlowService {

	public void deploy();
	
	public String startFlow(Long orderId);
	
	public void completeTaskByPid(String processInstanceId,String outcome);

	public List<TaskBean> selectTaskByAssignee(String assignee);
	
}

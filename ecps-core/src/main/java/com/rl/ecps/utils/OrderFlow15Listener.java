package com.rl.ecps.utils;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class OrderFlow15Listener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5050319827029225296L;

	public void notify(DelegateTask delegateTask) {
		//获得任务的定义的ID也就是英文名
		String taskKey = delegateTask.getTaskDefinitionKey();
		delegateTask.setAssignee(taskKey+"er");
	}

}

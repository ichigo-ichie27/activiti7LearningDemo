package com.lc.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.HashMap;

public class TestAssigneeUel {

    /**
     * 部署流程定义
     */
    @Test
    public void deployProcessDefinition(){

        // 1.获取ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.加载本地流程定义，部署流程
        Deployment deployment = repositoryService.createDeployment()
                .name("出差申请流程1")
                .addClasspathResource("bpmn/evection-uel.bpmn")
                .addClasspathResource("bpmn/evection-uel.png")
                .deploy();
        System.out.println("流程部署的id："+deployment.getId());
        System.out.println("流程部署的名字："+deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        // 1.获取ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3.通过流程定义的key启动一个流程实例
        // 设置assignee的值，用来替换uel表达式
        HashMap<String, Object> assigneeMap = new HashMap<String, Object>();
        assigneeMap.put("assignee0","张三");
        assigneeMap.put("assignee1","李四");
        assigneeMap.put("assignee2","王五");
        ProcessInstance myEvection1 = runtimeService.startProcessInstanceByKey("myEvection1",assigneeMap);
        System.out.println("流程部署的id："+myEvection1.getDeploymentId());
        System.out.println("流程定义的key："+myEvection1.getProcessDefinitionKey());
        System.out.println("流程实例的id"+myEvection1.getProcessInstanceId());
        System.out.println("当前活动的id："+myEvection1.getActivityId());
    }

}

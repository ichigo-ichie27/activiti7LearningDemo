package com.lc.test;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class TestBpmn20 {

    /**
     * 部署流程示例
     */
    @Test
    public void deploymentProcessInstance(){
        // 1.创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.使用repositoryService进行流程的部署
        Deployment deploy = repositoryService.createDeployment()
                .name("测试activiti是否支持bpmn20.xml")
                .addClasspathResource("bpmn/test.bpmn20.xml")
                .deploy();
        System.out.println("流程部署id："+deploy.getId());
        System.out.println("流程部署名称："+deploy.getName());
    }

    /**
     * 启动流程示例
     */
    @Test
    public void startProcessInstance(){
        // 1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3.根据流程定义的key启动流程示例
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("testBpmn");

        System.out.println("流程定义id："+instance.getProcessDefinitionId());
        System.out.println("流程示例id："+instance.getProcessInstanceId());

    }

    /**
     * 完成个人任务
     */
    @Test
    public void completeTask(){
        // 1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 3.获取个人任务
        Task task = taskService.createTaskQuery().processDefinitionKey("testBpmn")
                .taskAssignee("yangbo").singleResult();
        System.out.println("任务id="+task.getId());
        System.out.println("任务负责人="+task.getAssignee());
        System.out.println("任务名称="+task.getName());
        // 4.通过任务id完成个人任务
        taskService.complete(task.getId());
    }

}

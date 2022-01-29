package com.lc.test;

import com.lc.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;

public class TestVariables {

    /**
     * 部署流程
     */
    @Test
    public void deployProcess(){

        // 1.获取ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.部署流程
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程-variables")
                .addClasspathResource("bpmn/evection-global.bpmn")
                .deploy();
        System.out.println("流程部署id："+deploy.getId());
        System.out.println("流程部署名字："+deploy.getName());
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
        // 3.启动流程实例并传入流程参数
        HashMap<String, Object> map = new HashMap<String,Object>();
        Evection evection = new Evection();
        evection.setDays(2d);
        map.put("evection",evection);
        map.put("assignee0","林工");
        map.put("assignee1","杨经理");
        map.put("assignee2","王总经理");
        map.put("assignee3","周财务");
        ProcessInstance myEvection2 = runtimeService.startProcessInstanceByKey("myEvection2", map);
        System.out.println("流程实例id："+myEvection2.getProcessInstanceId());
        System.out.println("流程部署id："+myEvection2.getDeploymentId());

    }

    /**
     * 完成个人任务
     */
    @Test
    public void completeTask(){
        // 1.获取ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 3.获取个人任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection2")
                .taskAssignee("杨经理")
                .singleResult();
        if(task != null){
            // 完成任务
            taskService.complete(task.getId());
            System.out.println(task.getId()+"任务已完成");
        }
    }
}

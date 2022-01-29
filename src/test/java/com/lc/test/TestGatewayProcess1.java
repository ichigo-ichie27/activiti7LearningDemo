package com.lc.test;
import com.lc.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;

/**
 * 测试有网关的流程
 */
public class TestGatewayProcess1 {

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
                .name("出差申请流程-包含网关")
                .addClasspathResource("bpmn/evection-gateway1.bpmn")
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
        // 3.通过流程定义的key启动流程实例并传入流程参数
        HashMap<String, Object> map = new HashMap<String,Object>();
        Evection evection = new Evection();
        evection.setDays(3d);
        map.put("evection",evection);
        runtimeService.startProcessInstanceByKey("myGatewayProcess",map);
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completeTask(){
       // 1.获取processEngine
       ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
       // 2.获取TaskService
       TaskService taskService = processEngine.getTaskService();
       // 3.创建任务查询对象
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myGatewayProcess")
                .taskAssignee("jackson")
                .singleResult();
        if(task != null){
            // 完成任务
            taskService.complete(task.getId());
            System.out.println(task.getId()+"完成了任务");
        }
    }
}

package com.lc.test;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试会签流程
 */
public class TestCountersign {

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcess(){
        // 1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.进行流程部署
        Deployment deploy = repositoryService.createDeployment()
                .name("测试会签")
                .addClasspathResource("bpmn/countersignDemo.bpmn20.xml")
                .deploy();
        System.out.println("流程部署id："+deploy.getId());
        System.out.println("流程部署名称："+deploy.getName());

    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        // 1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<String,Object>();
        variables.put("approverList", Arrays.asList("xiaoliu","xiaozhou","xiaowang"));
        // 3.根据流程定义的key启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("countersignDemo",variables);
        System.out.println("流程定义id："+processInstance.getProcessDefinitionId());
        System.out.println("流程示例id："+processInstance.getProcessInstanceId());
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completeTask(){
        // 1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RuntimeService
        TaskService taskService = processEngine.getTaskService();
        // 3.获取个人任务
        Task task = taskService.createTaskQuery().processDefinitionKey("countersignDemo")
                .taskAssignee("wangwu").singleResult();
        if(task != null){
            // 4.通过任务id完成个人任务
            taskService.complete(task.getId());
        }
    }

}

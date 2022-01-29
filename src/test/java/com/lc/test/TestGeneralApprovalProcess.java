package com.lc.test;

import com.lc.pojo.GeneralApproval;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestGeneralApprovalProcess {

    /**
     * 部署流程
     */
    @Test
    public void deployProcessDefinition(){

        // 1.获取ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.部署流程
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("bpmn/generalApproval.bpmn")
                .name("测试提交和审核流程111")
                .deploy();
        System.out.println("流程部署的名字："+deploy.getName());
        System.out.println("流程部署的id："+deploy.getId());
        System.out.println("流程部署的key："+deploy.getKey());

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
        Map<String, Object> map = new HashMap<String,Object>();
        GeneralApproval generalApproval = new GeneralApproval();
        generalApproval.setOperateType(0);
        map.put("approval",generalApproval);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("generalApproval", map);
        System.out.println("流程实例的id："+processInstance.getId());
        System.out.println("流程定义的名字："+processInstance.getProcessDefinitionName());
        System.out.println("流程定义的key："+processInstance.getProcessDefinitionKey());

    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask(){

        // 1.获取ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 3.创建任务查询对象
        Task task = taskService.createTaskQuery()
                .taskAssignee("leader")
                .processDefinitionKey("generalApproval")
                .processInstanceId("182501")
                .singleResult();
//        Map<String, Object> map = new HashMap<>();
//        GeneralApproval approval = new GeneralApproval();
//        approval.setJudgeType(0);
//        map.put("approval",approval);
        if(task != null){
            // 完成任务
            taskService.complete(task.getId());
            System.out.println("任务的名称："+task.getName());
            System.out.println("任务的负责人："+task.getAssignee());
            System.out.println("完成任务的id："+task.getId());
        }

    }
}

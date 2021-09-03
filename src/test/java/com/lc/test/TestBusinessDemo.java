package com.lc.test;

import com.sun.org.apache.xalan.internal.xslt.Process;
import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class TestBusinessDemo {

    /**
     * 添加业务key到activiti表
     */
    @Test
    public void testBusinessKey(){

        // 1.获取ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3.启动流程实例，并和businessKey关联
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection", "2021");
        System.out.println("业务key："+instance.getBusinessKey());
    }

    /**
     * 全部流程实例的挂起和激活
     */
    @Test
    public void suspendAllProcessInstance(){

        // 1.获取ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.创建流程定义查询对象,查询流程定义
        ProcessDefinition myEvection = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
        // 4.获取当前流程定义的状态（挂起或者激活）
        boolean suspended = myEvection.isSuspended();
        // 5.获取流程定义的id
        String definitionId = myEvection.getId();
        // 6.判断当前流程定义的状态，如果是挂起状态，改为激活，否则改为挂起（true为挂起）
        if(suspended){
            // 参数1：流程定义id 参数2：是否激活 参数3：激活时间
            repositoryService.activateProcessDefinitionById(
                    definitionId,true,null);
            System.out.println("流程定义id："+definitionId+"已激活");
        }else{
            // 参数1：流程定义id 参数2：是否挂起 参数3：挂起时间
            repositoryService.suspendProcessDefinitionById(definitionId,
                    true,null);
            System.out.println("流程定义id："+definitionId+"已挂起");
        }

    }

    /**
     * 单个流程实例的挂起和激活
     */
    @Test
    public void suspendSingleProcessInstance(){

        // 1.获取ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3.创建流程实例查询对象，查询流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("22501")
                .singleResult();
        // 4.获取流程实例id
        String processInstanceId = processInstance.getId();
        // 5.获取当前流程实例的状态（true为挂起 false为激活）
        boolean suspended = processInstance.isSuspended();
        // 6.判断当前流程实例的状态，如果是挂起状态，改为激活，否则改为挂起
        if(suspended){
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println("流程实例id："+processInstanceId+"已激活");
        }else{
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println("流程实例id："+processInstanceId+"已挂起");
        }
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completeTask(){
        // 1.创建流程引擎ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 3.根据任务id完成任务
        Task task = taskService.createTaskQuery()
                .processInstanceId("22501")
                .taskAssignee("zhangsan")
                .singleResult();
        System.out.println("流程实例id="+task.getProcessInstanceId());
        System.out.println("任务id="+task.getId());
        System.out.println("任务负责人="+task.getAssignee());
        System.out.println("任务名称="+task.getName());
        taskService.complete(task.getId());
    }
}

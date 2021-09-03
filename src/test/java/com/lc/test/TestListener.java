package com.lc.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.HashMap;

public class TestListener {

    /**
     * 部署流程示例
     */
    @Test
    public void testDeployment(){

        // 1.创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.使用service进行流程的部署，定义一个流程的名字，把bpmn和png部署到数据库
        Deployment deploy = repositoryService.createDeployment()
                .name("测试监听器指定assignee")
                .addClasspathResource("bpmn/listener.bpmn")
                .deploy();
        // 4.输出部署信息
        System.out.println("流程部署id="+deploy.getId());
        System.out.println("流程部署名字="+deploy.getName());
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
        ProcessInstance myEvection1 = runtimeService.startProcessInstanceByKey("listener");
    }
}

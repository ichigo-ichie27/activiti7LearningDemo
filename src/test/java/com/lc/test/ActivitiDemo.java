package com.lc.test;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ActivitiDemo {

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
                .name("出差申请流程")
                .addClasspathResource("bpmn/evection.bpmn")
                .addClasspathResource("bpmn/evection.png")
                .deploy();
        // 4.输出部署信息
        System.out.println("流程部署id="+deploy.getId());
        System.out.println("流程部署名字="+deploy.getName());
    }

    /**
     * 使用压缩包的方式进行流程部署
     */
    @Test
    public void testDeployProcessByZip(){
        // 1.获取ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.读取资源包文件，构造成inputStream
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("bpmn/bpmn.zip");
        // 4.使用inputStream构造ZipInputStream
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 5.使用压缩包的流进行流程的部署
        Deployment deploy = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println("流程部署id="+deploy.getId());
        System.out.println("流程部署名字="+deploy.getName());
    }

    /**
     * 启动流程示例
     * act_hi_actinst 流程实例执行历史
     * act_hi_identitylink 流程参与者的历史信息
     * act_hi_procinst 流程实例的历史信息
     * act_hi_taskinst 任务的历史信息
     * act_ru_execution 流程执行的信息
     * act_ru_identitylink 流程参与者信息
     * act_ru_task 任务信息
     */
    @Test
    public void testStartProcess(){

        // 1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3.根据流程定义的key启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection");
        // 4.输出信息
        System.out.println("流程定义id="+instance.getProcessDefinitionId());
        System.out.println("流程实例id="+instance.getId());
        System.out.println("当前活动的id="+instance.getActivityId());

    }

    /**
     * 获取某个用户待执行的任务
     */
    @Test
    public void getPersonalTaskList(){

        // 1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 3.根据流程key和任务的负责人查询任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("myEvection") // 流程key
                .taskAssignee("zhangsan") // 负责人
                .list();
        // 4.输出信息
        for (Task task:taskList) {
            System.out.println("流程实例id="+task.getProcessInstanceId());
            System.out.println("任务id="+task.getId());
            System.out.println("任务负责人="+task.getAssignee());
            System.out.println("任务名称="+task.getName());
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
                .processDefinitionKey("myEvection")
                .taskAssignee("zhou")
                .singleResult();
        System.out.println("流程实例id="+task.getProcessInstanceId());
        System.out.println("任务id="+task.getId());
        System.out.println("任务负责人="+task.getAssignee());
        System.out.println("任务名称="+task.getName());
        taskService.complete(task.getId());
    }

    /**
     * 查询流程定义的信息
     */
    @Test
    public void getProcessDefinition(){
        // 1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.创建processDefinitionQuery对象
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        // 4.查询当前所有的流程定义，返回流程定义信息的集合
        List<ProcessDefinition> definitionList = definitionQuery.processDefinitionKey("myLeave")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        for (ProcessDefinition processDefinition : definitionList) {
            System.out.println("流程定义id="+processDefinition.getId());
            System.out.println("流程定义名称="+processDefinition.getName());
            System.out.println("流程定义key="+processDefinition.getKey());
            System.out.println("流程定义的版本="+processDefinition.getVersion());
            System.out.println("流程部署id="+processDefinition.getDeploymentId());
        }

    }

    /**
     * 删除流程部署信息
     * act_re_deployment
     * act_ge_bytearray
     * act_re_procdef
     *
     * 注：当前的流程如果没有完成，使用流程部署id删除流程会报错；
     *      想要删除未完成的流程则需要使用特殊的方式，也就是级联删除
     */
    @Test
    public void testDeleteProcessDeployment(){
        // 1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.通过部署id删除流程部署信息
        String deploymentId = "2501";
        //repositoryService.deleteDeployment(deploymentId);
        // 级联删除（可用于删除未完成的流程）
        repositoryService.deleteDeployment(deploymentId,true);
    }

    /**
     * 流程资源下载
     * 方案1：使用activiti提供的api来下载流程资源，保存到文件目录
     * 方案2：自己写代码从数据库中下载文件，使用jdbc将blob类型、clob类型数据读取出来，保存到文件目录
     * 解决IO操作：引入commons-io.jar包
     */
    @Test
    public void downloadProcessResource() throws IOException {
        // 1.获取流程引擎ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.创建ProcessDefinitionQuery，查询流程定义信息
        ProcessDefinition myEvection = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
        // 4.通过流程定义信息，获取部署id
        String deploymentId = myEvection.getDeploymentId();
        // 5.通过RepositoryService,传递部署id，获取资源信息（png和bpmn）
        // 5.1、获取png图片的流
        // 从流程定义表中获取图片的目录和名字
        String pngName = myEvection.getDiagramResourceName();
        // 通过流程部署id和文件名字来获取图片的资源
        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, pngName);
        // 5.2、获取bpmn文件的流
        // 从流程定义表中获取bpmn的目录和名字
        String bpmnName = myEvection.getResourceName();
        // 通过流程部署id和文件名字来获取bpmn的资源
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, bpmnName);
        // 6.构造outputStream流
        File pngFile = new File("d:/evectionflow01.png");
        File bpmnFile = new File("d:/evectionflow01.bpmn");
        FileOutputStream pngOutputStream = new FileOutputStream(pngFile);
        FileOutputStream bpmnOutputStream = new FileOutputStream(bpmnFile);
        // 7.输入流、输出流的转换
        IOUtils.copy(pngInput,pngOutputStream);
        IOUtils.copy(bpmnInput,bpmnOutputStream);
        // 8.关闭流
        pngOutputStream.close();
        bpmnOutputStream.close();
        pngInput.close();
        bpmnInput.close();
    }

    /**
     * 查看历史信息
     */
    @Test
    public void testGetHistoryInfo(){
        // 1.获取ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取HistoryService
        HistoryService historyService = processEngine.getHistoryService();
        // 3.创建查询对象
        HistoricActivityInstanceQuery instanceQuery = historyService.createHistoricActivityInstanceQuery();
        // 4.构造查询条件
        List<HistoricActivityInstance> list = instanceQuery.processDefinitionId("myEvection:1:7504")
                .list();
        for (HistoricActivityInstance hi : list) {
            System.out.println("流程定义id="+hi.getProcessDefinitionId());
            System.out.println("流程实例id="+hi.getProcessInstanceId());
            System.out.println("活动名称="+hi.getActivityName());
            System.out.println("活动负责人="+hi.getAssignee());
            System.out.println("-----------------------------------------");
        }
    }

}

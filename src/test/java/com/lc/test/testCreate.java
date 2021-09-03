package com.lc.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

public class testCreate {

    @Test
    public void testCreateActivitiDbTable(){
        // getDefaultProcessEngine会默认从resource下读取名字为activiti.cfg.xml的文件
        // 当processEngine对象创建时，就会创建activiti的表到mysql
        // 默认方式创建流程引擎对象
        //ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 使用自定义方式
        // 配置文件的名字可以自定义，bean的名字也可以自定义
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.
                createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 获取流程引擎对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

    }
}

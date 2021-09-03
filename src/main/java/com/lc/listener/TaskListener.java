package com.lc.listener;

import org.activiti.engine.delegate.DelegateTask;

/**
 * 想要成为activiti的监听器，需要实现TaskListener接口
 */
public class TaskListener implements org.activiti.engine.delegate.TaskListener {

    /**
     * 指定负责人
     * @param delegateTask
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        // 判断当前的任务是创建申请并且是create事件
        if("创建申请".equals(delegateTask.getName()) &&
                "create".equals(delegateTask.getEventName())){
            delegateTask.setAssignee("LX");
        }

    }
}

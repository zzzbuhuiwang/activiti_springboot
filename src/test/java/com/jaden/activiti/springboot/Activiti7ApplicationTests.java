package com.jaden.activiti.springboot;

import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Activiti7 新特性 测试
 *      原有 API 的基础上再次进行封闭 （简化对工作流的操作）扩展出了
 *          ProcessRuntime 接口  （流程定义信息的操作）
 *          TaskRuntime 接口 （任务运行情况）
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Activiti7ApplicationTests {
    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private SecurityUtil securityUtil;

    //流程定义查询
    @Test
    public void contextLoads() {
        securityUtil.logInAs("salaboy");
        //分页查询流程定义信息
        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
        System.out.println("可用的流程定义数量：" + processDefinitionPage.getTotalItems());
        for (ProcessDefinition pd : processDefinitionPage.getContent()) {
            System.out.println("流程定义：" + pd);
        }
    }

    //启动流程实例
    @Test
    public void testStartProcess() {
        securityUtil.logInAs("ryandawsonuk");
        ProcessInstance pi = processRuntime.start(ProcessPayloadBuilder.start().withProcessDefinitionKey("testSpringboot")
                .build());
        System.out.println("流程实例ID：" + pi.getId());
    }

    //查询任务、拾取任务、完成任务
    @Test
    public void testTask() {
        securityUtil.logInAs("erdemedeiros");
        Page<Task> taskPage=taskRuntime.tasks(Pageable.of(0,10));
        if (taskPage.getTotalItems()>0){
            for (Task task:taskPage.getContent()){
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
                System.out.println("任务："+task);
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
            }
        }
        Page<Task> taskPage2=taskRuntime.tasks(Pageable.of(0,10));
        if (taskPage2.getTotalItems()>0){
            System.out.println("任务："+taskPage2.getContent());
        }
    }
}

package com.jaden.activiti.springboot.controller;

import com.jaden.activiti.springboot.SecurityUtil;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  Activiti7+ SpringBoot+SpringMVC
 *      首先 用测试类 Activiti7ApplicationTests 启动流程实例
 *      启动 SpringBoot 启动类 Activiti7Application
 *      网页访问  http://127.0.0.1:8080/helloActiviti7  键入用户名密码  查看输出
 */
@RestController
public class Activiti7Controller {
    private Logger logger = LoggerFactory.getLogger(Activiti7Controller.class);

    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/helloActiviti7")
    public void helloActiviti7() {

        //当前项目中 只有 testSpringboot.bpmn 一个流程，且该流程在设计时，已分配给activitiTeam 组
        //可查询 security上下文 获取用户组下成员信息作为登陆信息

        //分页查询当前用户任务
        Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 10));

        if (taskPage.getTotalItems() > 0) {
            for (Task task : taskPage.getContent()) {
                logger.info("任务信息1：" + task);
                //注意，完成任务前必须先拾取任务
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
                //完成任务
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build()
                );
            }
        }
        //上一轮任务完成，再看下一个节点任务信息
        Page<Task> taskPage2 = taskRuntime.tasks(Pageable.of(0, 10));
        if (taskPage2.getTotalItems() > 0) {
            logger.info("任务信息2：" + taskPage2.getContent());
        }

    }
}

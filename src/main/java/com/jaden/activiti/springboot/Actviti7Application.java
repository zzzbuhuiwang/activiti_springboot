package com.jaden.activiti.springboot;

import org.activiti.api.process.runtime.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Activiti7 整合 Springboot
 *      Activiti7 与 SpringSecurity 的强耦合 ，必须引入 SpringSecurity
 *      resources/processes/*.bpmn 流程定义文件会自动部署
 *      创建activiti数据库下的表缺失 act_hi_*
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Actviti7Application {
    private Logger logger = LoggerFactory.getLogger(Actviti7Application.class);



    public static void main(String[] args) {
        SpringApplication.run(Actviti7Application.class, args);
    }

    @Bean
    public Connector testConnector() {
        return integrationContext -> {
            logger.info("以前叫代理，现在叫连接器被调用啦~~");
            return integrationContext;
        };
    }
}

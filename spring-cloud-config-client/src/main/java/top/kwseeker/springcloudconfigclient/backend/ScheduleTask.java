package top.kwseeker.springcloudconfigclient.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ScheduleTask {

    @Autowired
    private ContextRefresher contextRefresher;
    @Autowired
    private Environment environment;

    @Scheduled(fixedRate = 10*1000, initialDelay = 3*1000)
    public void autoRefresh() {
        System.out.println("refresh property");
        Set<String> updatedPropertyNames = contextRefresher.refresh();
        updatedPropertyNames.forEach(propertyName -> System.out.printf("[Thread: :%s] 当前配置已经更新，" +
                        "具体 key： %s, value: %s\n",
                Thread.currentThread().getName(), propertyName, environment.getProperty(propertyName)));
        if(!updatedPropertyNames.isEmpty()) {
            System.out.printf("[Thread]: %s 当前配置已更新，具体项目:%s \n",
                    Thread.currentThread().getName(), updatedPropertyNames);
        }
    }
}

package top.kwseeker.springcloudconfignacosserver.service.basic;

import com.alibaba.nacos.api.config.listener.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.alibaba.nacos.NacosConfigProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * ApplicationRunner
 * Interface used to indicate that a bean should <em>run</em> when it is contained within
 * a {@link SpringApplication}.
 */
//基于事件机制自动刷新配置
@Component
public class SampleRunner implements ApplicationRunner {

    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Value("${user.name}")
    String username;
    @Value("${user.age:25}")
    int userAge;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.printf("Initial username=%s, userAge=%d", username, userAge);

        nacosConfigProperties.configServiceInstance().addListener(
                "nacos-config-example.properties",
                "DEFAULT_GROUP",
                new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return null;
                    }

                    @Override
                    public void receiveConfigInfo(String configInfo) {
                        Properties properties = new Properties();
                        try {
                            properties.load(new StringReader(configInfo));
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("config changed: " + properties);
                    }
                }
        );
    }

}

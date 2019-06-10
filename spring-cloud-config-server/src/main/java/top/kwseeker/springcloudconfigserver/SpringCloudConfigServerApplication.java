package top.kwseeker.springcloudconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableConfigServer
public class SpringCloudConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigServerApplication.class, args);
    }

    //实现自定义的配置仓库，如果不指定的化，默认是基于Git仓库
    //在实现自定义的配置仓库之前，看下默认的配置仓库的实现以及使用
    //@Bean
    //public EnvironmentRepository environmentRepository() {
    //    return (String application, String profile, String label) -> {
    //        Environment environment = new Environment("default", profile);
    //        List<PropertySource> propertySources = environment.getPropertySources();
    //        Map<String, Object> source = new HashMap<>();
    //        source.put("name", "小马哥");
    //        PropertySource propertySource = new PropertySource("map", source);
    //        // 追加 PropertySource
    //        propertySources.add(propertySource);
    //        return environment;
    //    };
    //}
}

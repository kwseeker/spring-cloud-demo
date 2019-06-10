package top.kwseeker.springcloudnativeapplication;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

//@SpringBootApplication
@EnableAutoConfiguration
public class SpringCloudNativeApplication {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setId("newApplicationContext");          //设置此应用上下文的名称
        //使用这个应用上下文加载Bean
        //context.registerBean("greetBean", GreetBean.class, new GreetBean());  //TODO:为何失败？
        context.registerBean("helloWorld", String.class, "Hello world");
        context.refresh();
        new SpringApplicationBuilder(SpringCloudNativeApplication.class)
                .parent(context)   //显示地设置双亲上下文
                .run(args);
        //应用上下文执行顺序
        //bootstrap ApplicationContext -> newApplicationContext -> application-1 ApplicationContext

        //SpringApplication.run(SpringCloudNativeApplication.class, args);
    }

    //这个类必需符合Bean的编码规范
    public static class GreetBean {

        public GreetBean() {}

        public String sayHello() {
            return "Hello Arvin";
        }
    }
}

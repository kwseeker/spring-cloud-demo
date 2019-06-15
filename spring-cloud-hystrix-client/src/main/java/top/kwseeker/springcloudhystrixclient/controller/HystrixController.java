package top.kwseeker.springcloudhystrixclient.controller;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Observer;
import top.kwseeker.springcloudhystrixclient.service.GreetService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.*;

@RestController
public class HystrixController {

    private static final Random random = new Random();
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    private GreetService greetService;
    @Autowired
    private HttpServletResponse response;

    @GetMapping("/hi")
    @com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand(
            fallbackMethod = "errorContent",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100"), //超时阻断时间
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")                //采用处理策略，当接口访问量很高时应该使用信号量的方式
            }
    )
    public String hi() throws Exception {
        int value = random.nextInt(200);
        System.out.println("hi request costs " + value + " ms.");
        Thread.sleep(value);
        return "Hi";
    }

    @GetMapping("/hello")
    public String helloWithHystrix() {
        return new HelloCommand().execute();
    }

    private class HelloCommand extends HystrixCommand<String> {
        protected HelloCommand() {
            super(HystrixCommandGroupKey.Factory.asKey("hello"),
                    100);   //超时时间
        }

        @Override
        protected String run() throws Exception {
            int value = random.nextInt(200);
            System.out.println("hello request cost " + value + " ms.");
            Thread.sleep(value);
            return "Hello";
        }

        protected String getFallback() {
            return HystrixController.this.errorContent();
        }
    }

    private String errorContent() {
        return "Fault";
    }

    //使用Future的方式实现超时熔断
    @GetMapping("/hey")
    public String hey() {
        int value = random.nextInt(200);
        Future<String> future = executorService.submit(() -> {
            System.out.println("hey request cost " + value + "ms");
            Thread.sleep(value);
            return "hey";
        });
        try {
            return future.get(100, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.out.println("hey request timeout");
            return "Fault";
        }
    }

    //使用RxJava实现超时熔断
    @GetMapping("/goodmorning")
    public void goodMorning() {
        long requestTime = System.currentTimeMillis();
        GreetObserver observer = new GreetObserver(response, requestTime);
        greetService.goodMorning(observer);
    }

    public static class GreetObserver implements Observer<String> {

        private HttpServletResponse response;
        private long requestTime;

        public GreetObserver(HttpServletResponse response, long requestTime) {
            this.response = response;
            this.requestTime = requestTime;
        }

        @Override
        public void onCompleted() {
            System.out.println("执行结束");
        }

        @Override
        public void onError(Throwable e) {
            System.out.println("Fault");
            try {
                OutputStream outputStream = response.getOutputStream();
                outputStream.write("Fault".getBytes(StandardCharsets.UTF_8));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        //TODO：这里感觉用错了，onNext是具体的业务处理逻辑？还是业务处理完成之后调用处理结果？待深入分析RxJava的工作流程
        @Override
        public void onNext(String s) {      //数据消费
            long currentTime = System.currentTimeMillis();
            long costTime = currentTime - requestTime;
            System.out.println("goodMorning request cost " + costTime + "ms");
            if(costTime > 100) {
                throw new RuntimeException("Timeout");  //onError
            }
            try {
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(s.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

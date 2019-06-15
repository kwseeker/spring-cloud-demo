package top.kwseeker.springcloudhystrixclient.service;

import org.springframework.stereotype.Service;
import rx.Observer;
import rx.Single;
import rx.schedulers.Schedulers;

import java.util.Random;

@Service
public class GreetService {

    public void goodMorning(Observer<String> observer) {
        try {
            Thread.sleep(new Random().nextInt(200));    //模拟业务逻辑的延迟
            Single.just("good morning")                         //发布数据              //TODO:正常应该是先订阅再发布数据的吧
                    .subscribeOn(Schedulers.immediate())        //订阅此数据的线程池
                    .subscribe(observer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

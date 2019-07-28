package top.kwseeker.webgreetserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kwseeker.webgreetserver.domain.DailyNews;

@RestController
public class GreetController {

    @GetMapping("/greet/dailynews")
    public DailyNews pullDailyNews() {
        DailyNews dailyNews = new DailyNews();
        dailyNews.setContentUrl("http://www.baidu.com");
        dailyNews.setTopic("每日早报");
        dailyNews.setPicUrl("http://www.xxx.com");
        return dailyNews;
    }
}

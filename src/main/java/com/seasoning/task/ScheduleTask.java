package com.seasoning.task;

import com.seasoning.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class ScheduleTask {

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0 1 0 * * ?")
    private void stepStatistics() {

        log.info("statistics step data begin!");

        orderService.updateDay();
        log.info("statistics step data end!");
    }
}

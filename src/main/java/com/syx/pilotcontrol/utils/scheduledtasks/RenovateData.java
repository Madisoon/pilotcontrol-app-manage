package com.syx.pilotcontrol.utils.scheduledtasks;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by Msater Zg on 2017/5/10.
 * spring 定时器任务
 */
public class RenovateData {
    @Scheduled(cron = "0 0 24 * * ?")
    /*@Scheduled(fixedRate = 1000 * 60 * 10)*/
    public void getDataUser() {
    }

}

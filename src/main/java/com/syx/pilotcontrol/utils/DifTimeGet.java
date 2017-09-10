package com.syx.pilotcontrol.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Msater Zg on 2017/2/17.
 */
@Component
public class DifTimeGet {
    public String getWeekTime(Date dt) {
        String[] weekDays = {"day_seven", "day_one", "day_two", "day_three", "day_four", "day_five", "day_six"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    public boolean judgeTimeInterval(String timeInterval, Date nowTime) {
        boolean flag = false;
        String[] times = timeInterval.split("-");
        if (timeInterval == "" || times[0].equals(times[1])) {
            flag = false;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String nowTimeHM = sdf.format(nowTime);
            int startResult = nowTimeHM.compareTo(times[0]);
            int endResult = nowTimeHM.compareTo(times[1]);
            if (startResult > 0 && endResult < 0) {
                flag = true;//立刻发
            } else {
                flag = false;//过一段时间再发
            }
        }
        return flag;
    }
}

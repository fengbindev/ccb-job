package com.ccb.job.admin.core.route.strategy;


import com.ccb.job.admin.core.route.ExecutorRouter;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteRound extends ExecutorRouter {

    private static  ConcurrentHashMap<String, Integer> routeCountEachJob = new ConcurrentHashMap<String, Integer>();
    private static  long CACHE_VALID_TIME = 0;
    private static  int count(String jobId) {
        // cache clear
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            routeCountEachJob.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 1000*60*60*24;
        }

        // count++
        Integer count = routeCountEachJob.get(jobId);
        count = (count==null || count>1000000)?(new Random().nextInt(100)):++count;  // 初始化时主动Random一次，缓解首次压力
        routeCountEachJob.put(jobId, count);
        return count;
    }

    @Override
    public String route(String jobId, ArrayList<String> addressList) {
        return addressList.get(count(jobId)%addressList.size());
    }

}

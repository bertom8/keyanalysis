package org.keyanalysis.Services;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

public class SchedulerService extends TimerTask implements Runnable {
	
	public static void getTweetsEveryDay() {
		long time = new Date().getTime() - 86400000;
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.get(Calendar.DAY_OF_WEEK);
		
		for (long i = 0; i < 16; i++) {
			//org.keyanalysis.Services.TweetSearcher.searchEngTweets(time);
			//org.keyanalysis.Services.TweetSearcher.searchHunTweets(time);
			time += 5400000;
		}
	}

	@Override
	public void run() {
		getTweetsEveryDay();
		
	}
}

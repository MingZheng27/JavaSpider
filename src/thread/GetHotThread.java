package thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import getdata.GetTopicId;

public class GetHotThread implements Runnable{
	
	public int child_topic_id;
	
	public GetHotThread(int child_topic_id) {
		this.child_topic_id = child_topic_id;
	}

	@Override
	public void run() {
		Lock lock = new ReentrantLock();
		try{
			lock.lock();
			GetTopicId.loadOneTopicHotQA(child_topic_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}

}

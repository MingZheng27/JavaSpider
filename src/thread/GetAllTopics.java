package thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.mysql.jdbc.Connection;

import getdata.GetTopicId;

public class GetAllTopics implements Runnable{
	
	public int main_topic_id;
	public Connection conn;
	
	public GetAllTopics(int main_topic_id,Connection conn) {
		this.main_topic_id = main_topic_id;
		this.conn = conn;
	}
	
	//getChildTopics(main_topic_id,conn);
	@Override
	public void run() {
		Lock lock = new ReentrantLock();
		try{
			lock.lock();
			GetTopicId.getChildTopics(main_topic_id, conn);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}

}

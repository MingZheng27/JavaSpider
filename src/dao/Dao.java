package dao;

import java.util.List;

import bean.HotQA;

public interface Dao {
	
	void save(HotQA qa,int child_topic_id);
	
	HotQA get(int answer_id);
	
	//ͨ��child_topic_id����ѯһ���ӻ������������
	List<HotQA> list(int child_topic_id);

}

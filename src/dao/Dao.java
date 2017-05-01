package dao;

import java.util.List;

import bean.HotQA;

public interface Dao {
	
	public abstract void save(HotQA qa,int child_topic_id);
	
	public abstract HotQA get(int answer_id);
	
	//通过child_topic_id来查询一个子话题的热门内容
	public abstract List<HotQA> list(int child_topic_id);

}

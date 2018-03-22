package daoimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import bean.HotQA;
import dao.Dao;
import util.JDBCUtil;

public class DaoImpl implements Dao{

	Connection conn = JDBCUtil.getConn();

	@Override
	public void save(HotQA qa,int child_topic_id) {
		String sql = "insert into question_and_answer values(null,?,?,?,?,?,?,?)";
		PreparedStatement state = null;
		try {
			state = (PreparedStatement) conn.prepareStatement(sql);
			state.setInt(1, child_topic_id);
			state.setInt(2, qa.getQuestion_id());
			state.setString(3, qa.getQuestion_name());
			state.setInt(4, qa.getAnswer_id());
			state.setString(5, qa.getUsername());
			state.setString(6, qa.getExcerpt());
			state.setBoolean(7, qa.isArticle());
			state.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public HotQA get(int answer_id) {
		String sql = "select * from question_and_answer where answer_id = " + answer_id;
		try {
			PreparedStatement state = (PreparedStatement) conn.prepareStatement(sql);
			ResultSet rs = state.executeQuery();
			HotQA qa = null;
			if (rs.next()){
				qa = new HotQA(rs.getInt("child_topic_id"),rs.getInt("question_id"),rs.getString("question_name"),
						rs.getInt("answer_id"),rs.getString("username"),rs.getString("content"),rs.getBoolean("is_article"));
				qa.setId(rs.getInt("id"));
			}
			return qa;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<HotQA> list(int child_topic_id) {
		List<HotQA> list = new ArrayList<HotQA>();
		String sql = "select * from question_and_answer where child_topic_id = " 
					+ child_topic_id + " order by id desc limit 0,5";
		try {
			PreparedStatement state = (PreparedStatement) conn.prepareStatement(sql);
			ResultSet rs = state.executeQuery();
			while (rs.next()){
				HotQA qa = new HotQA(rs.getInt("child_topic_id"),rs.getInt("question_id"),rs.getString("question_name"),
						rs.getInt("answer_id"),rs.getString("username"),rs.getString("content"),rs.getBoolean("is_article"));
				list.add(qa);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}

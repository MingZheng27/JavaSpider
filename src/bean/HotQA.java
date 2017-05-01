package bean;

public class HotQA {
	
	private int id;
	private int child_topic_id;
	private int question_id;
	private String question_name;
	private int answer_id;
	private String username;
	
	public HotQA() {
		// TODO Auto-generated constructor stub
	}
	
	public HotQA(int child_topic_id, int question_id, String question_name, int answer_id, String username) {
		super();
		this.child_topic_id = child_topic_id;
		this.question_id = question_id;
		this.question_name = question_name;
		this.answer_id = answer_id;
		this.username = username;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getChild_topic_id() {
		return child_topic_id;
	}

	public void setChild_topic_id(int child_topic_id) {
		this.child_topic_id = child_topic_id;
	}

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public String getQuestion_name() {
		return question_name;
	}

	public void setQuestion_name(String question_name) {
		this.question_name = question_name;
	}

	public int getAnswer_id() {
		return answer_id;
	}

	public void setAnswer_id(int answer_id) {
		this.answer_id = answer_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}

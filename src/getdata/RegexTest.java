package getdata;

import java.util.List;


import bean.HotQA;
import daoimpl.DaoImpl;
/**
 * 测试类，仅仅用于测试
 * @author dell
 *
 */
public class RegexTest {
	
	public static void main(String[] args) {
		DaoImpl daoimpl = new DaoImpl();
		List<HotQA> list = daoimpl.list(19552706);
		for (int i = 0 ;i < list.size();i++){
			System.out.println(list.get(i).getQuestion_name());
		}
		//String s = "<h2><a class=\"question_link\" href=\"/question/59172844\" target=\"_blank\" data-id=\"15891523\" data-za-element-name=\"Title\">";
		//System.out.println(s.length());
//		String regx = "href=\\\"/topic/[0-9]+?\\\"";
//		Pattern p1 = Pattern.compile(regx);
//		String target = "<div class=\"item\"><div class=\"blk\">\n<a target=\"_blank\" href=\"/topic/19550994\">\n<img src=\"https://pic4.zhimg.com/fb89e0ea3_xs.jpg\" alt=\"游戏\">\n<strong>游戏</strong>\n</a>\n<p>游戏 是一种在特定时间、空间范围内遵循某种特定规则的，追求精神…</p>\n\n<a id=\"t::-253\" href=\"javascript:;\" class=\"follow meta-item zg-follow\"><i class=\"z-icon-follow\"></i>关注</a>\n\n</div></div>";
//		Matcher m = p1.matcher(target);
//		while (m.find()){
//			System.out.println(m.group());
//		}
		
//		Connection conn = JDBCUtil.getConn();
		//DaoImpl daoimpl = new DaoImpl();
		//daoimpl.save(new HotQA(234,1,"test",3,"test"),234);
		try {
//			String sql = "insert into main_topic values(?,?,?)";
//			PreparedStatement state = (PreparedStatement) conn.prepareStatement(sql);
//			state.setInt(1, 1);
//			state.setInt(2, 2);
//			state.setString(3, "test");
//			state.execute();
//			String sql1 = "select count(*) from main_topic";
//			PreparedStatement state = (PreparedStatement) conn.prepareStatement(sql1);
//			ResultSet rs = state.executeQuery();
//			while (rs.next()){
//				System.out.println(rs.getString("count(*)"));
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
}

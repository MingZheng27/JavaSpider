package getdata;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import bean.HotQA;
import daoimpl.DaoImpl;
import thread.GetAllTopics;
import thread.GetHotThread;
import util.JDBCUtil;
import util.Utils;

public class GetTopicId {

    public static Set<Integer> child_topic_set = new HashSet<Integer>();

    public static void main(String[] args) {
        try {
            //��ȡ������
            //getTopicId();
            //��ȡ�������µķֻ�������
            //getAllChildTopics();
            //��ȡÿ�������µ����Ŵ�
            //loadAllTopicHotQA();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //��ȡ������
    public static void getTopicId() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Connection connection;
                //id����
                int id = 1;
                //���̵߳��쳣û�취�׳������̲߳��񣬱���Ҫ�����̲߳���
                try {
                    URL url = new URL("https://www.zhihu.com/topics");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    BufferedReader bfr = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String line = null;
                    StringBuilder sb = new StringBuilder();
                    while ((line = (bfr.readLine())) != null) {
                        sb.append(line);
                    }
                    String result = sb.toString();
                    String regex = "data-id=\"[0-9]{0,6}\"";
                    Pattern pattern = Pattern.compile(regex);
                    //����matcher
                    Matcher m = pattern.matcher(result);
                    String regx = "href=\"#.*?\"";
                    Pattern p = Pattern.compile(regx);
                    Matcher mn = p.matcher(result);
                    //ͨ��m.find()������,�ҵ�һ������true,�Ҳ����ͷ���false,
                    //m.group()Ҫ��find()��ϲ����ҵ������
                    while (m.find() && mn.find()) {
                        String s = m.group();
                        //data-id="19800"><a href="#����">����</a>
                        s = s.substring(9, s.length() - 1);
                        String sn = mn.group();
                        sn = sn.substring(7, sn.length() - 1);
                        System.out.println(s + " " + sn);
                        connection = JDBCUtil.getConn();
                        PreparedStatement state = (PreparedStatement) connection.prepareStatement("insert into main_topic values(?,?,?)");
                        state.setInt(1, id++);
                        state.setInt(2, Integer.valueOf(s));
                        state.setString(3, sn);
                        state.execute();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //��ȡ�����ӻ���
    public static void getAllChildTopics() throws Exception {
        List<Integer> list = new ArrayList<Integer>();
        ExecutorService exec = Executors.newFixedThreadPool(2);
        Connection conn = JDBCUtil.getConn();
        String sql = "select main_topic_id from main_topic";
        PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(rs.getInt(1));
        }
        //ͨ���̳߳ؿ������̻߳�ȡ����
        for (int i = 0; i < list.size(); i++) {
            int main_topic_id = list.get(i);
            exec.execute(new GetAllTopics(main_topic_id, conn));
        }
        //���������̳߳�������߳�,�ȳ����е��̴߳�����ͻ�shutdown
        exec.shutdown();

    }

    //ͨ��һ���������������ӻ���
    public static void getChildTopics(int topic_id, Connection conn) throws Exception {
        int offset = 0;
        //		HttpClient httpClient = new DefaultHttpClient();
        //��ʹ�����ӳؼ�������
        //CloseableHttpClient client = HttpClients.createDefault();
        while (true) {
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setRetryHandler(new DefaultHttpRequestRetryHandler())
                    .setConnectionManager(cm)
                    .build();
            HttpPost req = new HttpPost("https://www.zhihu.com/node/TopicsPlazzaListV2");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("method", "next"));
            params.add(new BasicNameValuePair("params", "{\"topic_id\":" + topic_id + ",\"offset\":" + offset + ",\"hash_id\":\"37492588249aa9b50ee49d1797e9af81\"}"));
            req.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
            HttpResponse resp = httpClient.execute(req);
            String sb = EntityUtils.toString(resp.getEntity());
            if (sb.length() < 25) break;
            //<strong>������Ӿ������� (WCG)</strong>
            //.�ǵ��������ַ�*��������,ͨ��?��ȡ��̰��ģʽ������̰��ģʽ��ֱ��ƥ�䵽���һ��
            //href=\"/topic/19550994\"
            String regex = "<strong>.*?<\\\\/strong>";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(sb);
            String regx = "href=\\\\\"\\\\/topic\\\\/[0-9]+\\\\\"";
            Pattern p1 = Pattern.compile(regx);
            Matcher m1 = p1.matcher(sb);
            while (m.find() && m1.find()) {
                //m.group�õ��Ժ�תΪ�ֽ�������תΪString
                String temp = m.group().substring(8, m.group().length() - 10);
                //System.out.println(temp);
                String sql = "insert into child_topic values(null,?,?,?)";
                PreparedStatement state = (PreparedStatement) conn.prepareStatement(sql);
                state.setInt(1, topic_id);
                state.setString(2, m1.group().substring(16, m1.group().length() - 2));
                state.setString(3, Utils.decodeUnicode(temp));
                state.execute();
            }
            offset += 20;
        }
    }

    //��ȡ���������ʴ�
    public static void loadAllTopicHotQA() throws Exception {
        ExecutorService exec = Executors.newFixedThreadPool(2);
        Connection conn = JDBCUtil.getConn();
        String sql = "select child_topic_id from child_topic";
        PreparedStatement state = (PreparedStatement) conn.prepareStatement(sql);
        ResultSet rs = state.executeQuery();
        while (rs.next()) {
            //����setȥ��
            synchronized (child_topic_set) {
                //��ס��Ϊ�˷�ֹ���߳�ʱͬʱ�ж��ظ�����if�����
                Integer temp = rs.getInt(1);
                if (!child_topic_set.contains(temp)) {
                    child_topic_set.add(temp);
                    exec.execute(new GetHotThread(temp));
                }
            }
        }
        exec.shutdown();
        child_topic_set.clear();
    }

    //��ȡ���������µ������ʴ�
    public static void loadOneTopicHotQA(int child_topic_id) throws Exception {
        LoadDetailInformationTest loadDetailInformation = new LoadDetailInformationTest();
        List<HotQA> resultList = loadDetailInformation.loadNHotQA(5, String.valueOf(child_topic_id));
        DaoImpl daoimpl = new DaoImpl();
        for (HotQA qa : resultList) {
            daoimpl.save(qa, child_topic_id);
        }
    }
}

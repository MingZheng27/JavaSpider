package getdata;

import bean.DetailInformation;
import bean.HotQA;
import com.mysql.jdbc.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoadDetailInformationTest {

    //https://www.zhihu.com/api/v4/topics/19553732/feeds/top_activity
    private static final String LIMIT_AND_AFTER_ID_REGEX = "limit=\\d+&after_id=.*?\"";
    private static final String QUESTION_ID_REGEX = "type\": \"(question|article)\", \"id\": \\d*";
    private static final String QUESTION_NAME_REGEX = "\"title\": \".*?\"";
    private static final String ANSWER_ID_REGEX = "answers/\\d+\"";
    private static final String USERNAME_REGEX = "\"name\": \".*?\"";
    private static final String EXCERPT_REGEX = "\"excerpt\": \".*?\",";

    private Pattern p = Pattern.compile(LIMIT_AND_AFTER_ID_REGEX);
    private Pattern questionIdPattern = Pattern.compile(QUESTION_ID_REGEX);
    private Pattern questionNamePattern = Pattern.compile(QUESTION_NAME_REGEX);
    private Pattern answerIdPattern = Pattern.compile(ANSWER_ID_REGEX);
    private Pattern usernamePattern = Pattern.compile(USERNAME_REGEX);
    private Pattern excerptPattern = Pattern.compile(EXCERPT_REGEX);

    /**
     *
     * @param n
     * @return list contains n * 5 HotQA Object
     */
    public List<HotQA> loadNHotQA(int n,String childTopicId) throws IOException {
        DetailInformation detailInformation = new DetailInformation();
        List<HotQA> list = new ArrayList<>();
        for (int i = 0 ;i < n ;i++) {
            detailInformation = loadDetailInformation(childTopicId,
                    detailInformation.getLimit(), detailInformation.getAfterId());
            Matcher questionIdMatcher = questionIdPattern.matcher(detailInformation.getContent());
            Matcher questionNameMatcher = questionNamePattern.matcher(detailInformation.getContent());
            Matcher answerIdMatcher = answerIdPattern.matcher(detailInformation.getContent());
            Matcher usernameMatcher = usernamePattern.matcher(detailInformation.getContent());
            Matcher excerptMatcher = excerptPattern.matcher(detailInformation.getContent());
            while (questionIdMatcher.find() && questionNameMatcher.find() && usernameMatcher.find() && excerptMatcher.find()) {
                String typeAndQuestionId = questionIdMatcher.group();
                String questionId = typeAndQuestionId.split(":")[2].substring(1);
                String questionName = questionNameMatcher.group().split("\"")[3];
                String username = usernameMatcher.group().split("\"")[3];
                String excerpt = excerptMatcher.group().substring(12,excerptMatcher.group().length() - 2);
                System.out.println(excerpt);
                HotQA hotQA = new HotQA(Integer.parseInt(childTopicId), Integer.parseInt(questionId), questionName, -1, username, excerpt, false);
                if (typeAndQuestionId.contains("question")) {
                    answerIdMatcher.find();
                    String answerId = answerIdMatcher.group().substring(8,answerIdMatcher.group().length() - 1);
                    hotQA.setAnswer_id(Integer.parseInt(answerId));
                } else {
                    hotQA.setArticle(true);
                }
                list.add(hotQA);
            }
        }
        return list;
    }

    public DetailInformation loadDetailInformation(String childTopicId, String limit, String afterId) throws IOException {
        HttpGet httpGet;
        if (!StringUtils.isNullOrEmpty(limit) && !StringUtils.isNullOrEmpty(afterId)) {
            httpGet = new HttpGet("https://www.zhihu.com/api/v4/topics/" + childTopicId + "/feeds/top_activity?limit=" + limit + "&after_id=" + afterId);
        } else {
            httpGet = new HttpGet("https://www.zhihu.com/api/v4/topics/" + childTopicId + "/feeds/top_activity");
        }
        httpGet.setHeader("authorization", "oauth c3cef7c66a1843f8b3a9e6a1e3160e20");
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setRetryHandler(new DefaultHttpRequestRetryHandler())
                .setConnectionManager(cm)
                .build();
        HttpResponse resp = httpClient.execute(httpGet);
        //not stream but entity
        String result = EntityUtils.toString(resp.getEntity());
        Matcher m = p.matcher(result);
        if (m.find()) {
            String limitAndAfterId = m.group();
            //limit=10&after_id=4473.51329"
            String[] arr = limitAndAfterId.split("&");
            DetailInformation detailInformation = new DetailInformation(Utils.decodeUnicode(result),
                    arr[0].substring(6),arr[1].substring(9,arr[1].length() - 1)); //匹配到要设置进去
            return detailInformation;
        } else {
            throw new IOException("no limit or after id");
        }
    }

}

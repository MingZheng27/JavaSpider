# JavaSpider
#### 能够爬取各话题下的热门内容的Java爬虫
### 1.如何使用
第一次运行：  
在cmd中cd到项目目录下运行
<pre>
source /工作目录/sql/createtable.sql
source /工作目录/sql/QAtable.sql</pre>
来建立数据库表
<pre>
getTopicId(); //用于获取主话题id
getAllChildTopics();  //用于获取子话题id</pre>
通过获取的子话题id来爬取相应的热门问答
<pre>loadAllTopicHotQA(); //爬取热门问答</pre>
如果您希望每隔一段时间爬取一次数据可以设置定时器来让程序每隔一段时间执行一次该方法  
### 2.如何调优
若您希望爬取速度更快那么可以修改线程池的大小：  
<pre>
ExecutorService exec = Executors.newFixedThreadPool(2);</pre>
**爬取过快或造成ip被封，可使用代理用不同的ip进行爬取**  
**[具体写作思路详见](http://www.jianshu.com/p/28a9bd3365c7)**

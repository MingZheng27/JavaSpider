package bean;

public class DetailInformation {

    private String content;
    private String limit;
    private String afterId;

    public DetailInformation() {
    }

    public DetailInformation(String content) {
        this.content = content;
    }

    public DetailInformation(String content, String limit, String afterId) {
        this.content = content;
        this.limit = limit;
        this.afterId = afterId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getAfterId() {
        return afterId;
    }

    public void setAfterId(String afterId) {
        this.afterId = afterId;
    }
}

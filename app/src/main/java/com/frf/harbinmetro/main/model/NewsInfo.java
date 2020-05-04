package com.frf.harbinmetro.main.model;

/**
 * @author 方荣福
 * @date 2019/6/3
 * 新闻信息 的javaBean
 */

public class NewsInfo {
    /**
     * 标题
     */
    private String title;
    /**
     * 时间
     */
    private String time;
    /**
     * 图片的URL
     */
    private String pic;
    /**
     * 新闻详情连接的URL
     */
    private String url;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

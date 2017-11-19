package mypackage.main.prototype;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Site {
    public Site() { }
    public Site(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @JsonProperty("title")
    public String title;

    @JsonProperty("content")
    public String content;
}

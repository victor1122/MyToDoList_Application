package TaskObject;

import java.io.Serializable;

/**
 * Created by anhnht on 09/26/2017.
 */

public class Task implements Serializable {
    private String header, content;
    private int percentComplete, id;
    private long fromDate, toDate;

    public Task() {
    }

    public Task(int id, String header, long fromDate, long toDate, String content, int percentComplete) {
        this.header = header;
        this.content = content;
        this.percentComplete = percentComplete;
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Task(String header, long fromDate, long toDate, String content, int percentComplete) {
        this.header = header;
        this.content = content;
        this.percentComplete = percentComplete;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getFromDate() {
        return fromDate;
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public long getToDate() {
        return toDate;
    }

    public void setToDate(long toDate) {
        this.toDate = toDate;
    }
}


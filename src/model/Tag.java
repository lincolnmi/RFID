package model;

/**
 * Created by Jason on 2016/3/28.
 */
public class Tag {
    private String ID;
    private int num;
    private int status;//0 - finished, 1 - join, 2 - silent temporarily

    public Tag(String ID) {
        this.ID = ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

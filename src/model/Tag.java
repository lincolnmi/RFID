package model;

/**
 * Created by Jason on 2016/3/28.
 */
public class Tag {
    private String ID;
    private int num;

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
}

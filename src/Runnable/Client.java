package Runnable;

import Util.Helper;
import model.Reader;
import model.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Jason on 2016/3/28.
 */
public class Client {

    public static void main(String[] args) {
        int tag_number = 10;
        int reader_number = 3;
        ArrayList<Tag> tags = new ArrayList<Tag>();
        for (int i=0;i<tag_number;i++) {
            Tag tag = new Tag(Helper.generateID());
            tag.setNum(i+1);
            tags.add(tag);
        }
        for (Tag tag : tags) {
            System.out.println(tag.getID());
        }
        ArrayList<Reader> readers = new ArrayList<Reader>();
        for (int i=0;i<reader_number;i++) {
            readers.add(new Reader(i+1));
        }
        HashMap<Integer,Set<Tag>> distribution = Helper.generateDistribution(readers,tags);
        for (int key:distribution.keySet()) {
            System.out.print(key + ": ");
            for (Tag tag:distribution.get(key)) {
                System.out.print(tag.getNum() + ",");
            }
            System.out.println();
        }
    }

}

package Util;

import model.Reader;
import model.Tag;

import java.util.*;

/**
 * Created by Jason on 2016/3/28.
 */
public class Helper {

    private static Random rdm = new Random();

    public static String generateID() {
        StringBuilder str = new StringBuilder();
        int len = 96;
        Random rdm = new Random();
        for (int i=0;i<len;i++) {
            int value = rdm.nextInt(2);
            str.append(value);
        }
        return str.toString();
    }

    public static HashMap<Integer,Set<Tag>> generateDistribution(ArrayList<Reader> readers,ArrayList<Tag> tags) {
        int size = readers.size();

        HashMap<Integer,Set<Tag>> distribution = new HashMap<Integer, Set<Tag>>();
        for (Tag tag:tags) {
            coverTags(distribution,tag,size);
        }
        for (int i=0;i<size*0.5;i++) {
            int index = rdm.nextInt(size);
            Tag tag = tags.get(index);
            int idx = rdm.nextInt(size)+1;
            coverTags(distribution,tag,size);
        }
        return distribution;
    }

    private static void coverTags(HashMap<Integer,Set<Tag>> distribution,Tag tag,int readerNumber) {
        int idx = rdm.nextInt(readerNumber)+1;
        Set<Tag> _tags = null;
        if (distribution.get(idx)==null) {
            _tags = new HashSet<Tag>();
        } else {
            _tags = distribution.get(idx);
        }
        _tags.add(tag);
        distribution.put(idx,_tags);
    }

}

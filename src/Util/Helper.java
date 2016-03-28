package Util;

import model.Reader;
import model.Tag;

import java.util.*;

/**
 * Created by Jason on 2016/3/28.
 */
public class Helper {
    private static long seed = 2016;

    private static Random rdm = new Random(seed);

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

    public static HashMap<Integer,Set<Tag>> generateDistribution(Set<Reader> readers,Set<Tag> tags) {
        int reader_num = readers.size();
        int tag_num = tags.size();

        HashMap<Integer,Set<Tag>> distribution = new HashMap<Integer, Set<Tag>>();
        for (Tag tag:tags) {
            coverTags(distribution,tag,reader_num);
            int index = rdm.nextInt(tag_num);
            if (index<=tag_num*0.8) {
                coverTags(distribution,tag,reader_num);
            }
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

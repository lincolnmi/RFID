package Runnable;

import Util.HashFunctions;
import Util.Helper;
import model.Reader;
import model.Tag;

import java.util.*;

/**
 * Created by Jason on 2016/3/28.
 */
public class Client {

    private static final int tag_number = 10;
    private static final int reader_number = 3;

    public static Set<Reader> initReader() {
        System.out.println("init readers");
        Set<Reader> readers = new HashSet<Reader>();
        for (int i=0;i<reader_number;i++) {
            readers.add(new Reader(i+1));
        }
        return readers;
    }

    public static Set<Tag> initTags() {
        System.out.println("init tags");
        Set<Tag> tags = new HashSet<Tag>();
        for (int i=0;i<tag_number;i++) {
            Tag tag = new Tag(Helper.generateID());
            tag.setNum(i+1);
            tags.add(tag);
        }
        for (Tag tag : tags) {
            System.out.println(tag.getID());
        }
        return tags;
    }

    public static HashMap<Integer,Set<Tag>> initDistribution(Set<Reader> readers,Set<Tag> tags) {
        System.out.println("init distribution");
        HashMap<Integer,Set<Tag>> distribution = Helper.generateDistribution(readers,tags);
        for (int key:distribution.keySet()) {
            System.out.print(key + ": ");
            for (Tag tag:distribution.get(key)) {
                System.out.print(tag.getNum() + ",");
            }
            System.out.println();
        }
        return distribution;
    }

    public static HashMap<Integer,Integer> getTagMappedIndex(Set<Tag> tags,int frame,long seed) {
        System.out.println("init map all tags index");
        HashMap<Integer,Integer> mappedResults = new HashMap<Integer, Integer>();
        Random rdm = new Random(seed);
        for (Tag tag:tags) {
            int idx = rdm.nextInt(frame);
            mappedResults.put(tag.getNum(),idx);
        }
        for (int key:mappedResults.keySet()) {
            System.out.println("Tag "+key + ":" + mappedResults.get(key));
        }
        return mappedResults;
    }

    public static HashMap<Integer,Set<Tag>> mapTags(Set<Tag> tags,HashMap<Integer,Integer> mappedIndex) {
        System.out.println("map tags");
        HashMap<Integer,Set<Tag>> mappedResults = new HashMap<Integer, Set<Tag>>();
        for (Tag tag:tags) {
            int idx = mappedIndex.get(tag.getNum());
            Set<Tag> _tags = null;
            if (mappedResults.get(idx)==null) {
                _tags = new HashSet<Tag>();
            } else {
                _tags = mappedResults.get(idx);
            }
            _tags.add(tag);
            mappedResults.put(idx,_tags);
        }
        for (int key:mappedResults.keySet()) {
            System.out.print(key + ": ");
            for (Tag tag:mappedResults.get(key)) {
                System.out.print(tag.getNum() + " ");
            }
            System.out.println();
        }
        return mappedResults;
    }

    public static Set<Tag> predictAdjacentTags(HashMap<Integer,Set<Tag>> expected,HashMap<Integer,Set<Tag>> reality) {
        Set<Tag> tags = new HashSet<Tag>();
        for (int key:reality.keySet()) {
            tags.addAll(expected.get(key));
        }
        return tags;
    }

    public static HashMap<Integer,Set<Tag>> mapReaders(HashMap<Integer,Set<Tag>> distribution,HashMap<Integer,
            Set<Tag>> allMappedResults,HashMap<Integer,Integer> mappedIndex) {
        HashMap<Integer,Set<Tag>> allPredictTags = new HashMap<Integer, Set<Tag>>();
        System.out.println("predict tags");
        for (int key:distribution.keySet()) {
            Set<Tag> tags = distribution.get(key);
            HashMap<Integer,Set<Tag>> subMappedResults = mapTags(tags,mappedIndex);
            Set<Tag> predictTags = predictAdjacentTags(allMappedResults,subMappedResults);
            allPredictTags.put(key,predictTags);
            System.out.print("Reader " + key + " : ");
            for (Tag tag:predictTags) {
                System.out.print(tag.getNum()+" ");
            }
            System.out.println();
        }
        return allPredictTags;
    }

    public static void output(HashMap<Integer,Set<Tag>> results) {
        for (int key:results.keySet())  {
            System.out.print("Reader " + key + " : ");
            for (Tag tag:results.get(key)) {
                System.out.print(tag.getNum()+" ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Set<Tag> tags = initTags();
        Set<Reader> readers = initReader();
        HashMap<Integer,Set<Tag>> distribution = initDistribution(readers,tags);
        HashMap<Integer,Integer> mappedIndex = getTagMappedIndex(tags, tag_number,2015);
        HashMap<Integer,Set<Tag>> allMappedResults = mapTags(tags,mappedIndex);
        HashMap<Integer,Set<Tag>> allPredictTags = mapReaders(distribution,allMappedResults,mappedIndex);
        for (int key:mappedIndex.keySet()) {
            System.out.println(mappedIndex.get(key) + ":" +key);
        }
        System.out.println("expected");
        output(distribution);
        System.out.println("predict");
        output(allPredictTags);
    }

}

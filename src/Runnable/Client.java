package Runnable;

import Util.HashFunctions;
import Util.Helper;
import model.Reader;
import model.Tag;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Jason on 2016/3/28.
 */
public class Client {

    private static int tag_number = 10;
    private static int reader_number = 1;
    private static final double e = Math.E;

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
            tag.setStatus(1);
            tags.add(tag);
        }
        /*for (Tag tag : tags) {
            System.out.println(tag.getID());
        }*/
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
        //System.out.println("init map all tags index");
        HashMap<Integer,Integer> mappedResults = new HashMap<Integer, Integer>();
        Random rdm = new Random(seed);
        for (Tag tag:tags) {
            if (tag.getStatus()!=0) {
                int idx = rdm.nextInt(frame);
                mappedResults.put(tag.getNum(), idx);
            }
        }
        /*for (int key:mappedResults.keySet()) {
            System.out.println("Tag "+key + ":" + mappedResults.get(key));
        }*/
        return mappedResults;
    }

    public static HashMap<Integer,Set<Tag>> mapTags(Set<Tag> tags,HashMap<Integer,Integer> mappedIndex) {
        //System.out.println("map tags");
        HashMap<Integer,Set<Tag>> mappedResults = new HashMap<Integer, Set<Tag>>();
        for (Tag tag:tags) {
            if (tag.getStatus()!=0) {
                int idx = mappedIndex.get(tag.getNum());
                Set<Tag> _tags = null;
                if (mappedResults.get(idx) == null) {
                    _tags = new HashSet<Tag>();
                } else {
                    _tags = mappedResults.get(idx);
                }
                _tags.add(tag);
                mappedResults.put(idx, _tags);
            }
        }
        for (int key:mappedResults.keySet()) {
            /*System.out.print(key + ": ");
            for (Tag tag:mappedResults.get(key)) {
                System.out.print(tag.getNum() + " ");
            }
            System.out.println();*/
        }
        return mappedResults;
    }

    public static Set<Tag> predictAdjacentTags(HashMap<Integer,Set<Tag>> expected,HashMap<Integer,Set<Tag>> reality) {
        Set<Tag> tags = new HashSet<Tag>();
        for (int key:reality.keySet()) {
            Set<Tag> expectedTags = expected.get(key);
            if (expectedTags.size()==1) {
                Tag tag = expectedTags.iterator().next();
                //tag.setStatus(0);
                tags.add(tag);
            }
        }
        return tags;
    }

    public static HashMap<Integer,Set<Tag>> mapReaders(HashMap<Integer,Set<Tag>> distribution,HashMap<Integer,
            Set<Tag>> allMappedResults,HashMap<Integer,Integer> mappedIndex) {
        HashMap<Integer,Set<Tag>> allPredictTags = new HashMap<Integer, Set<Tag>>();
        //System.out.println("predict tags");
        for (int key:distribution.keySet()) {
            Set<Tag> tags = distribution.get(key);
            HashMap<Integer,Set<Tag>> subMappedResults = mapTags(tags,mappedIndex);
            Set<Tag> predictTags = predictAdjacentTags(allMappedResults,subMappedResults);
            allPredictTags.put(key,predictTags);
            /*System.out.print("Reader " + key + " : ");
            for (Tag tag:predictTags) {
                System.out.print(tag.getNum()+" ");
            }
            System.out.println();*/
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

    public static int getNotIdentifiedTags(Set<Tag> tags) {
        int number = 0;
        for (Tag tag:tags) {
            if (tag.getStatus()!=0) {
                number++;
            }
        }
        return number;
    }

    public static int getExpectedDistributionNumbers(HashMap<Integer,Set<Tag>> distribution) {
        int expectedNumbers = 0;
        for (int key:distribution.keySet()) {
            expectedNumbers += distribution.get(key).size();
        }
        return expectedNumbers;
    }

    public static double EDFSA(HashMap<Integer,Set<Tag>> distribution) {
        double time = 0;
        for (int key:distribution.keySet()) {
            int size = distribution.get(key).size();
            time += e*size*2.4;
        }
        if (reader_number>4) {
            time = time * 4 / reader_number;
        }
        return time;
    }

    public static double getAdjacentTagsTime(Set<Tag> tags,Set<Reader> readers,HashMap<Integer,Set<Tag>> distribution) {
        int expectedNumbers = getExpectedDistributionNumbers(distribution);
        int realityNumbers = 0,round = 1;
        double time = 0;
        HashMap<Integer,Set<Tag>> finalPredictTags = new HashMap<Integer, Set<Tag>>();
        while (realityNumbers!=expectedNumbers) {
            System.out.println("round "+round);
            int frame = getNotIdentifiedTags(tags);
            HashMap<Integer,Integer> mappedIndex = getTagMappedIndex(tags,frame,round);
            HashMap<Integer,Set<Tag>> allMappedResults = mapTags(tags,mappedIndex);
            HashMap<Integer,Set<Tag>> allPredictTags = mapReaders(distribution,allMappedResults,mappedIndex);
            System.out.println(frame);
            if (reader_number>4) {
                time += frame*4*0.4;
            } else {
                time += frame*reader_number*0.4;
            }

            for (int key:mappedIndex.keySet()) {
                System.out.println(mappedIndex.get(key) + ":" +key);
            }
            for (int key:allPredictTags.keySet()) {
                for (Tag tag:allPredictTags.get(key)) {
                    tag.setStatus(0);
                    realityNumbers++;
                }

                if (finalPredictTags.containsKey(key)) {
                    finalPredictTags.get(key).addAll(allPredictTags.get(key));
                } else {
                    finalPredictTags.put(key,allPredictTags.get(key));
                }
            }

            //output(distribution);
            //System.out.println("predict");
            output(allPredictTags);
            //System.out.println("total identified tags: " + realityNumbers);
            round++;
            //Thread.sleep(1000);
        }
        System.out.println("final predict tags takes "+(round-1)+" rounds which costs "+ time +"ms");
        return time;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        File file = new File("time.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (int i=3;i<=3;i++) {
            reader_number = i;
            Set<Tag> tags = initTags();
            Set<Reader> readers = initReader();
            HashMap<Integer,Set<Tag>> distribution = initDistribution(readers,tags);
            double time = getAdjacentTagsTime(tags,readers,distribution);
            double edfsa = EDFSA(distribution);
            System.out.println((int)time*1.0/1000+"s "+(int)edfsa*1.0/1000+"s");
            bw.write((int)time*1.0/1000+" "+(int)edfsa*1.0/1000+"\n");
        }
        bw.flush();
        bw.close();
        //output(finalPredictTags);
    }

}

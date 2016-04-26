package Probability;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Jason on 2016/4/25.
 */
public class Probability {

    /* number of missing tags */
    private int m;
    /* number of known tags */
    private int n;
    /* number of unknown tags */
    private int u;
    /* the writer */
    private BufferedWriter bw;

    public Probability() {

    }

    public Probability(int m,int n,int u) {
        this.m = m;
        this.n = n;
        this.u = u;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setU(int u) {
        this.u = u;
    }

    public HashMap<Integer,Integer> checkKnownDistribution() {
        int size = n;
        HashMap<Integer,Integer> distribution = new HashMap<Integer,Integer>(size);
        int[] array = new int[size];
        Random rd = new Random();
        for (int i=0;i<size;i++) {
            int idx = rd.nextInt(size);
            array[idx]++;
            distribution.put(i,idx);
        }
        int singleton = 0, empty = 0, collision = 0;
        for (int i=0;i<size;i++) {
            if (array[i]==0) {
                empty++;
            } else if (array[i]==1) {
                singleton++;
            } else {
                collision++;
            }
        }
        System.out.println("known tags empty probability is "+empty*1.0/size);//approximate e^-1
        System.out.println("known tags singleton probability is "+singleton*1.0/size);//approximate e^-1
        System.out.println("known tags collision probability is "+collision*1.0/size);//approximate 1-2*e^-1
        return distribution;
    }

    public HashMap<Integer,Integer> getDistribution(int size) {
        HashMap<Integer,Integer> distribution = new HashMap<Integer,Integer>(size);
        Random rd = new Random();
        for (int i=0;i<size;i++) {
            int idx = rd.nextInt(size);
            distribution.put(i,idx);
        }
        return distribution;
    }

    public void checkDynamicDistribution() {
        int size = n + u;
        HashMap<Integer,Integer> distribution = getDistribution(size);
        int[] array_known_unknown = new int[size];
        int[] array_known = new int[size];
        int[] array_missing = new int[size];
        int[] array_all = new int[size];
        System.out.println(distribution.size());
        for (int key:distribution.keySet()) {
            int idx = distribution.get(key);
            array_known_unknown[idx]++;
            array_all[idx]++;
            // key less than n is justified as known
            if (key<n) {
                array_known[idx]++;
                array_missing[idx]++;
            }
            // key less than m is justified as missing
            if (key<m) {
                array_missing[idx]--;
                array_all[idx]--;
            }
        }
        //compute the number of missing tags
        int missing = getMissing(array_known, array_all, size);
        int unknown = getUnknown(array_known, array_all, size);
        int dynamic = getDifferent(array_known,array_all,size);
        double missing_ratio = missing*1.0/size;
        double unknown_ratio = unknown*1.0/size;
        System.out.println("the number of missing tags is "+missing + " simulation missing ratio is "+missing_ratio);
        System.out.println("the number of unknown tags is "+unknown + " simulation unknown ratio is "+unknown_ratio);
        System.out.println("the number of dynamic tags is "+dynamic + " simulation dynamic ratio is "+dynamic*1.0/size);
        /*System.out.println("array_known");
        output(array_known);
        System.out.println("array_known_unknown");
        output(array_known_unknown);
        System.out.println("array_known_missing");
        output(array_missing);
        System.out.println("array_all");
        output(array_all);*/
        try {
            bw.write(missing_ratio+" "+unknown_ratio+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getUnknown(int[] array_known, int[] array_all, int size) {
        int unknown = 0;
        for (int i=0;i<size;i++) {
            if (array_known[i]==0&&array_all[i]>0) {
                unknown++;
            }
        }
        return unknown;
    }

    private int getMissing(int[] array_known, int[] array_all, int size) {
        int missing = 0;
        for (int i=0;i<size;i++) {
            if (array_known[i]>0&&array_all[i]==0) {
                missing++;
            }
        }
        return missing;
    }

    private void output(int[] array) {
        for (int value:array) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    private int getDifferent(int[] array1,int[] array2,int size) {
        int different = 0;
        for (int i=0;i<size;i++) {
            different+=Math.abs(array1[i]-array2[i]);
        }
        return different;
    }

    public void outputTheoryValue() {
        int size = n+u;
        double missingRatio = Math.pow(1-1.0/size,n+u-m) - Math.pow(1-1.0/size,n+u);
        System.out.println("theory missing ratio is "+missingRatio);
        double unknownRatio = Math.pow(1-1.0/size,n) - Math.pow(1-1.0/size,n+u);
        System.out.println("theory unknown ratio is "+unknownRatio);
        try {
            bw.write(missingRatio+" "+unknownRatio+" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeProbability() {
        try {
            bw = new BufferedWriter(new FileWriter("probability.txt"));
            int m = 1000, n = 10000, u = 5000;
            setN(n);
            setU(u);
            for (m=500;m<=5000;m+=500) {
                setM(m);
                outputTheoryValue();
                checkDynamicDistribution();
            }
            setM(m);
            for (u=500;u<=5000;u+=500) {
                setU(u);
                outputTheoryValue();
                checkDynamicDistribution();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int m = 1000, n = 10000, u = 5000;
        Probability probability = new Probability();
        probability.writeProbability();
    }

}

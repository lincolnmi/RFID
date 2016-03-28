package Util;

public class HashFunctions {
	private static int hashValues[];

	public static int hash(String str, int index) {
		hashValues = new int[9];
		hashValues[0] = RSHash(str);
		hashValues[1] = JSHash(str);
		hashValues[2] = ELFHash(str);
		hashValues[3] = BKDRHash(str);
		hashValues[4] = APHash(str);
		hashValues[5] = DJBHash(str);
		hashValues[6] = SDBMHash(str);
		hashValues[7] = PJWHash(str);
		hashValues[8] = DEKHash(str);
		int hash = 0,size=0;
		while(index>0) {
			if ((index&1)>0) {
				hash+=hashValues[size];
				//System.out.print(size+" ");
				if (size%100==0) {
					//System.out.println();
				}
			}
			size++;
			index>>=1;
		}
        return hash;
    }
	
    public static int RSHash(String str) {
    	//System.out.println(str);
        int hash = 0;
        int magic = 63689;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            hash = hash * magic + str.charAt(i);
            magic = magic * 378551;
        }
        return hash;
    }
    
    public static int JSHash(String str) {
        int hash = 1315423911;
        for (int i = 0; i < str.length(); i++) {
            hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
        }
        return hash;
    }
    
    public static int ELFHash(String str) {
        int hash = 0;
        int x = 0;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            hash = (hash << 4) + str.charAt(i);
            if ((x = hash & 0xF0000000) != 0) {
                hash ^= (x >> 24);
                hash &= ~x;
            }
        }
        return hash;
    }
    
    
    public static int BKDRHash(String str) {
        int seed = 131;
        int hash = 0;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            hash = (hash * seed) + str.charAt(i);
        }
        return hash;
    }
    
    public static int APHash(String str) {
        int hash = 0;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if ((i & 1) == 0) {
                hash ^= ((hash << 7) ^ str.charAt(i) ^ (hash >> 3));
            } else {
                hash ^= (~((hash << 11) ^ str.charAt(i) ^ (hash >> 5)));
            }
        }
        return hash;
    }
    
    public static int DJBHash(String str) {
        int hash = 5381;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            hash = ((hash << 5) + hash) + str.charAt(i);
        }
        return hash;
    }
    
    public static int SDBMHash(String str) {
        int hash = 0;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
        }
        return hash;
    }
    
    public static int PJWHash(String str) {
        long BitsInUnsignedInt = (4 * 8);
        long ThreeQuarters = ((BitsInUnsignedInt * 3) / 4);
        long OneEighth = (BitsInUnsignedInt / 8);
        long HighBits = (long) (0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);
        int hash = 0;
        long test = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash << OneEighth) + str.charAt(i);
            if ((test = hash & HighBits) != 0) {
                hash = (int) ((hash ^ (test >> ThreeQuarters)) & (~HighBits));
            }
        }
        return hash;
    }
    
    public static int DEKHash(String str) {  
    	int hash = str.length();
    	for(int i = 0; i < str.length(); i++) {
    		hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
    	}
    	return hash;
    }
}

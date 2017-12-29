package cs6301.g36;
import java.util.ArrayList;
import java.util.List;

public class KMP {
    public static void main(String [] args) {
    	int pai [] = findPai("ABABA");
    	
    	System.out.println(kmp("ABABA", "ABABABABBABABAA", pai));
    }
    
    public static int[] findPai(String p) {
    	char r [] = (" "+p).toCharArray();
    	int [] pai = new int [r.length];
    	int k = 0;
    	pai[1] = 0;
    	for(int q = 2; q <= p.length(); q++) {
    		while(k > 0 && r[k + 1] != r[q]) {
    			k = pai[k];
    		}
    		
    		if(r[k + 1] == r[q]) {
    			k += 1;
    		}
    		pai[q] = k;
    	}
    	return pai;
    }
    
    public static List<Integer> kmp(String pattern, String text, int[] pai) {
    	char [] p = (" "+pattern).toCharArray();
    	char [] t = (" "+text).toCharArray();
    	List<Integer> ans = new ArrayList<>();
    	int k = 0;
    	for(int i = 1; i <= text.length(); i++) {
    		while(k > 0 && p[k + 1] != t[i]) {
    			k = pai[k];
    		}
    		
    		if(p[k + 1] == t[i]) {
    			k++;
    		}
    		
    		if(k == pattern.length()) {
    			ans.add(i - k);
    			k = pai[k];
    		}
    	}
    	return ans;
    }
}
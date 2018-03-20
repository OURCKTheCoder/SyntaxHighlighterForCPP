package ourck.tokens;

import java.util.*;

public class CmtManager extends TokenManager{
	private static final String PREFIX = "<font color=\"#8C3FC8\">" ;
	private static final String SUFFIX = "</font>";
	private static LinkedList<Token> cmts = new LinkedList<Token>();
	public static ArrayList<Token> SharedCmtBlockList = new ArrayList<Token>();
	// TODO ^ This list is used for some methods. Fairly not a good idea.

	public static boolean isPtrInsideCmtBlock(int index) {
		for(Token cmt : SharedCmtBlockList) {
			if(index >= cmt.getIndex() && index <= cmt.getRear()) {
				return true;
			}
		}
		return false;
	}
	
	public static int ptrPosByBlock(Token cmtBlock, int ptr) {
		if(cmtBlock.getIndex() > ptr) return -1;
		else if(cmtBlock.getRear() < ptr) return 1;
		else return 0;
	}
	
	public void add(Token t) { 
		cmts.add(t); 
	}
	
	public boolean cmp(String s) { return false; } // TODO Fake method!
	
	public LinkedList<Token> currentList() { return cmts; }

	public StringBuilder analyze(StringBuilder newToken) {
		SharedCmtBlockList.clear();
		cmts.clear();
		
		char[] charary = newToken.toString().toCharArray();
		Character[] chars = new Character[charary.length];
		for(int i = 0; i < charary.length; i++) {
			chars[i] = charary[i];
		}
		
		//1.Get all tokens' positions:
		int state = 0;
		int head = 0, rear = 0;
		for(int it = 0; it < chars.length; it++) {
			if(isPtrInsideCmtBlock(it)) continue;
			Character c = new Character(chars[it]);
			switch(state) {
			case 0:
				if(c.equals(' ') || c.equals('\t')) state = 0;
				else if(c.equals('/')) {
					head = it;
					state = 1;
				}
				break;
			case 1:
				if(c.equals('/')) state = 3;
				else if(c.equals('*')) state = 2;
				else state = 0;
				break;
			case 2:
				rear = it; // [!] Avoid analyzing to file's end.
				if(c.equals('*')) state = 4;
				else state = 2;
				break;
			case 3:
				if(!c.equals('\n')) { state = 3; }
				else {
					rear = it;
					Comment cmt = new Comment(head, rear);
					add(cmt);
					state = 5;
				}
				break;
			case 4:
				if(c.equals('/')) {
					rear = it + 1;
					Comment cmt = new Comment(head, rear);
					add(cmt);
					state = 5;
				}
				else state = 2;
				break;
			case 5: 
				state = 0;
				break;
			}
		}
		shareList();
		return newToken;
	}
	
	private void shareList() {
		SharedCmtBlockList.addAll(cmts);
	}
		
	public void highlightAll(StringBuilder newToken) {
		SharedCmtBlockList.clear();
		//2.Highlight all.
		int baseIndex = 0; 
		for(Token t : cmts) { // [!] Iterates in sequence.
			int head = t.getIndex(), rear = t.getRear();
			// [!] Rear goes first.
			newToken.insert(rear + baseIndex, SUFFIX);
			newToken.insert(head + baseIndex, PREFIX);
			
			SharedCmtBlockList.add(new Comment(head + baseIndex,
					rear + baseIndex + SUFFIX.length() + PREFIX.length()));
			
			baseIndex += SUFFIX.length() + PREFIX.length();
		}
		
	}
}

class Comment extends Token {
	Comment(int head, int rear){
		super("(COMMENTS)", head, rear);
	}
}

package ourck.tokens;

import java.util.LinkedList;

public class LTGTMaker implements SPCharManager {
	private static final String LTCHAR = "&lt;";
	private static final String GTCHAR = "&gt;";
	private LinkedList<LTGTChar> LTCharList = new LinkedList<LTGTChar>();
	
	public StringBuilder analyze(StringBuilder txt) {
		//1.Record position.
		char[] charAry = txt.toString().toCharArray();
		LinkedList<Character> chars = new LinkedList<Character>();
		for(char c : charAry) {
			chars.add(c);
		}
		for(int i = 0; i < chars.size(); i++) {
			if(chars.get(i).equals('<')) {
				LTCharList.add(new LTChar(i));// Backward
			}
			if(chars.get(i).equals('>')) {
				LTCharList.add(new GTChar(i));// Backward
			}
		}
		
		//2.
		int baseIndex = 0;
		for(LTGTChar t : LTCharList) {
			int pos = t.getIndex();
			if(t.getStr().equals("lt")) {
				txt.insert(pos + baseIndex, LTCHAR);
				
				for(Token cmtBlock : CmtManager.SharedCmtBlockList) {
					if(CmtManager.ptrPosByBlock(cmtBlock, pos + baseIndex) == -1) {
						cmtBlock.setIndex(cmtBlock.getIndex() + LTCHAR.length() - 1);
						cmtBlock.setRear(cmtBlock.getRear() + LTCHAR.length() - 1);
					}
				}
				
				baseIndex += LTCHAR.length();
			}
			else {
				txt.insert(pos + baseIndex, GTCHAR);
				
				for(Token cmtBlock : CmtManager.SharedCmtBlockList) {
					if(CmtManager.ptrPosByBlock(cmtBlock, pos + baseIndex) == -1) {
						//[!] 减一是因为原先的大于/小于符号被删掉了
						cmtBlock.setIndex(cmtBlock.getIndex() + LTCHAR.length() - 1);
						cmtBlock.setRear(cmtBlock.getRear() + LTCHAR.length() - 1);
					}
				}
				
				baseIndex += GTCHAR.length();
			}
			txt.deleteCharAt(pos + baseIndex);
			baseIndex--;
		}
		
		return txt;
	}
}

abstract class LTGTChar extends Token {
	LTGTChar(String lt_gt, int pos){
		super(lt_gt, pos, pos);
	}
}

class LTChar extends LTGTChar {
	public LTChar(int pos) {
		super("lt", pos);
	}
}

class GTChar extends LTGTChar {
	public GTChar(int pos) {
		super("gt", pos);
	}
}

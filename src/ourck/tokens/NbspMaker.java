package ourck.tokens;

import java.util.LinkedList;

public class NbspMaker implements SPCharManager{
	private static final String SPACECHAR = "&nbsp;";
	private LinkedList<SpaceChar> spList = new LinkedList<SpaceChar>();
	public StringBuilder analyze(StringBuilder txt) {
		//1.Record position.
		char[] charAry = txt.toString().toCharArray();
		LinkedList<Character> chars = new LinkedList<Character>();
		for(char c : charAry) {
			chars.add(c);
		}
		for(int i = 0; i < chars.size(); i++) {
			if(chars.get(i).equals(' ')) {
				spList.add(new SpaceChar(i));// Backward
			}
		}
		
		//2.Add space.
		int baseIndex = 0;
		for(Token t : spList) {
			int pos = t.getIndex();
			txt.deleteCharAt(pos + baseIndex);
			txt.insert(pos + baseIndex, SPACECHAR);
			
			for(Token cmtBlock : CmtManager.SharedCmtBlockList) {
				if(CmtManager.ptrPosByBlock(cmtBlock, pos + baseIndex) == -1) {
					cmtBlock.setIndex(cmtBlock.getIndex() + SPACECHAR.length() - 1);
					cmtBlock.setRear(cmtBlock.getRear() + SPACECHAR.length() - 1);
				}
				if(CmtManager.ptrPosByBlock(cmtBlock, pos + baseIndex) == 0) {
					cmtBlock.setRear(cmtBlock.getRear() + SPACECHAR.length() - 1);
				}
			}
			
			baseIndex += SPACECHAR.length() - 1;//TODO ???
		}
		return txt;
	}
}

class SpaceChar extends Token {
	public SpaceChar(int pos) {
		super("(_)", pos, pos);
	}
}

package ourck.tokens.manager;

import java.util.LinkedList;

import ourck.tokens.token.*;

/**
 * 空格字符管理器。<br>
 * 在HTML中，字符' '需要做特殊转义处理。<br>
 * @deprecated 虽说' '需要做特殊转义处理为'&nbsp'<br>
 * 				但实际上，可以通过在HTML文本中嵌入pre标签来跳过人工转义。
 * @author ourck
 */
public class NbspMaker extends TokenManager{
	
	private static final String SPACECHAR = "&nbsp;";
	private LinkedList<SpaceChar> spList = new LinkedList<SpaceChar>();
	
	@Override
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
			
			for(Token cmtBlock : CmtManager.getCmtList()) {
				if(CmtManager.ptrPosByBlock(cmtBlock, pos + baseIndex) == -1) {
					cmtBlock.setIndex(cmtBlock.getIndex() + SPACECHAR.length() - 1);
					cmtBlock.setRear(cmtBlock.getRear() + SPACECHAR.length() - 1);
				}
				if(CmtManager.ptrPosByBlock(cmtBlock, pos + baseIndex) == 0) {
					cmtBlock.setRear(cmtBlock.getRear() + SPACECHAR.length() - 1);
				}
			}
			
			baseIndex += SPACECHAR.length() - 1; //TODO ???
		}
		
		doNext(txt);

		return txt;
	}

	@Override
	public void reset() { spList.clear(); }
}

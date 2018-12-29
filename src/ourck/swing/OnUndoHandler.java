package ourck.swing;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

/**
 * 重做 / 撤销时的事件处理器具体类
 * @author ourck
 */
public class OnUndoHandler implements UndoableEditListener {
	
	/**
	 * 撤销使用到的管理器
	 */
	private UndoManager undoMgr = new UndoManager();
	
	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		System.out.println(" - Recorded.");
		undoMgr.addEdit(e.getEdit());
	}
	
	public void undo() { undoMgr.undo(); }
	
	public void redo() { undoMgr.redo(); }
}

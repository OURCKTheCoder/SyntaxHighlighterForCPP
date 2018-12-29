package ourck.swing;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 文档改变时的事件处理器具体类
 * @author ourck
 */
public class DocOnChangeHandler implements DocumentListener{
	@Override
	public void removeUpdate(DocumentEvent e) {
		System.out.println("Remove!");
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		System.out.println("Insert!");
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		System.out.println("Change!");
	}
}

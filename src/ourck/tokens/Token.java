package ourck.tokens;

public abstract class Token {
	private String str;
	private Integer head;
	private Integer rear;
	Token(String str, int head, int rear){
		this.str = str;
		this.head = head;
		this.rear = rear;
	}
	public Integer getIndex() { return head; }
	public Integer getRear() { return rear; }
	public void setIndex(Integer val) { head = val;}
	public void setRear(Integer val) { rear = val;}
	public String getStr() { return str; }
	
	// Override
	public String toString() {
		return "[" + head + " - " + rear + ", " + str + "]";
	}
	// Use this method to compare token with words in the list. 
	public boolean equals(Object obj) { // TODO May occur Exceptions when casting.
		if(str == ((Token)obj).getStr() | head == ((Token)obj).getIndex() ) { 
			// [!] 						   ^	ShortCircuit is not allowed.
			return true;
		}
		else {
			return false;
		}
	}
}

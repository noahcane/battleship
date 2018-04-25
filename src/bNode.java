public class bNode { //each coordinate has priority, AKA bNode
	
	private Integer priority;
	private bNode N = null;
	private bNode E = null;
	private bNode S = null;
	private bNode W = null;
	
	public bNode(){
		this.priority = 1;
	}

	public Integer getPriority(){
		return priority;
	}
	
	public void setPriority(int p) {
		this.priority = p;
	}
	
	public void addPriority(int p) {
		this.priority += p;
	}
	
	public bNode getN() {
		return N;
	}
	
	public bNode getE() {
		return E;
	}
	
	public bNode getS() {
		return S;
	}

	public bNode getW() {
		return W;
	}
	
	public void setN(bNode n) {
		this.N = n;
	}

	public void setE(bNode n) {
		this.E = n;
	}

	public void setS(bNode n) {
		this.S = n;
	}

	public void setW(bNode n) {
		this.W = n;
	}

}

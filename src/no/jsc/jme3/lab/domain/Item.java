package no.jsc.jme3.lab.domain;

public class Item {
	String name;
	String functionDescShort;
	String functionDescLong;
	
	public Item(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFunctionDescShort() {
		return functionDescShort;
	}

	public void setFunctionDescShort(String functionDescShort) {
		this.functionDescShort = functionDescShort;
	}

	public String getFunctionDescLong() {
		return functionDescLong;
	}

	public void setFunctionDescLong(String functionDescLong) {
		this.functionDescLong = functionDescLong;
	}
	
	
}

package com.example.biboy.ictraffle;

public class Student {
	String name, id, qr;

	
	public Student(String qr, String name, String id) {
		super();
		this.name = name;
		this.id = id;
		this.qr = qr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQr() {
		return qr;
	}

	public void setQr(String qr) {
		this.qr = qr;
	}
	
	
		
}

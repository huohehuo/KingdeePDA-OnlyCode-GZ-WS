package com.fangzuo.assist.Beans;

public class CommonBean {
	public String FName;
	public String FNumber;
	public String FStandby1;
	public String FStandby2;
	public String FStandby3;
	public String FStandby4;
	public String FStandby5;
	public CommonBean(String name, String number){
		FName=name;
		FNumber = number;
	}
	public CommonBean(String name, String number,boolean s){
		FStandby1=name;
		FStandby2 = number;
	}
	public CommonBean(String name){
		FStandby1=name;
	}


	public String getFName() {
		return FName;
	}

	public void setFName(String FName) {
		this.FName = FName;
	}

	public String getFNumber() {
		return FNumber;
	}

	public void setFNumber(String FNumber) {
		this.FNumber = FNumber;
	}

	public String getFStandby1() {
		return FStandby1;
	}

	public void setFStandby1(String FStandby1) {
		this.FStandby1 = FStandby1;
	}

	public String getFStandby2() {
		return FStandby2;
	}

	public void setFStandby2(String FStandby2) {
		this.FStandby2 = FStandby2;
	}

	public String getFStandby3() {
		return FStandby3;
	}

	public void setFStandby3(String FStandby3) {
		this.FStandby3 = FStandby3;
	}

	public String getFStandby4() {
		return FStandby4;
	}

	public void setFStandby4(String FStandby4) {
		this.FStandby4 = FStandby4;
	}

	public String getFStandby5() {
		return FStandby5;
	}

	public void setFStandby5(String FStandby5) {
		this.FStandby5 = FStandby5;
	}
}

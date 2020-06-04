package com.example.demo;

import com.opencsv.bean.CsvBindByName;

public class GCPCalVo {

	@CsvBindByName
	private String environment;
	@CsvBindByName
	private String vCPU;
	@CsvBindByName
	private String memory;
	@CsvBindByName
	private String location;
	@CsvBindByName
	private String instanceType;
	@CsvBindByName
	private String operatingSystem;
	@CsvBindByName
	private String diskSpace;
	@CsvBindByName
	private String commitmentType;
	@CsvBindByName
	private Double totalCost;
	
	
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getvCPU() {
		return vCPU;
	}
	public void setvCPU(String vCPU) {
		this.vCPU = vCPU;
	}
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getInstanceType() {
		return instanceType;
	}
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}
	public String getOperatingSystem() {
		return operatingSystem;
	}
	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	public String getDiskSpace() {
		return diskSpace;
	}
	public void setDiskSpace(String diskSpace) {
		this.diskSpace = diskSpace;
	}
	public String getCommitmentType() {
		return commitmentType;
	}
	public void setCommitmentType(String commitmentType) {
		this.commitmentType = commitmentType;
	}
	public Double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
	
}

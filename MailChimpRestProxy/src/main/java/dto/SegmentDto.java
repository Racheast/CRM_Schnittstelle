package dto;

import java.util.Arrays;

public class SegmentDto {
	private String name;
	private String[] static_segment;
	
	public SegmentDto() {
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getStatic_segment() {
		return static_segment;
	}
	public void setStatic_segment(String[] static_segment) {
		this.static_segment = static_segment;
	}
	public SegmentDto(String name, String[] static_segment) {
		super();
		this.name = name;
		this.static_segment = static_segment;
	}
	@Override
	public String toString() {
		return "SegmentDto [name=" + name + ", static_segment=" + Arrays.toString(static_segment) + "]";
	}
	
	
	
}

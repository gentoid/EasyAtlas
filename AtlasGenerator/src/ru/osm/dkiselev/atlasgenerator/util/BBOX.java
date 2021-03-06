package ru.osm.dkiselev.atlasgenerator.util;

public class BBOX {
	
	private double top;
	private double bottom;
	private double left;
	private double right;
	
	public BBOX(double top, double bottom, double left, double right) {
		super();
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}
	
	public double getTop() {
		return top;
	}
	
	public double getBottom() {
		return bottom;
	}
	
	public double getLeft() {
		return left;
	}
	
	public double getRight() {
		return right;
	}

	public void move(double dx, double dy) {
		bottom += dy;
		top += dy;
		
		left += dx;
		right += dx;
	}
	
	@Override
	public String toString() {
		return String.format("%.6f %.6f %.6f %.6f", top, bottom, left, right);
	}
	
}

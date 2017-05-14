package org.keyanalysis.View;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

/**
 * 
 * @author Bereczki Tamás
 *
 */
@JavaScript("org_keyanalysis_View_LineComplex.js")
public class LineComplex extends AbstractJavaScriptComponent {
	private static final long serialVersionUID = -9061447705933132682L;

	private List<Double> list;
	private double avg;
	private double std;

	public void sendComplexTypes() {
		this.getRpcProxy(LineComplexRpc.class).sendComplexTypes(this.list, this.avg, this.std);
	}

	public LineComplex(final List<Double> list, final double avg, final double std) {
		this.list = new ArrayList<>(list);
		this.avg = avg;
		this.std = std;
		this.sendComplexTypes();
	}

	public LineComplex() {
	}

	public List<?> getList() {
		return this.list;
	}

	public void setList(final List<Double> list) {
		this.list = list;
	}
}

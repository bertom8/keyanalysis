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
		getRpcProxy(LineComplexRpc.class).sendComplexTypes(list, avg, std);
	}

	public LineComplex(List<Double> list, double avg, double std) {
		this.list = new ArrayList<Double>(list);
		this.avg = avg;
		this.std = std;
		sendComplexTypes();
	}

	public LineComplex() {
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<Double> list) {
		this.list = list;
	}
}

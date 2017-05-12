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
@JavaScript("org_keyanalysis_View_CompareComplex.js")
public class CompareComplex extends AbstractJavaScriptComponent {
	private static final long serialVersionUID = -9061447705933132682L;

	private List<Double> list, list2;

	public void sendComplexTypes() {
		getRpcProxy(CompareRateComplex.class).sendComplexTypes(list, list2);
	}

	public CompareComplex(List<Double> list, List<Double> list2) {
		this.list = new ArrayList<Double>(list);
		this.list2 = new ArrayList<Double>(list2);
		sendComplexTypes();
	}

	public CompareComplex() {
	}

	public List<?> getList() {
		return list;
	}
	
	public List<?> getList2() {
		return list2;
	}

	public void setList(List<Double> list) {
		this.list = list;
	}
	
	public void setList2(List<Double> list) {
		this.list2 = list;
	}
}
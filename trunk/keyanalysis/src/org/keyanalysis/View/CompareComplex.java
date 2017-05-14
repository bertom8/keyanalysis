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
		this.getRpcProxy(CompareRateComplex.class).sendComplexTypes(this.list, this.list2);
	}

	public CompareComplex(final List<Double> list, final List<Double> list2) {
		this.list = new ArrayList<>(list);
		this.list2 = new ArrayList<>(list2);
		this.sendComplexTypes();
	}

	public CompareComplex() {
	}

	public List<?> getList() {
		return this.list;
	}

	public List<?> getList2() {
		return this.list2;
	}

	public void setList(final List<Double> list) {
		this.list = list;
	}

	public void setList2(final List<Double> list) {
		this.list2 = list;
	}
}
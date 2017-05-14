package org.keyanalysis.View;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

/**
 * 
 * @author Bereczki Tamás
 */
@JavaScript("org_keyanalysis_View_RateComplex.js")
public class RateComplex extends AbstractJavaScriptComponent {
	private static final long serialVersionUID = -9061447705933132682L;

	private List<Double> list;

	public void sendComplexTypes() {
		this.getRpcProxy(ComplexTypesRpc.class).sendComplexTypes(this.list);
	}

	public RateComplex(final List<Double> list) {
		this.list = new ArrayList<>(list);
		this.sendComplexTypes();
	}

	public RateComplex() {
	}

	public List<?> getList() {
		return this.list;
	}

	public void setList(final List<Double> list) {
		this.list = list;
	}
}

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
@JavaScript("org_keyanalysis_View_HammingComplex.js")
public class HammingComplex extends AbstractJavaScriptComponent {
	private static final long serialVersionUID = -9061447705933132682L;

	private List<Double> list;

	public void sendComplexTypes() {
		getRpcProxy(ComplexTypesRpc.class).sendComplexTypes(list);
	}

	public HammingComplex(List<Double> list) {
		this.list = new ArrayList<Double>(list);
		sendComplexTypes();
	}

	public HammingComplex() {
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<Double> list) {
		this.list = list;
	}
}

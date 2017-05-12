package org.keyanalysis.View;

import java.util.List;

import com.vaadin.shared.communication.ClientRpc;

/**
 * 
 * @author Bereczki Tamás
 */
public interface ComplexTypesRpc extends ClientRpc {
	public void sendComplexTypes(List<Double> list);
}

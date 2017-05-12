package org.keyanalysis.View;

import java.util.List;

import com.vaadin.shared.communication.ClientRpc;

/**
 * 
 * @author Bereczki Tamás
 *
 */
public interface CompareRateComplex extends ClientRpc {
	public void sendComplexTypes(List<Double> list, List<Double> list2);
}

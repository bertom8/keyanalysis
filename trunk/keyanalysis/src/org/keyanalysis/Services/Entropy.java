package org.keyanalysis.Services;

import java.util.HashMap;
import java.util.Map;

public class Entropy {

	/*
	 * Source: https://rosettacode.org/wiki/Entropy#Java
	 */
	@SuppressWarnings("boxing")
	public static double getShannonEntropy(String s) {
		int n = 0;
		Map<Character, Integer> occ = new HashMap<>();

		for (int c_ = 0; c_ < s.length(); ++c_) {
			char cx = s.charAt(c_);
			if (occ.containsKey(cx)) {
				occ.put(cx, occ.get(cx) + 1);
			} else {
				occ.put(cx, 1);
			}
			++n;
		}

		double e = 0.0;
		for (Map.Entry<Character, Integer> entry : occ.entrySet()) {
			double p = (double) entry.getValue() / n;
			e += p * log2(p);
		}
		return -e;
	}

	private static double log2(double a) {
		return Math.log(a) / Math.log(2);
	}
}

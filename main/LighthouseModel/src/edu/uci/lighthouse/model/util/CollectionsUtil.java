package edu.uci.lighthouse.model.util;

import java.util.Arrays;
import java.util.Collection;

public class CollectionsUtil {

	public static boolean equals(Collection<?> c1, Collection<?> c2) {
		Object[] arr1 = c1.toArray();
		Object[] arr2 = c2.toArray();
		return arr1.length == arr2.length && Arrays.equals(arr1, arr2);
	}
}

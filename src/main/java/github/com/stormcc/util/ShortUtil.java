package github.com.stormcc.util;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author xingmao@
 * Create at: 2019-08-30
 */
public final class ShortUtil {
	private ShortUtil(){}
	private static String COMMA_SEPARATOR = ",";

	public static List<Short> toListElseEmptyList(String ids) {
		if (Strings.isNullOrEmpty(ids)) {
			return new ArrayList<>();
		}

		String[] arr = ids.split(COMMA_SEPARATOR);
		if ( arr.length <=0 ){
			return new ArrayList<>();
		}

		List<Short> idList = new ArrayList<>();
		for (String s : arr) {
			if ( ! isPositiveDigit(s) ){
				return new ArrayList<>();
			} else {
				idList.add(Short.valueOf(s));
			}
		}
		return idList;
	}

	private static boolean isPositiveDigit(String str) {
		if ( Strings.isNullOrEmpty(str) ) {
			return false;
		}
		for (int i = str.length()-1; i >=0; i-- ) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}

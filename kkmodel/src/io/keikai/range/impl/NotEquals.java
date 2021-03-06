/* NotEquals.java

	Purpose:
		
	Description:
		
	History:
		July 18, 2017 3:13:53 PM, Created by rwenzel

	Copyright (C) 2017 Potix Corporation. All Rights Reserved.
*/

package io.keikai.range.impl;

import io.keikai.model.impl.RuleInfo;

// ZSS-1336
/**
 * @author rwenzel
 * @since 3.9.2
 */
public class NotEquals extends Equals {
	private static final long serialVersionUID = -5745004549005666242L;

	public NotEquals(Object b) {
		super(b);
	}
	public NotEquals(RuleInfo ruleInfo) {
		super(ruleInfo);
	}
			
	protected boolean matchString(String text, String b) {
		return !super.matchString(text, b);
	}

	protected boolean matchDouble(Double num, Double b) {
		return !super.matchDouble(num, b);
	}
}

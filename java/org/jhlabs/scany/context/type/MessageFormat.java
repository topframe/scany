/*
 *  Copyright (c) 2008 Jeong Ju Ho, All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jhlabs.scany.context.type;

import java.util.HashMap;
import java.util.Map;


public final class MessageFormat extends Type {

	public static final MessageFormat XML;

	public static final MessageFormat JSON;
	
	public static final MessageFormat BINARY;
	
	private static final Map<String, MessageFormat> types;
	
	static {
		XML = new MessageFormat("xml");
		JSON = new MessageFormat("json");
		BINARY = new MessageFormat("binary");

		types = new HashMap<String, MessageFormat>();
		types.put(XML.toString(), XML);
		types.put(JSON.toString(), JSON);
		types.put(BINARY.toString(), BINARY);
	}

	private MessageFormat(String type) {
		super(type);
	}

	public static MessageFormat valueOf(String type) {
		if(type == null)
			return null;
		
		return types.get(type);
	}
}

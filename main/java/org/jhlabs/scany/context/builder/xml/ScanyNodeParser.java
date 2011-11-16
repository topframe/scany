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
package org.jhlabs.scany.context.builder.xml;

import java.io.InputStream;
import java.util.Properties;

import org.jhlabs.scany.context.builder.ScanyContextBuilderAssistant;
import org.jhlabs.scany.context.rule.ClientRule;
import org.jhlabs.scany.context.rule.ServerRule;
import org.jhlabs.scany.util.xml.Nodelet;
import org.jhlabs.scany.util.xml.NodeletParser;
import org.w3c.dom.Node;

/**
 * Translet Map Parser.
 * 
 * <p>Created: 2008. 06. 14 오전 4:39:24</p>
 */
public class ScanyNodeParser {
	
	private final NodeletParser parser = new NodeletParser();

	private final ScanyContextBuilderAssistant assistant;
	
	/**
	 * Instantiates a new translet map parser.
	 * 
	 * @param assistant the assistant for Context Builder
	 */
	public ScanyNodeParser(ScanyContextBuilderAssistant assistant) {
		//super(log);
		
		this.assistant = assistant;
		this.assistant.clearObjectStack();

		parser.setValidation(true);
		parser.setEntityResolver(new ScanyDtdResolver());

		addRootNodelets();
		addLocalNodelets();
		addClientNodelets();
		addServerNodelets();
	}

	/**
	 * Parses the translet map.
	 * 
	 * @param inputStream the input stream
	 * 
	 * @return the translet rule map
	 * 
	 * @throws Exception the exception
	 */
	public void parse(InputStream inputStream) throws Exception {
		try {
			parser.parse(inputStream);
		} catch(Exception e) {
			throw new Exception("Error parsing scany context configuration. Cause: " + e, e);
		}
	}

	/**
	 * Adds the translet map nodelets.
	 */
	private void addRootNodelets() {
		parser.addNodelet("/scany", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
			}
		});
	}

	private void addLocalNodelets() {
		parser.addNodelet("/scany/local", new LocalServiceRuleNodeletAdder(assistant));
	}
	
	private void addClientNodelets() {
		parser.addNodelet("/scany/client", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				ClientRule cr = new ClientRule();
				assistant.pushObject(cr);
			}
		});
		
		parser.addNodelet("/scany/client/local", new LocalServiceRuleNodeletAdder(assistant));
		parser.addNodelet("/scany/client/http", new HttpServiceRuleNodeletAdder(assistant));
		parser.addNodelet("/scany/client/remote", new HttpServiceRuleNodeletAdder(assistant));

		parser.addNodelet("/scany/client/end()", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				ClientRule cr = (ClientRule)assistant.popObject();
				assistant.setClientRule(cr);
			}
		});
	}
	
	private void addServerNodelets() {
		parser.addNodelet("/scany/server", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				ServerRule sr = new ServerRule();
				assistant.pushObject(sr);
			}
		});

		parser.addNodelet("/scany/server/remote", new HttpServiceRuleNodeletAdder(assistant));

		parser.addNodelet("/scany/server/end()", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				ServerRule sr = (ServerRule)assistant.popObject();
				assistant.setServerRule(sr);
			}
		});
	}

}
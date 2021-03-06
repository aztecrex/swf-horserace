/*
 * Licensed to Media Science International (MSI) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. MSI
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.msiops.demo.swf.horserace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.msiops.demo.swf.horserace.worker.RaceFlowClientExternal;
import com.msiops.demo.swf.horserace.worker.RaceFlowClientExternalFactory;
import com.msiops.demo.swf.horserace.worker.RaceFlowClientExternalFactoryImpl;

/**
 * Kick off a horse race. When run, this request the start of a new workflow in
 * Amazon AWS.
 *
 * @author greg wiley <aztec.rex@jammm.com>
 *
 */
public final class HorseRace {

	private static final RaceFlowClientExternalFactory CLIENTS;

	private static final String DOMAIN = "Demo";

	/**
	 * This is the actual client interface used by the generated external flow
	 * client. Its credentials can be configured like any other client in the
	 * AWS Java SDK. By default, it checks the environment, system properties,
	 * and (if running on EC2) the host role.
	 */
	private static final AmazonSimpleWorkflowClient SWF = new AmazonSimpleWorkflowClient();

	static {
		CLIENTS = new RaceFlowClientExternalFactoryImpl(SWF, DOMAIN);

	}

	public static void main(final String[] args) {
		final HorseRace hr;
		if (args.length == 0) {
			hr = new HorseRace(DEFAULT_HORSES, DEFAULT_LAPS);
		} else if (args.length == 1) {
			hr = new HorseRace(DEFAULT_HORSES, Integer.valueOf(args[0]));
		} else {
			hr = new HorseRace(Arrays.asList(Arrays.copyOfRange(args, 0,
					args.length - 1)), Integer.valueOf(args[args.length - 1]));
		}
		hr.go();

	}

	/**
	 * Client stub for requesting workflows from outside of FlowFramework
	 * enhanced code. FF code generation adds the "ClientExternal" suffix.
	 */
	private final RaceFlowClientExternal race = CLIENTS.getClient();

	/**
	 * Names of the horses running in the race. This becomes a parameter to the
	 * workflow creation.
	 */
	private final List<String> horses;


	/**
	 * Number of laps to run. This becomes a parameter to the workflow creation.
	 */
	private final int laps;

	/**
	 * Default horse names are taken from "Mr. Magoo and the Seven Dwarfs"
	 */
	private static final List<String> DEFAULT_HORSES = Arrays.asList("Axlerod",
			"Bartholomew", "Cornelius", "Dexter", "Eustace", "Ferdinand",
			"George");

	private static final int DEFAULT_LAPS = 10;

	public HorseRace(final Collection<String> horses, final int laps) {

		this.horses = new ArrayList<>(horses);
		this.laps = laps;

	}

	/**
	 * Start the race.
	 */
	public void go() {
		/*
		 * invoke FF SWF client stub. Note that the name of this method is taken
		 * from the interface method marked with @Execute.
		 * 
		 * Other interface methods can be marked with @Signal. Any such methods
		 * become methods in the client stub for signaling an active workflow.
		 */
		this.race.go(this.horses, this.laps);
	}
}

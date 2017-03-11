package com.arch.domainobjects;

import java.util.List;

public class Country {
List<State> states;

/**
 * @return the states
 */
public List<State> getStates() {
	return states;
}

/**
 * @param states the states to set
 */
public void setStates(List<State> states) {
	this.states = states;
}


}

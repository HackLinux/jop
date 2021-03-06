/*
  This file is part of JOP, the Java Optimized Processor
    see <http://www.jopdesign.com/>

  Copyright (C) 2008, Benedikt Huber (benedikt.huber@gmail.com)

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.jopdesign.wcet.ipet;

import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import com.jopdesign.dfa.framework.CallString;
import com.jopdesign.wcet.Project;
import com.jopdesign.wcet.analysis.WcetCost;
import com.jopdesign.wcet.frontend.ControlFlowGraph;
import com.jopdesign.wcet.frontend.SuperGraph;
import com.jopdesign.wcet.frontend.ControlFlowGraph.CFGEdge;
import com.jopdesign.wcet.frontend.ControlFlowGraph.CFGNode;
import com.jopdesign.wcet.frontend.SuperGraph.SuperInvokeEdge;
import com.jopdesign.wcet.frontend.SuperGraph.SuperReturnEdge;
import com.jopdesign.wcet.graphutils.LoopColoring;
import com.jopdesign.wcet.ipet.LinearConstraint.ConstraintType;

/**
 * Build an ILP model for determining the WCET. 
 * 
 * @author Benedikt Huber (benedikt.huber@gmail.com)
 */
public class ILPModelBuilder {
	/**
	 * Implementors provide cost for objects of type T
	 * @param <T>
	 */
	public interface CostProvider<T> {
		public long getCost(T obj);
	}
	
	public static class MapCostProvider<T> implements CostProvider<T> {
			private Map<T, Long> costMap;
			private long defCost;
			public MapCostProvider(Map<T,Long> costMap, long defCost) {
				this.costMap = costMap;
				this.defCost = defCost;
			}
			public long getCost(T obj) {
				Long cost = costMap.get(obj);
				if(cost == null) return defCost;
				else             return cost;
			}		
	}

	private IpetConfig ipetConfig;
	public ILPModelBuilder(IpetConfig ipetConfig) {
		this.ipetConfig = ipetConfig;
	}
	
	/**
	 * Create a max-cost maxflow problem for the given flow graph graph, based on a 
	 * given node to cost mapping.
	 * @param key a unique identifier for the problem (for reporting)
	 * @param callString context of the method invocation
	 * @param g the graph
	 * @param nodeWCET cost of nodes
	 * @return The max-cost maxflow problem
	 */
	public MaxCostFlow<CFGNode,CFGEdge> 
		buildLocalILPModel(String key, CallString callString, ControlFlowGraph g, CostProvider<CFGNode> nodeWCET) {
		Vector<FlowConstraint> flowCs = topLevelEntryExitConstraints(g);
		flowCs.addAll(loopBoundConstraints(g,callString));
		MaxCostFlow<CFGNode,CFGEdge> maxflow = 
			new MaxCostFlow<CFGNode,CFGEdge>(key,g.getGraph(),g.getEntry(),g.getExit());
		for(CFGNode n : g.getGraph().vertexSet()) {
			maxflow.setCost(n, nodeWCET.getCost(n));
		}
		for(FlowConstraint c : flowCs) maxflow.addFlowConstraint(c);
		maxflow.setDumpILP(ipetConfig.dumpIlp);
		return maxflow;
	}
	
	/**
	 * Create an interprocedural max-cost max-flow problem for the given flow graph 
	 * based on a given node to cost mapping.
	 * @param key      A unique identifier for the problem (for reporting)
	 * @param sg       The supergraph to build the ILP for
	 * @param nodeCosts Cost of nodes (or {@code null} if no cost is associated with nodes)
	 * @return The max-cost maxflow problem
	 */
	public MaxCostFlow<CFGNode,CFGEdge> 
		buildGlobalILPModel(String key, SuperGraph sg, CostProvider<CFGNode> nodeCosts) {
		ControlFlowGraph top = sg.getTopCFG();
		Vector<FlowConstraint> flowCs = topLevelEntryExitConstraints(top);
		for(ControlFlowGraph cfg : sg.getControlFlowGraphs()) {
			flowCs.addAll(loopBoundConstraints(cfg));
		}
		for(Entry<SuperInvokeEdge,SuperReturnEdge> superEdgePair : sg.getSuperEdgePairs().entrySet()) {
			flowCs.add(superEdgeConstraint(superEdgePair.getKey(), superEdgePair.getValue()));
		}
		MaxCostFlow<CFGNode,CFGEdge> maxflow = 
			new MaxCostFlow<CFGNode,CFGEdge>(key,sg,sg.getTopEntry(),sg.getTopExit());
		
		if(nodeCosts == null) {
			nodeCosts = new CostProvider<CFGNode>() {
				public long getCost(CFGNode obj) {
					return 0;
				}			
			};
		}
		for(CFGNode n : sg.vertexSet()) {
			maxflow.setCost(n, nodeCosts.getCost(n));
		}

		for(FlowConstraint c : flowCs) maxflow.addFlowConstraint(c);
		maxflow.setDumpILP(ipetConfig.dumpIlp);
		return maxflow;
	}
	
	private FlowConstraint superEdgeConstraint(SuperInvokeEdge in, SuperReturnEdge out) {
		FlowConstraint pairConstraint = new FlowConstraint(ConstraintType.Equal);
		pairConstraint.addLHS(in);
		pairConstraint.addRHS(out);
		return pairConstraint;		
	}
	
	/*
	 * Top level flow constraints: flow out of entry and flow into exit should be 1
	 */
	private Vector<FlowConstraint> topLevelEntryExitConstraints(ControlFlowGraph g) {
		Vector<FlowConstraint> constraints = new Vector<FlowConstraint>();
		// sum (e_Entry_x) = 1		
		FlowConstraint entryConstraint = new FlowConstraint(ConstraintType.Equal);
		entryConstraint.addRHS(1);
		for(CFGEdge entryEdge : g.getGraph().outgoingEdgesOf(g.getEntry())) {
			entryConstraint.addLHS(entryEdge);
		}
		constraints.add(entryConstraint);
		// sum (e_y_Exit) = 1
		FlowConstraint exitConstraint = new FlowConstraint(ConstraintType.Equal);
		exitConstraint.addRHS(1);
		for(CFGEdge exitEdge : g.getGraph().incomingEdgesOf(g.getExit())) {
			exitConstraint.addLHS(exitEdge);
		}
		constraints.add(exitConstraint);
		return constraints;
	}
	
	/*
	 * Compute flow constraints: Loop Bound constraints
	 * @param g the flow graph
	 * @return A list of flow constraints
	 */
	private Vector<FlowConstraint> loopBoundConstraints(ControlFlowGraph g) {
		return loopBoundConstraints(g,CallString.EMPTY);
	}
	/*
	 * Compute flow constraints: Loop Bound constraints
	 * @param g the flow graph
	 * @param cs the invocation context
	 * @return A list of flow constraints
	 */
	private Vector<FlowConstraint> loopBoundConstraints(ControlFlowGraph g, CallString cs) {
		Vector<FlowConstraint> constraints = new Vector<FlowConstraint>();
		// - for each loop with bound B
		// -- sum(exit_loop_edges) * B <= sum(continue_loop_edges)
		LoopColoring<CFGNode, CFGEdge> loops = g.getLoopColoring();
		for(CFGNode hol : loops.getHeadOfLoops()) {
			FlowConstraint loopConstraint = new FlowConstraint(ConstraintType.Equal);
			if(g.getLoopBounds().get(hol) == null) {
				throw new Error("No loop bound record for head of loop: "+hol+
								" : "+g.getLoopBounds());
			}
			int lhsMultiplicity = g.getLoopBound(hol,cs).getUpperBound();
			for(CFGEdge exitEdge : loops.getExitEdgesOf(hol)) {
				loopConstraint.addLHS(exitEdge,lhsMultiplicity);
			}
			for(CFGEdge continueEdge : loops.getBackEdgesTo(hol)) {
				loopConstraint.addRHS(continueEdge);
			}
			constraints.add(loopConstraint);
		}
		return constraints;
	}
}

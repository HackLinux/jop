/*
  This file is part of JOP, the Java Optimized Processor
    see <http://www.jopdesign.com/>

  Copyright (C) 2008, Wolfgang Puffitsch

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

package com.jopdesign.dfa.framework;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;

public class Interpreter<K, V> {

	private Analysis<K, V> analysis;
	private DFAAppInfo program;
	
	public Interpreter(Analysis<K, V> a, DFAAppInfo p) {
		program = p;
		analysis = a;
	}
	
	public DFAAppInfo getProgram() {
		return program;
	}
	
	public Map<InstructionHandle, ContextMap<K, V>> interpret(Context context,
			InstructionHandle entry,
			Map<InstructionHandle, ContextMap<K, V>> state,
			boolean start) {
		
		LinkedList<FlowEdge> worklist = new LinkedList<FlowEdge>();

		for (Iterator<FlowEdge> i = program.getFlow().getOutEdges(entry).iterator(); i.hasNext(); ) {
			FlowEdge f = i.next();
			if (entry.equals(f.getTail())) {
				worklist.add(new FlowEdge(f, context));
			}
 		}

		Map<InstructionHandle, ContextMap<K, V>> result = state;
		
		if (start) {
			for (Iterator<InstructionHandle> i = program.getStatements().iterator(); i.hasNext(); ) {
				InstructionHandle s = i.next();
				result.put(s, analysis.bottom());
			}		
			result.put(entry, analysis.initial(entry));
		}
		
		while(!worklist.isEmpty()) {
			
			FlowEdge edge = worklist.removeFirst();
			//System.out.println("computing: "+edge);
			InstructionHandle tail = edge.getTail();
			InstructionHandle head = edge.getHead();

			ContextMap<K, V> tailSet = result.get(tail);
			tailSet.setContext(edge.getContext()); 
			ContextMap<K, V> transferred = analysis.transfer(tail, edge, tailSet, this, result);
			ContextMap<K, V> headSet = result.get(head);

			if (!analysis.compare(transferred, headSet)) {
				
				ContextMap<K, V> joinedSet = analysis.join(headSet, transferred);				
				result.put(head, joinedSet);
				
				Set<FlowEdge> outEdges = program.getFlow().getOutEdges(head);
				if (outEdges != null) {
					for (Iterator<FlowEdge> i = outEdges.iterator(); i.hasNext(); ) {
						FlowEdge f = new FlowEdge(i.next(), transferred.getContext());
						if (worklist.isEmpty() || !worklist.getFirst().equals(f)) {
							worklist.addFirst(f);
							//System.out.println("pushing: "+f);
						}
					}
				}
			}
			
			//System.out.println("worklist: "+worklist);
		}

		return result;
	}
	
}

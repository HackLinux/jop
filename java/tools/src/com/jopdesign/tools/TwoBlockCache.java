
/**
*
*	Simulation of simple cache (as in JOP)
*
*/

package com.jopdesign.tools;

public class TwoBlockCache extends Cache {

	int[] addr = {0, 0};
	int next = 0;
	int currentBlock = 0;

	TwoBlockCache(int[] main, JopSim js) {

		mem = main;
		sim = js;
	}


	int corrPc(int pc) {

		// save block relative pc on invoke
		return pc & (MAX_BC_MASK>>1);
	}

	// TODO use both blocks for methods larger than 512 bytes
	int invoke(int start, int len) {

		if (len*4 > (MAX_BC>>1)) {
			System.out.println("block too large");
			System.exit(0);
		}
		int off = testCache(start, len);
		return off;
	}

	int ret(int start, int len, int pc) {

		int off = testCache(start, len);
		return off+pc;
	}

	int testCache(int start, int len) {

		if (start==addr[0]) {
			currentBlock = 0;
			return 0;
		} else if (start==addr[1]) {
			currentBlock = 1;
			return (MAX_BC>>1);
		}

		if (currentBlock==0) {
			next = 1;
		} else {
			next = 0;
		}
		currentBlock = next;
		int off = 0; 
		if (next!=0) {
			off = MAX_BC>>1;
		}
		addr[next] = start;
		loadBc(off, start, len);
		return off;
	}

	void loadBc(int off, int start, int len) {

// high byte of word is first bc!!!
		for (int i=0; i<len; ++i) {
			int val = sim.readInstrMem(start+i);
			for (int j=0; j<4; ++j) {
				bc[off+i*4+(3-j)] = (byte) val;
				val >>>= 8;
			}
		}

		memRead += len*4;
		memTrans++;
	}

	byte bc(int addr) {
		++cacheRead;
		return bc[addr];
	}


}

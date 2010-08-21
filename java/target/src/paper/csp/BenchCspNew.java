/*
  This file is part of JOP, the Java Optimized Processor
    see <http://www.jopdesign.com/>

  Copyright (C) 2010, Martin Schoeberl (martin@jopdesign.com)

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
package csp;

import java.util.Vector;

import util.Timer;

import joprt.RtThread;

import com.jopdesign.io.IOFactory;
import com.jopdesign.io.SysDevice;
import com.jopdesign.sys.Native;
import com.jopdesign.sys.Startup;

/**
 * @author martin
 *
 */
public class BenchCspNew implements Runnable {
	
		final static int CNT = 100;
		
		int id;
		
		static Vector msg;

		public BenchCspNew(int i) {
			id = i;
		}

		/**
		 * @param args
		 */
		public static void main(String[] args) {

			msg = new Vector();
			
			System.out.println("Hello World from CPU 0");
			System.out.print("Status: ");
			System.out.println(Native.rd(NoC.NOC_REG_STATUS));


			
			SysDevice sys = IOFactory.getFactory().getSysDevice();
//			for (int i=0; i<sys.nrCpu-1; ++i) {
//				Runnable r = new Main(i+1);
//				Startup.setRunnable(r, i);
//			}
			Runnable r = new BenchCsp(1);
			Startup.setRunnable(r, 0);
//			Startup.setRunnable(new BenchCsp(2), 1);
			
			// start the other CPUs
			sys.signal = 1;
			// set the WD LED for the simulation
			sys.wd = 1;
			
			int[] buffer = new int[10];
			int i=0;
			// print their messages
			for (;;) {
// no need for this, b_receive is blocking
//				if (!NoC.isReceiveBufferEmpty()) {
//				if (NoC.isReceiving()) {
					int val = NoC.b_receive1();
					System.out.print(" Received ");
					System.out.print(val);
//				}
				RtThread.sleepMs(10);
//				System.out.print("Status: ");
//				System.out.println(Native.rd(NoC.NOC_REG_STATUS));
				++i;
//				if (i==10) {
//					System.out.println("Send something to core 2");
//					NoC.nb_send1(2, 'x');
//				}

			}
		}

		public void run() {
			
			for (int i=0; i<CNT; ++i) {
				// should be inlined
//				while(NoC.isSendBufferFull()) {
//					;
//				}
// nb_send1 can only happen if there is no current message in sending
				while(NoC.isSending()) {
					// nop
				}
				NoC.nb_send1(0, i);
				RtThread.sleepMs(5);
			}
		}

	}


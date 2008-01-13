package com.jopdesign.tools;

import com.jopdesign.sys.Const;

//uncomment for usage of the PCs com port
//import javax.comm.CommPortIdentifier;
//import javax.comm.SerialPort;
//import javax.comm.UnsupportedCommOperationException;

/**
 * Simulation of the minimal IO devices
 * @author martin
 *
 */
public class IOSimMin {
	
	JopSim js;
	boolean log;
	//	find JVM exit
	static String exitStr = "JVM exit!";
	char[] exitBuf = new char[exitStr.length()];

	public IOSimMin() {
		log = System.getProperty("log", "false").equals("true");
	}
	
	public void setJopSimRef(JopSim jsRef) {
		js = jsRef;		
	}

	//
	//	Mapping of the second serial line to the PCs
	//	com port. See ejip.MainSlipUart2 for an example.
	//	Uncommented as javax.comm is NOT part of the standard
	//	JDK - Blame Sun!
	
//	private String portName;
//	private CommPortIdentifier portId;
//	private InputStream is = null;
//	private OutputStream os = null;
//	private SerialPort serialPort;
//
//	private void openSerialPort() {
//		try {
//			if (portId!=null) {
//				try {
//					is.close();
//					os.close();
//					is = null;
//					os = null;
//				} catch (Exception e1) {
//				}
//				serialPort.close();
//			}
//			portId = CommPortIdentifier.getPortIdentifier(portName);
//			serialPort = (SerialPort) portId.open(getClass().toString(), 2000);
//			serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_OUT
//										| SerialPort.FLOWCONTROL_RTSCTS_IN);
//			serialPort.setSerialPortParams(115200,
//				SerialPort.DATABITS_8,
//				SerialPort.STOPBITS_1,
//				SerialPort.PARITY_NONE);
//			is = serialPort.getInputStream();
//			os = serialPort.getOutputStream();
//System.out.println("open"+portName);
//		} catch (Exception e) {
//			is = null;
//			os = null;
//			System.out.println("Problem with serial port "+portName);
//			System.out.println(e.getMessage());
//			// System.exit(-1);
//		}
//	}
	
	int read(int addr) {

		int val;
		int i;

		try {
			switch (addr) {
				case Const.IO_STATUS:
					val = Const.MSK_UA_TDRE;
					if (System.in.available()!=0) {
						val |= Const.MSK_UA_RDRF;
					}
					break;
				case Const.IO_STATUS2:
					i = 0;
//					if (is!=null) {
//						try {
//							if (is.available()!=0) {
//								i |= Const.MSK_UA_RDRF;
//							}
//						} catch (IOException e1) {
//							e1.printStackTrace();
//						}	// rdrf
//					}
//					i |= Const.MSK_UA_TDRE;							// tdre is alwais true on OutputStream
					val = i;
					break;
				case Const.IO_UART:
					if (System.in.available()!=0) {
						val = System.in.read();
					} else {
						val = '_';
					}
					break;
				case Const.IO_UART2:
					i=0;
//					try {
//						i =  is.read();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
					val = i;
					break;
				case Const.IO_CNT:
					val = js.clkCnt;
					break;
				case Const.IO_US_CNT:
					val = JopSim.usCnt();
					break;
				case 1234:
					// trigger cache debug output
//					cache.rawData();
//					cache.resetCnt();
					val = 0;
					break;
				default:
					val = 0;
			}
		} catch (Exception e) {
			System.out.println(e);
			val = 0;
		}
		
		return val;
	}

	void write(int addr, int val) {

		switch (addr) {
			case Const.IO_UART:
				if (log) System.out.print("\t->");
				System.out.print((char) val);
				if (log) System.out.println("<-");
				// check the output for JVM exit!
				for (int i=0; i<exitStr.length()-1; ++i) {
					exitBuf[i] = exitBuf[i+1];
				}
				exitBuf[exitBuf.length-1] = (char) val;
				if (new String(exitBuf).equals(exitStr)) {
					JopSim.exit();
				}
				break;
			case Const.IO_UART2:
//				if (os==null) return;
//				try {
//					os.write(val&0xff);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				break;
			case Const.IO_STATUS2:
//				if (serialPort!=null) {
//					serialPort.setDTR(val==1);
//					try {
//						if ((val&0x04)==0) {
//							serialPort.setSerialPortParams(2400,
//								SerialPort.DATABITS_8,
//								SerialPort.STOPBITS_1,
//								SerialPort.PARITY_NONE);
//						} else {
//							serialPort.setSerialPortParams(115200,
//								SerialPort.DATABITS_8,
//								SerialPort.STOPBITS_1,
//								SerialPort.PARITY_NONE);
//						}
//						if ((val&0x02)==0) {
//							serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
//						} else {
//							serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_OUT
//														| SerialPort.FLOWCONTROL_RTSCTS_IN);
//						}
//					} catch (UnsupportedCommOperationException e1) {
//						e1.printStackTrace();
//					}
//				}
				break;
				
			case Const.IO_INT_ENA:
				JopSim.intEna = (val==0) ? false : true;
				break;
			case Const.IO_TIMER:
				JopSim.intPend = false;		// reset pending interrupt
				JopSim.interrupt = false;		// for shure ???
				JopSim.nextTimerInt = val;
				break;
			case Const.IO_SWINT:
				if (!JopSim.intPend) {
					JopSim.interrupt = true;
					JopSim.intPend = true;
				}
				break;
			default:
		}
	}

}

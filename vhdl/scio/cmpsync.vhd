--
--	cmpsync.vhd
--
--	Author: CP
--
--  2007-11-22  Global lock synchronization and bootup of CMP
--  2007-12-07  redesign of global lock synchronization
--  2007-12-19  included new lock arbitration in state "locked".
--							Otherwise wrong output if two CPUs are waiting for lock!
--  2008-02-20	Removed exit from the loop logic by inverting the range of the loop


library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;
use work.jop_types.all;

entity cmpsync is
generic (
		cpu_cnt : integer
	);
port (
	clk, reset		 : in std_logic;		
	sync_in_array  : in sync_in_array_type(0 to cpu_cnt-1);
	sync_out_array : out sync_out_array_type(0 to cpu_cnt-1)
);
end cmpsync;


architecture rtl of cmpsync is

type state_type is (idle, locked);
signal state : state_type;
signal next_state : state_type;

signal locked_id	: integer;
signal next_locked_id : integer;


begin

  -- save who has the lock, next_state of fsm
  process(sync_in_array, state, locked_id)	 
  	begin
  
  		next_state <= state;
  		next_locked_id <= locked_id;
  	
  		case state is
  			when idle =>
				next_locked_id <= cpu_cnt; -- means no CPU_ID is locked
				
  				-- the smallest CPU number wins when multiple lock requests  
  				for i in cpu_cnt-1 downto 0 loop
  					if sync_in_array(i).lock_req = '1' then 
  						next_state <= locked;
  						next_locked_id <= i;
  					end if;
  				end loop;
  				
  			when locked =>
  				-- CPU frees the lock
  				if sync_in_array(locked_id).lock_req = '0' then
  					next_state <= idle;
  					next_locked_id <= cpu_cnt;
						
						-- new lock request
						for i in cpu_cnt-1 downto 0 loop
							if sync_in_array(i).lock_req = '1' then 
								next_state <= locked;
								next_locked_id <= i;
							end if;
						end loop;
  				end if;
  		end case;
  	end process;
  	
  -- generates the FSM state
  	process (clk, reset)
  	begin
  		if (reset = '1') then
  			state <= idle;
  			locked_id <= cpu_cnt; -- initially no id is locked
    	elsif (rising_edge(clk)) then
  			state <= next_state;
  			locked_id <= next_locked_id;	
  		end if;
  	end process;
  	
  -- output
  process (next_state, next_locked_id, sync_in_array)
  begin

	for i in 0 to cpu_cnt-1 loop
		sync_out_array(i).s_out <= sync_in_array(0).s_in;  -- Bootup
	end loop;
	
  	case next_state is
  		when idle =>
  			for i in 0 to cpu_cnt-1 loop
  				sync_out_array(i).halted <= '0';
  			end loop;
  			
  		when locked =>
  			for i in 0 to cpu_cnt-1 loop
  				if (i = next_locked_id) then
  					sync_out_array(i).halted <= '0';
  				else
  					sync_out_array(i).halted <= '1';
  				end if;
  			end loop;	
  	end case;
  end process;

end rtl;






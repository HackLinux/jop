--
--	cnt.vhd
--
--	counter, interrrupt handling and watchdog bit
--
--	Author: Martin Schoeberl	martin@good-ear.com
--
--		address map:
--
--			0	read clk counter, write irq ena
--			1	read 1 MHz counter, write timer val (us) + irq ack
--			2	write generates sw-int (for yield())
--			3	write wd port
--
--	todo:
--
--
--	2003-07-05	new IO standard
--	2003-08-15	us counter, irq added
--

library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity cnt is

generic (io_addr : integer; clk_freq : integer);
port (
	clk		: in std_logic;
	reset	: in std_logic;
	addr	: in std_logic_vector(3 downto 0);
	din		: in std_logic_vector(31 downto 0);
	wr		: in std_logic;
	dout	: out std_logic_vector(31 downto 0);
	rd		: in std_logic;

	irq		: out std_logic;
	irq_ena	: out std_logic;

	wd		: out std_logic
);
end cnt ;

architecture rtl of cnt is

	signal clock_cnt		: std_logic_vector(31 downto 0);
	signal pre_scale		: std_logic_vector(7 downto 0);
	signal us_cnt			: std_logic_vector(31 downto 0);

	constant div_val	: integer := clk_freq/1000000-1;

	signal timer_int		: std_logic;
	signal yield_int		: std_logic;
	signal int_ack			: std_logic;

	signal timer			: std_logic;
	signal yield			: std_logic;

	signal irq_cnt			: std_logic_vector(31 downto 0);
	signal timer_equ		: std_logic;
	signal timer_dly		: std_logic;

begin

--
--	read cnt values
--
process(addr, rd, clock_cnt, us_cnt)

begin
--	if addr=std_logic_vector(to_unsigned(io_addr, 4)) then
--		dout <= clock_cnt;
--	elsif addr=std_logic_vector(to_unsigned(io_addr+1, 4)) then
if addr=std_logic_vector(to_unsigned(io_addr+1, 4)) then
		dout <= us_cnt;
	else
		dout <= (others => 'Z');
	end if;

end process;

--
--	compare timer value and us counter
--	and generate single shot
--
process(us_cnt, irq_cnt) begin
	timer_equ <= '0';
	if us_cnt = irq_cnt then
		timer_equ <= '1';
	end if;
end process;

process(clk, reset, timer_equ) begin
	if (reset='1') then
		timer_dly <= '0';
	elsif rising_edge(clk) then
		timer_dly <= timer_equ;
	end if;
end process;

	timer_int <= timer_equ and not timer_dly;

--
--	int processing from timer and yield request
--
process(clk, reset, timer_int, yield_int) begin

	if (reset='1') then
		timer <= '0';
		yield <= '0';
	elsif rising_edge(clk) then
		if int_ack='1' then
			timer <= '0';
			yield <= '0';
		else
			if timer_int='1' then
				timer <= '1';
			end if;
			if yield_int='1' then
				yield <= '1';
			end if;
		end if;
	end if;

end process;

	irq <= timer or yield;

--
--	counters
--		pre_scale is 8 bit => fmax = 255 MHz
--
process(clk, reset) begin

	if (reset='1') then

		clock_cnt <= (others => '0');
		us_cnt <= (others => '0');
		pre_scale <= std_logic_vector(to_unsigned(div_val, pre_scale'length));

	elsif rising_edge(clk) then

		clock_cnt <= std_logic_vector(unsigned(clock_cnt) + 1);
		pre_scale <= std_logic_vector(unsigned(pre_scale) - 1);
		if pre_scale = "00000000" then
			pre_scale <= std_logic_vector(to_unsigned(div_val, pre_scale'length));
			us_cnt <= std_logic_vector(unsigned(us_cnt) + 1);
		end if;

	end if;
end process;

--
--	io write processing
--
process(clk, reset, wr, addr, din)

begin
	if (reset='1') then

		irq_ena <= '0';
		irq_cnt <= (others => '0');
		int_ack <= '0';
		wd <= '0';

	elsif rising_edge(clk) then

		int_ack <= '0';
		yield_int <= '0';

		if addr=std_logic_vector(to_unsigned(io_addr, 4)) and wr='1' then
			irq_ena <= din(0);
		elsif addr=std_logic_vector(to_unsigned(io_addr+1, 4)) and wr='1' then
			irq_cnt <= din;
			int_ack <= '1';
		elsif addr=std_logic_vector(to_unsigned(io_addr+2, 4)) and wr='1' then
			yield_int <= '1';
		elsif addr=std_logic_vector(to_unsigned(io_addr+3, 4)) and wr='1' then
			wd <= din(0);
		end if;

	end if;
end process;

end rtl;

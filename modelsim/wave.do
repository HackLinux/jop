onerror {resume}
quietly WaveActivateNextPane {} 0
add wave -noupdate -format Logic -radix hexadecimal /tb_jop/joptop/clk_int
add wave -noupdate -format Logic -radix hexadecimal /tb_jop/joptop/int_res
add wave -noupdate -format Logic /tb_jop/joptop/io/wd
add wave -noupdate -format Literal -radix ascii /tb_jop/joptop/io/ua/char
add wave -noupdate -divider {java pc}
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/core/bcf/jpc
add wave -noupdate -divider bcfetch
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/core/bcf/jinstr
add wave -noupdate -format Literal /tb_jop/joptop/cpu/core/bcf/bc/val
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/core/bcf/opd
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/core/fch/pc
add wave -noupdate -format Logic -radix hexadecimal /tb_jop/joptop/cpu/core/fch/nxt
add wave -noupdate -divider fetch
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/core/fch/ir
add wave -noupdate -format Literal /tb_jop/joptop/cpu/core/fch/uc/val
add wave -noupdate -divider decode
add wave -noupdate -format Logic /tb_jop/joptop/cpu/core/dec/br
add wave -noupdate -format Logic /tb_jop/joptop/cpu/core/dec/jmp
add wave -noupdate -format Logic /tb_jop/joptop/cpu/core/dec/jbr
add wave -noupdate -format Logic /tb_jop/joptop/cpu/core/dec/ena_a
add wave -noupdate -format Logic -radix hexadecimal /tb_jop/joptop/cpu/core/fch/bsy
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/core/stk/sp
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/core/stk/vp0
add wave -noupdate -divider execute
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/core/stk/a
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/core/stk/b
add wave -noupdate -divider mem_sc
add wave -noupdate -format Literal -radix hexadecimal -expand /tb_jop/joptop/cpu/mem/mem_in
add wave -noupdate -format Literal -radix hexadecimal -expand /tb_jop/joptop/cpu/mem/mem_out
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/mem/state
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/mem/addr_reg
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/mem/index
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/mem/value
add wave -noupdate -format Logic /tb_jop/joptop/cpu/mem/was_a_store
add wave -noupdate -format Logic /tb_jop/joptop/cpu/mem/was_a_stidx
add wave -noupdate -format Logic /tb_jop/joptop/cpu/mem/was_a_hwo
add wave -noupdate -format Logic /tb_jop/joptop/cpu/mem/read_ocache
add wave -noupdate -format Logic -radix hexadecimal /tb_jop/joptop/cpu/mem/null_pointer
add wave -noupdate -format Logic -radix hexadecimal /tb_jop/joptop/cpu/mem/bounds_error
add wave -noupdate -format Logic /tb_jop/joptop/cpu/mem/np_exc
add wave -noupdate -format Logic /tb_jop/joptop/cpu/mem/ab_exc
add wave -noupdate -format Literal -radix hexadecimal -expand /tb_jop/joptop/cpu/mem/sc_mem_out
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/mem/sc_mem_in
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/main_mem/data
add wave -noupdate -divider ocache
add wave -noupdate -format Literal -radix hexadecimal -expand /tb_jop/joptop/cpu/mem/oc/ocin
add wave -noupdate -format Literal -radix hexadecimal -expand /tb_jop/joptop/cpu/mem/oc/ocout
add wave -noupdate -format Logic /tb_jop/joptop/cpu/mem/oc/hit_reg
add wave -noupdate -format Literal -radix hexadecimal -expand /tb_jop/joptop/cpu/mem/oc/ocin_reg
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/mem/oc/tag
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/mem/oc/index
add wave -noupdate -format Logic -radix hexadecimal /tb_jop/joptop/cpu/mem/oc/valid
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/mem/oc/data
add wave -noupdate -divider SimpCon
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/cpu/sc_mem_out
add wave -noupdate -format Literal -radix hexadecimal -expand /tb_jop/joptop/cpu/sc_mem_in
add wave -noupdate -divider {external signals}
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/main_mem/addr
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/main_mem/data
add wave -noupdate -format Logic /tb_jop/main_mem/ncs
add wave -noupdate -format Logic /tb_jop/main_mem/noe
add wave -noupdate -format Logic /tb_jop/main_mem/nwr
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/fl_a
add wave -noupdate -format Literal -radix hexadecimal /tb_jop/joptop/fl_d
add wave -noupdate -format Logic /tb_jop/joptop/fl_ncs
add wave -noupdate -format Logic /tb_jop/joptop/fl_ncsb
add wave -noupdate -format Logic /tb_jop/joptop/fl_noe
add wave -noupdate -format Logic /tb_jop/joptop/fl_nwe
add wave -noupdate -format Logic /tb_jop/joptop/fl_rdy
TreeUpdate [SetDefaultTree]
WaveRestoreCursors {{Cursor 1} {5200699804 ps} 0}
configure wave -namecolwidth 228
configure wave -valuecolwidth 144
configure wave -justifyvalue left
configure wave -signalnamewidth 0
configure wave -snapdistance 10
configure wave -datasetprefix 0
configure wave -rowmargin 4
configure wave -childrowmargin 2
configure wave -gridoffset 0
configure wave -gridperiod 1
configure wave -griddelta 40
configure wave -timeline 0
configure wave -timelineunits ps
update
WaveRestoreZoom {5200640975 ps} {5200648479 ps}

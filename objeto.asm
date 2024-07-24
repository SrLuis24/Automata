section .data
const_1 dq 0x3ff199999999999a ; 1.1
const_2 dq 0x4024333333333333 ; 10.1
const_3 dq 0x400199999999999a ; 2.2
const_4 dq 0x3ff0000000000000 ; 1.0
t4 dd 0
a dq 0
t5 dd 0
b dq 0
t6 dd 0
c dq 0
t7 dd 0
d dd 0
t8 dd 0
t9 dd 0
r dd 0
t10 dq 0
t11 dq 0
x dd 0
y dq 0
t1 dq 0
t2 dd 0
t3 dd 0
section .text
global _start
_start:
movsd xmm0, qword [const_1]
movsd xmm1, qword [const_1]
addsd xmm0, xmm1
movsd qword [t1], xmm0
movsd xmm1, qword [const_2]
movsd xmm2, qword [const_1]
ucomisd xmm1, xmm2
setne byte [t2]
cmp byte [t2], 0
je L1
mov eax, 2
add eax, 1
mov [t3], eax
mov eax, t3
add eax, 1
mov [t4], eax
mov eax, 4
mov ebx, t4
cdq
idiv ebx
mov [t5], eax
mov eax, t5
imul eax, 2
mov [t6], eax
mov eax, 1
sub eax, 1
mov [t7], eax
mov eax, t7
add eax, r
mov [t8], eax
cmp byte [t9], 0
je L2
movsd xmm0, qword [const_3]
movsd xmm1, qword [const_1]
addsd xmm0, xmm1
movsd qword [t10], xmm0
jmp L3
L2:
movsd xmm0, qword [const_3]
movsd xmm1, qword [const_4]
mulsd xmm0, xmm1
movsd qword [t11], xmm0
L3:
L1:
mov rax, 60
xor rdi, rdi
syscall

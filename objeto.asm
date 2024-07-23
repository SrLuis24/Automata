section .data
const_1 dq 0x400199999999999a
const_2 dd 2
const_3 dq 0x400a6666813e5901
const_4 dq 0x4000cccccccccccd
a dq 0
b dq 0
r dd 0
c dq 0
d dd 0
x dd 0
y dq 0
t1 dd 0
t2 dd 0
t3 dd 0
section .text
global _start
_start:
movsd xmm0, qword [const_1]
movsd qword [a], xmm0
mov eax, 10
cmp eax, 1
setg byte [t1]
cmp byte [t1], 0
je L1
mov dword [x], 2
mov eax, 2
add eax, [r]
mov [t2], eax
mov dword [x], eax
mov eax, 1
cmp eax, 2
setg byte [t3]
cmp byte [t3], 0
je L2
movsd a, const_3
jmp L3
L2
movsd xmm0, qword [const_4]
movsd qword [c], xmm0
L3
L1
mov rax, 60
xor rdi, rdi
syscall

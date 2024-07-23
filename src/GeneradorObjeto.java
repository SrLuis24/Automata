import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class GeneradorObjeto {
    private List<Cuadruplo> quadruples;
    private StringBuilder asmCode;
    private Set<String> tempVars;

    public GeneradorObjeto(List<Cuadruplo> quadruples) {
        this.quadruples = quadruples;
        this.asmCode = new StringBuilder();
        this.tempVars = new HashSet<>();
    }

    public String generate() {
        generateDataSection();
        generateTextSection();
        return asmCode.toString();
    }

    private void generateDataSection() {
        asmCode.append("section .data\n");
        for (Cuadruplo quad : quadruples) {
            if (quad.operador.equals("Real") || quad.operador.equals("Int")) {
                String type = quad.operador;
                String name = quad.operando1;
                if (type.equals("Real")) {
                    asmCode.append(name).append(" dq 0.0\n");
                } else if (type.equals("Int")) {
                    asmCode.append(name).append(" dd 0\n");
                }
            } else {
                // Identificar variables temporales
                if (quad.resultado != null && quad.resultado.startsWith("t")) {
                    tempVars.add(quad.resultado);
                }
            }
        }
        for (String tempVar : tempVars) {
            asmCode.append(tempVar).append(" db 0\n");
        }
        asmCode.append("\n");
    }

    private void generateTextSection() {
        asmCode.append("section .text\n");
        asmCode.append("global _start\n");
        asmCode.append("_start:\n");

        Map<String, String> labels = new HashMap<>();
        int labelCount = 0;

        // Crear una lista de cuádruplos con etiquetas para etiquetarlas
        List<Cuadruplo> labeledQuadruples = new ArrayList<>();
        for (Cuadruplo quad : quadruples) {
            if (quad.operador.startsWith("L")) {
                labels.put(quad.operador, "L" + labelCount++);
            }
            labeledQuadruples.add(quad);
        }

        // Generar el código para cada cuádruplo
        for (Cuadruplo quad : labeledQuadruples) {
            switch (quad.operador) {
                case "Assign":
                    generateAssign(quad);
                    break;
                case "+":
                    generateAdd(quad);
                    break;
                case "*":
                    generateMul(quad);
                    break;
                case ">":
                    generateComparison(quad, "setg");
                    break;
                case "IF_FALSE":
                    generateIfFalse(quad, labels);
                    break;
                case "GOTO":
                    generateGoto(quad, labels);
                    break;
                default:
                    if (labels.containsKey(quad.operador)) {
                        asmCode.append(quad.operador).append(":\n");
                    }
            }
        }

        asmCode.append("mov rax, 60\n");
        asmCode.append("xor rdi, rdi\n");
        asmCode.append("syscall\n");
    }

    private void generateAssign(Cuadruplo quad) {
        if (quad.operando1 != null) {
            String type = getTypeFromQuad(quad.resultado);
            if (type.equals("Real")) {
                asmCode.append("mov rax, ").append(quad.operando1).append("\n");
                asmCode.append("mov [").append(quad.resultado).append("], rax\n");
            } else if (type.equals("Int")) {
                asmCode.append("mov dword [").append(quad.resultado).append("], ").append(quad.operando1).append("\n");
            }
        }
    }

    private void generateAdd(Cuadruplo quad) {
        String type = getTypeFromQuad(quad.resultado);
        if (type.equals("Real")) {
            asmCode.append("fld qword [").append(quad.operando1).append("]\n");
            asmCode.append("fadd qword [").append(quad.operando2).append("]\n");
            asmCode.append("fstp qword [").append(quad.resultado).append("]\n");
        } else if (type.equals("Int")) {
            asmCode.append("mov eax, [").append(quad.operando1).append("]\n");
            asmCode.append("add eax, [").append(quad.operando2).append("]\n");
            asmCode.append("mov [").append(quad.resultado).append("], eax\n");
        }
    }

    private void generateMul(Cuadruplo quad) {
        String type = getTypeFromQuad(quad.resultado);
        if (type.equals("Real")) {
            asmCode.append("fld qword [").append(quad.operando1).append("]\n");
            asmCode.append("fmul qword [").append(quad.operando2).append("]\n");
            asmCode.append("fstp qword [").append(quad.resultado).append("]\n");
        } else if (type.equals("Int")) {
            asmCode.append("mov eax, [").append(quad.operando1).append("]\n");
            asmCode.append("imul eax, [").append(quad.operando2).append("]\n");
            asmCode.append("mov [").append(quad.resultado).append("], eax\n");
        }
    }

    private void generateComparison(Cuadruplo quad, String setInstruction) {
        asmCode.append("mov eax, [").append(quad.operando1).append("]\n");
        asmCode.append("cmp eax, [").append(quad.operando2).append("]\n");
        asmCode.append(setInstruction).append(" byte [").append(quad.resultado).append("]\n");
    }

    private void generateIfFalse(Cuadruplo quad, Map<String, String> labels) {
        asmCode.append("cmp byte [").append(quad.operando1).append("], 0\n");
        asmCode.append("je ").append(quad.resultado).append("\n");
    }

    private void generateGoto(Cuadruplo quad, Map<String, String> labels) {
        asmCode.append("jmp ").append(quad.resultado).append("\n");
    }

    private String getTypeFromQuad(String variable) {
        for (Cuadruplo quad : quadruples) {
            if (quad.operador.equals("Real") || quad.operador.equals("Int")) {
                if (quad.operando1.equals(variable)) {
                    return quad.operador;
                }
            }
        }
        return "unknown";
    }

    public void writeToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(asmCode.toString());
        }
    }
}

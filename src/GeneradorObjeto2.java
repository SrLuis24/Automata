import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneradorObjeto2 {

    private static Map<String, String> variableTypes = new HashMap<>();
    private static List<String> temporaries = new ArrayList<>();
    private static List<String> constants = new ArrayList<>();

    public static void determineVariableTypes(List<Cuadruplo> quadruples) {
        for (Cuadruplo quad : quadruples) {
            if (quad.operador.equals("Real") || quad.operador.equals("Int")) {
                variableTypes.put(quad.operando1, quad.operador);
            } else if (quad.resultado != null && quad.resultado.startsWith("t")) {
                // Determine the type of temporary variable based on the operation
                if (quad.operador.matches("[+\\-*/]") || quad.operador.equals("Assign")) {
                    if (quad.operando1.contains(".") || (quad.operando2 != null && quad.operando2.contains("."))) {
                        variableTypes.put(quad.resultado, "Real");
                    } else {
                        variableTypes.put(quad.resultado, "Int");
                    }
                } else if (isOperador(quad.operador) || quad.operador.equals("IF_FALSE")) {
                    variableTypes.put(quad.resultado, "Int");
                }
                if (!temporaries.contains(quad.resultado)) {
                    temporaries.add(quad.resultado);
                }
            }

            // Collect constants
            if (quad.operador.equals("Assign") && (quad.operando1.matches("\\d+\\.\\d+"))) {
                if (!constants.contains(quad.operando1)) {
                    constants.add(quad.operando1);
                }
            }
            if (quad.operando1.matches("\\d+\\.\\d+")) {
                if (!constants.contains(quad.operando1)) {
                    constants.add(quad.operando1);
                }
            }
            if (quad.operando2.matches("\\d+\\.\\d+")) {
                if (!constants.contains(quad.operando2)) {
                    constants.add(quad.operando2);
                }
            }
        }
    }

    private static boolean isOperador(String operador) {
        String[] operadores = {"<", ">", ">=", "<=", "!=", "=="};
        operador = operador.replace(" ", "");

        for (String op : operadores) {
            if (operador.equals(op)) return true;
        }
        return false;
    }

    private static String doubleToHexString(double value) {
        return Long.toHexString(Double.doubleToRawLongBits(value));
    }

    public static String generateAssembly(List<Cuadruplo> quadruples) {
        StringBuilder assemblyCode = new StringBuilder();

        // Sección de datos
        assemblyCode.append("section .data\n");

        // Define constants
        Map<String, String> constantMap = new HashMap<>();
        int constIndex = 1;
        for (String constant : constants) {
            String label = "const_" + constIndex++;
            constantMap.put(constant, label);
            if (constant.contains(".")) {
                assemblyCode.append(label).append(" dq 0x").append(doubleToHexString(Double.parseDouble(constant))).append(" ; "+ constant+"\n");
            } else {
                //assemblyCode.append(label).append(" dd ").append(constant).append("\n");
            }
        }

        // Define variables
        for (Map.Entry<String, String> entry : variableTypes.entrySet()) {
            String type = entry.getValue().equals("Real") ? "dq" : "dd";
            assemblyCode.append(entry.getKey()).append(" ").append(type).append(" 0\n");
        }

        assemblyCode.append("section .text\nglobal _start\n_start:\n");

        for (Cuadruplo quad : quadruples) {
            switch (quad.operador) {
                case "Assign":
                    if (variableTypes.get(quad.resultado).equals("Real")) {
                        String constantLabel = constantMap.get(quad.operando1);
                        if (constantLabel != null) {
                            assemblyCode.append("movsd xmm0, qword [").append(constantLabel).append("]\n");
                        } else {
                            assemblyCode.append("movsd xmm0, qword [").append(quad.operando1).append("]\n");
                        }
                        assemblyCode.append("movsd qword [").append(quad.resultado).append("], xmm0\n");
                    } else {
                        assemblyCode.append("mov dword [").append(quad.resultado).append("], ").append(quad.operando1).append("\n");
                    }
                    break;
                case ">":
                    if (constantMap.get(quad.operando1) != null) {
                        String constantLabel = constantMap.get(quad.operando1);
                        assemblyCode.append("movsd xmm1, qword [").append(constantLabel).append("]\n");
                    } else {
                        assemblyCode.append("mov eax, ").append(quad.operando1).append("\n");
                    }
                    if (constantMap.get(quad.operando2) != null) {
                        String constantLabel = constantMap.get(quad.operando2);
                        assemblyCode.append("movsd xmm2, qword [").append(constantLabel).append("]\n");
                        assemblyCode.append("ucomisd xmm1, xmm2").append("\n");
                    } else {
                        assemblyCode.append("cmp eax, ").append(quad.operando2).append("\n");
                    }
                    assemblyCode.append("setg byte [").append(quad.resultado).append("]\n");
                    break;
                case "<":
                    if (constantMap.get(quad.operando1) != null) {
                        String constantLabel = constantMap.get(quad.operando1);
                        assemblyCode.append("movsd xmm1, qword [").append(constantLabel).append("]\n");
                    } else {
                        assemblyCode.append("mov eax, ").append(quad.operando1).append("\n");
                    }
                    if (constantMap.get(quad.operando2) != null) {
                        String constantLabel = constantMap.get(quad.operando2);
                        assemblyCode.append("movsd xmm2, qword [").append(constantLabel).append("]\n");
                        assemblyCode.append("ucomisd xmm1, xmm2").append("\n");
                    } else {
                        assemblyCode.append("cmp eax, ").append(quad.operando2).append("\n");
                    }
                    assemblyCode.append("setl byte [").append(quad.resultado).append("]\n");
                    break;
                case "<=":
                    if (constantMap.get(quad.operando1) != null) {
                        String constantLabel = constantMap.get(quad.operando1);
                        assemblyCode.append("movsd xmm1, qword [").append(constantLabel).append("]\n");
                    } else {
                        assemblyCode.append("mov eax, ").append(quad.operando1).append("\n");
                    }
                    if (constantMap.get(quad.operando2) != null) {
                        String constantLabel = constantMap.get(quad.operando2);
                        assemblyCode.append("movsd xmm2, qword [").append(constantLabel).append("]\n");
                        assemblyCode.append("ucomisd xmm1, xmm2").append("\n");
                    } else {
                        assemblyCode.append("cmp eax, ").append(quad.operando2).append("\n");
                    }
                    assemblyCode.append("setle byte [").append(quad.resultado).append("]\n");
                    break;
                case ">=":
                    if (constantMap.get(quad.operando1) != null) {
                        String constantLabel = constantMap.get(quad.operando1);
                        assemblyCode.append("movsd xmm1, qword [").append(constantLabel).append("]\n");
                    } else {
                        assemblyCode.append("mov eax, ").append(quad.operando1).append("\n");
                    }
                    if (constantMap.get(quad.operando2) != null) {
                        String constantLabel = constantMap.get(quad.operando2);
                        assemblyCode.append("movsd xmm2, qword [").append(constantLabel).append("]\n");
                        assemblyCode.append("ucomisd xmm1, xmm2").append("\n");
                    } else {
                        assemblyCode.append("cmp eax, ").append(quad.operando2).append("\n");
                    }
                    assemblyCode.append("setge byte [").append(quad.resultado).append("]\n");
                    break;
                case "==":
                    if (constantMap.get(quad.operando1) != null) {
                        String constantLabel = constantMap.get(quad.operando1);
                        assemblyCode.append("movsd xmm1, qword [").append(constantLabel).append("]\n");
                    } else {
                        assemblyCode.append("mov eax, ").append(quad.operando1).append("\n");
                    }
                    if (constantMap.get(quad.operando2) != null) {
                        String constantLabel = constantMap.get(quad.operando2);
                        assemblyCode.append("movsd xmm2, qword [").append(constantLabel).append("]\n");
                        assemblyCode.append("ucomisd xmm1, xmm2").append("\n");
                    } else {
                        assemblyCode.append("cmp eax, ").append(quad.operando2).append("\n");
                    }
                    assemblyCode.append("sete byte [").append(quad.resultado).append("]\n");
                    break;
                case "!=":
                    if (constantMap.get(quad.operando1) != null) {
                        String constantLabel = constantMap.get(quad.operando1);
                        assemblyCode.append("movsd xmm1, qword [").append(constantLabel).append("]\n");
                    } else {
                        assemblyCode.append("mov eax, ").append(quad.operando1).append("\n");
                    }
                    if (constantMap.get(quad.operando2) != null) {
                        String constantLabel = constantMap.get(quad.operando2);
                        assemblyCode.append("movsd xmm2, qword [").append(constantLabel).append("]\n");
                        assemblyCode.append("ucomisd xmm1, xmm2").append("\n");
                    } else {
                        assemblyCode.append("cmp eax, ").append(quad.operando2).append("\n");
                    }
                    assemblyCode.append("setne byte [").append(quad.resultado).append("]\n");
                    break;
                case "IF_FALSE":
                    assemblyCode.append("cmp byte [").append(quad.operando1).append("], 0\n");
                    assemblyCode.append("je ").append(quad.resultado).append("\n");
                    break;
                case "GOTO":
                    assemblyCode.append("jmp ").append(quad.resultado).append("\n");
                    break;
                case "+":
                case "-":
                case "*":
                case "/":
                    if (variableTypes.get(quad.resultado).equals("Real")) {
                        if (constantMap.get(quad.operando1) != null) {
                            String constantLabel = constantMap.get(quad.operando1);
                            assemblyCode.append("movsd xmm0, qword [").append(constantLabel).append("]\n");
                        } else {
                            assemblyCode.append("movsd xmm0, qword [").append(quad.operando1).append("]\n");
                        }
                        if (constantMap.get(quad.operando2) != null) {
                            String constantLabel = constantMap.get(quad.operando2);
                            assemblyCode.append("movsd xmm1, qword [").append(constantLabel).append("]\n");
                        } else {
                            assemblyCode.append("movsd xmm1, qword [").append(quad.operando2).append("]\n");
                        }
                        switch (quad.operador) {
                            case "+":
                                assemblyCode.append("addsd xmm0, xmm1\n");
                                break;
                            case "-":
                                assemblyCode.append("subsd xmm0, xmm1\n");
                                break;
                            case "*":
                                assemblyCode.append("mulsd xmm0, xmm1\n");
                                break;
                            case "/":
                                assemblyCode.append("divsd xmm0, xmm1\n");
                                break;
                        }
                        assemblyCode.append("movsd qword [").append(quad.resultado).append("], xmm0\n");
                    } else {
                        assemblyCode.append("mov eax, ").append(quad.operando1).append("\n");
                        switch (quad.operador) {
                            case "+":
                                assemblyCode.append("add eax, ").append(quad.operando2).append("\n");
                                break;
                            case "-":
                                assemblyCode.append("sub eax, ").append(quad.operando2).append("\n");
                                break;
                            case "*":
                                assemblyCode.append("imul eax, ").append(quad.operando2).append("\n");
                                break;
                            case "/":
                                assemblyCode.append("mov ebx, ").append(quad.operando2).append("\n");
                                assemblyCode.append("cdq\n"); // Sign-extend eax into edx:eax
                                assemblyCode.append("idiv ebx\n");
                                break;
                        }
                        assemblyCode.append("mov [").append(quad.resultado).append("], eax\n");
                    }
                    break;
                default:
                    if (quad.operador.startsWith("L")) {
                        assemblyCode.append(quad.operador).append(":\n");
                    }
                    break;
            }
        }

        // Fin del programa
        assemblyCode.append("mov rax, 60\nxor rdi, rdi\nsyscall\n");

        return assemblyCode.toString();
    }

    private static void variables(Cuadruplo quad, StringBuilder assemblyCode, Map<String, String> constantMap) {

        if (constantMap.get(quad.operando1) != null) {
            String constantLabel = constantMap.get(quad.operando1);
            assemblyCode.append("movsd xmm0, qword [").append(constantLabel).append("]\n");
        } else {
            assemblyCode.append("movsd xmm0, qword [").append(quad.operando1).append("]\n");
        }
        if (constantMap.get(quad.operando2) != null) {
            String constantLabel = constantMap.get(quad.operando2);
            assemblyCode.append("movsd xmm0, qword [").append(constantLabel).append("]\n");
        } else {
            assemblyCode.append("movsd xmm0, qword [").append(quad.operando2).append("]\n");
        }
    }

    public static void saveToFile(String fileName, String content) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

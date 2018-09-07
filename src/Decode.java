public class Decode {

    static int loadCount = 0;
    static int storeCount = 0;
    static int aluCount = 0;
    static int branchCount = 0;
    static int jumpCount = 0;
    static int nopCount = 0;

    public int[] instructionDecode(String instruction) {
        int[] result = new int[4];  // result = {opcode,source1,source2,destination,branch/jump}
        String opcode = instruction.substring(25, 30);
        int decimalOpcode = Integer.parseInt(opcode, 2);
        switch (decimalOpcode) {
            case 0:
                result[0] = 0;
                result[1] = Integer.parseInt(instruction.substring(12, 17), 2);
                result[2] = -1;
                result[3] = Integer.parseInt(instruction.substring(20, 25), 2);
                loadCount = loadCount + 1;
                System.out.println("Instruction fetched is LOAD");
                break;
            case 4:
                result[0] = 4;
                result[1] = Integer.parseInt(instruction.substring(12, 17), 2);
                result[2] = -1;
                result[3] = Integer.parseInt(instruction.substring(20, 25), 2);
                aluCount = aluCount + 1;
                System.out.println("Instruction fetched is OP-IMM");
                break;
            case 5:
                result[0] = 5;
                result[1] = -1;
                result[2] = -1;
                result[3] = Integer.parseInt(instruction.substring(20, 25), 2);
                aluCount = aluCount + 1;
                System.out.println("Instruction fetched is ALUPC");
                break;
            case 6:
                result[0] = 6;
                result[1] = Integer.parseInt(instruction.substring(12, 17), 2);
                result[2] = -1;
                result[3] = Integer.parseInt(instruction.substring(20, 25), 2);
                aluCount = aluCount + 1;
                System.out.println("Instruction fetched is ALU-IMM");
                break;
            case 8:
                result[0] = 8;
                result[1] = Integer.parseInt(instruction.substring(12, 17), 2);
                result[2] = Integer.parseInt(instruction.substring(7, 12), 2);
                result[3] = -1;
                storeCount = storeCount + 1;
                System.out.println("Instruction fetched is STORE");
                break;
            case 12:
                result[0] = 12;
                result[1] = Integer.parseInt(instruction.substring(12, 17), 2);
                result[2] = Integer.parseInt(instruction.substring(7, 12), 2);
                result[3] = Integer.parseInt(instruction.substring(20, 25), 2);
                aluCount = aluCount + 1;
                System.out.println("Instruction fetched is OP");
                break;
            case 13:
                result[0] = 13;
                result[1] = -1;
                result[2] = -1;
                result[3] = Integer.parseInt(instruction.substring(20, 25), 2);
                aluCount = aluCount + 1;
                System.out.println("Instruction fetched is LUI");
                break;
            case 14:
                result[0] = 14;
                result[1] = Integer.parseInt(instruction.substring(12, 17), 2);
                result[2] = Integer.parseInt(instruction.substring(7, 12), 2);
                result[3] = Integer.parseInt(instruction.substring(20, 25), 2);
                aluCount = aluCount + 1;
                System.out.println("Instruction fetched is ALU-OP");
                break;
            case 24:
                result[0] = 24;
                result[1] = Integer.parseInt(instruction.substring(12, 17), 2);
                result[2] = Integer.parseInt(instruction.substring(7, 12), 2);
                result[3] = -1;
                branchCount = branchCount + 1;
                System.out.println("Instruction fetched is BRANCH");
                break;
            case 25:
                result[0] = 25;
                result[1] = Integer.parseInt(instruction.substring(12, 17), 2);
                result[2] = -1;
                result[3] = Integer.parseInt(instruction.substring(20, 25), 2);
                jumpCount = jumpCount + 1;
                System.out.println("Instruction fetched is JALR");
                break;
            case 26:
                result[0] = 26;
                result[1] = -1;
                result[2] = -1;
                result[3] = -1;
                jumpCount = jumpCount + 1;
                System.out.println("Instruction fetched is J");
                break;
            case 27:
                result[0] = 27;
                result[1] = -1;
                result[2] = -1;
                result[3] = Integer.parseInt(instruction.substring(20, 25), 2);
                jumpCount = jumpCount + 1;
                System.out.println("Instruction fetched is JAL");
                break;
            case 29:
                result[0] = 29;
                result[1] = Integer.parseInt(instruction.substring(12, 17), 2);
                result[2] = -1;
                result[3] = -1; //Ignoring "rd" for system. Integer.parseInt(instruction.substring(20,25),2);
                branchCount = branchCount + 1;
                System.out.println("Instruction fetched is SYSTEM");
                break;
            default:
                result[0] = -100;
                result[1] = -100;
                result[2] = -100;
                result[3] = -100;
                nopCount = nopCount + 1;
                System.out.println("Instruction fetched is NO-OP");
        }
        return result;
    }
}

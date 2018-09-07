import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public ArrayList<String> fileRead(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        ArrayList<String> ar = new ArrayList<String>();
        while ((st = br.readLine()) != null) {
            ar.add(st.trim());
        }
        return (ar);
    }

    public static void main(String[] args) throws IOException {
        Main t = new Main();
        ArrayList<String> address = t.fileRead("inst_addr_riscv_trace_project_2.txt");
        ArrayList<String> data = t.fileRead("inst_data_riscv_trace_project_2.txt");
        boolean nonConsecutiveAddress;
        int nextDecimalAddress;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the configuration value for instruction cache miss");
        int iCache = in.nextInt();
        System.out.println("Enter the configuration value for data cache miss");
        int dCache = in.nextInt();
        for (int i = 0; i < address.size(); i++) {
            nonConsecutiveAddress = false;
            int addressDecimal = Integer.parseInt(address.get(i).trim(), 16);
            if (i != (address.size() - 1)) {
                nextDecimalAddress = Integer.parseInt(address.get(i + 1).trim(), 16);
                int difference = nextDecimalAddress - addressDecimal;
                if (difference != 4) {
                    nonConsecutiveAddress = true;
                }
            }
            long dataDecimal = Long.parseLong(data.get(i).trim(), 16);
            String binaryData = Long.toBinaryString(dataDecimal);
            binaryData = String.format("%32s", binaryData).replace(' ', '0');
            Processor p = new Processor();
            p.instructionFetch(binaryData, nonConsecutiveAddress, iCache, dCache);
        }
        float totalMemCacheAccess = Decode.loadCount + Decode.storeCount;
        float memHitRatio = Processor.dCacheHit / totalMemCacheAccess;
        float instructionHitRatio = Processor.iCacheHit / (float) address.size();
        float IPC = (address.size()) / (float) Processor.globalWBClock;
        float percentOfTakenOverTotalBranch = (Processor.takenBranches / (float) Decode.branchCount) * 100;
        float aluFrequency = (Decode.aluCount / (float) address.size()) * 100;
        float loadFrequency = (Decode.loadCount / (float) address.size()) * 100;
        float storeFrequency = (Decode.storeCount / (float) address.size()) * 100;
        float branchFrequency = (Decode.branchCount / (float) address.size()) * 100;
        float percentStalledOverTotalCycles = (Processor.stallCount / (float) Processor.globalWBClock) * 100;
        System.out.println();
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Statistical data");
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Number of data cache load accesses = " + Decode.loadCount);
        System.out.println("Number of data cache store accesses = " + Decode.storeCount);
        System.out.println("Number of instruction cache accesses = " + address.size());
        System.out.println("Number of hits for instruction cache = " + Processor.iCacheHit);
        System.out.println("Number of hits for data cache = " + Processor.dCacheHit);
        System.out.println("Number of ALU instruction type = " + Decode.aluCount);
        System.out.println("Number of branch instruction type = " + Decode.branchCount);
        System.out.println("Number of load instruction type = " + Decode.loadCount);
        System.out.println("Number of store instruction type = " + Decode.storeCount);
        System.out.println("Number of jump instruction type = " + Decode.jumpCount);
        System.out.println("Number of No-Op instruction type = " + Decode.nopCount);
        System.out.println("Number of taken branches = " + Processor.takenBranches);
        System.out.println("Number of forwarding from EX stage = " + Processor.aluForwardCount);
        System.out.println("Number of forwarding from MEM stage = " + Processor.memForwardCount);
        System.out.println("Number of stalled cycles in ID stage = " + Processor.stallCountInID);
        System.out.println("Total cycles = " + Processor.globalWBClock);
        System.out.println();
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Calculations");
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Instruction cache hit ratio = " + instructionHitRatio);
        System.out.println("Memory cache hit ratio = " + memHitRatio);
        System.out.println("Instruction Per Cycle (IPC) = " + IPC);
        System.out.println("Percentage of taken branches over total branches = " + percentOfTakenOverTotalBranch + "%");
        System.out.println("Instruction frequency for ALU = " + aluFrequency + "%");
        System.out.println("Instruction frequency for load = " + loadFrequency + "%");
        System.out.println("Instruction frequency for store = " + storeFrequency + "%");
        System.out.println("Instruction frequency for branch = " + branchFrequency + "%");
        System.out.println("Percentage of stalled cycles over total cycles = " + percentStalledOverTotalCycles + "%");
        System.out.println("-----------------------------------------------------------------");
    }
}

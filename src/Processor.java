public class Processor {
    static int globalIFClock = 0;
    static int globalIDClockEntry = 0;
    static int globalIDClockExit = 0;
    static int globalEXEClock = 0;
    static int globalMEMClock = 0;
    static int globalWBClock = 0;
    static int globalBranchValid_0 = 0;
    static int globalBranchValid_1 = 0;
    static int iCacheHit = 0;
    static int dCacheHit = 0;
    static int[][] scoreBoard;
    static int aluForwardCount = 0;
    static int memForwardCount = 0;
    static int wbForwardCount = 0;
    static int stallCount = 0;
    static int takenBranches = 0;
    static int stallCountInID = 0;


    static {
        scoreBoard = new int[32][4];
    }

    private int opcode;
    private int destination;

    void instructionFetch(String instruction, boolean nonConsecutiveAddress, int iCache, int dCache) {
        globalIFClock = Math.max(Math.max((globalIFClock + 1), globalIDClockEntry), (globalBranchValid_0 + 1));
        if (globalIFClock % iCache == 55) {
            globalIFClock = globalIFClock + 15;
            stallCount = stallCount + 15;

        } else {
            iCacheHit = iCacheHit + 1;
        }
        this.instructionDecode(instruction, nonConsecutiveAddress, dCache);
    }

    private void instructionDecode(String instruction, boolean nonConsecutiveAddress, int dCache) {
        Decode d = new Decode();
        int[] result;
        int source1;
        int source2;
        result = d.instructionDecode(instruction);
        opcode = result[0];
        source1 = result[1];
        source2 = result[2];
        destination = result[3];
        if ((opcode == 24 || opcode == 29) && nonConsecutiveAddress) {
            takenBranches = takenBranches + 1;
        }
        globalIDClockEntry = Math.max(Math.max((globalIDClockExit + 1), (globalIFClock + 1)), globalEXEClock);
        globalIDClockExit = globalIDClockEntry;
        if ((source1 != -1) && (source1 != -100) && (source1 != 0)) {
            this.checkSource(source1);
        }
        if ((source2 != -1) && (source2 != -100) && (source2 != 0)) {
            this.checkSource(source2);
        }
        if ((destination != -1) && (destination != -100) && (destination != 0)) {
            this.writeScoreboard(destination, opcode, globalIDClockExit, 3);
        }
        this.alu(dCache);
    }

    private void alu(int dCache) {
        globalEXEClock = Math.max((globalIDClockExit + 1), globalMEMClock);
        if (opcode == 24 || opcode == 29) {
            globalBranchValid_1 = globalEXEClock;
            globalBranchValid_0 = globalEXEClock + 1;
            stallCount = stallCount + 4;
        }
        if ((destination != -1) && (destination != -100) && (destination != 0)) {
            this.writeScoreboard(destination, opcode, globalEXEClock, 1);
        }
        this.memory(dCache);
    }

    private void memory(int dCache) {
        //globalMEMClock = globalEXEClock + 1;
        globalMEMClock = Math.max((globalEXEClock + 1), globalWBClock);
        if (opcode == 0 || opcode == 8) {
            if (globalMEMClock % dCache == 11) {
                globalMEMClock = globalMEMClock + 15;
                stallCount = stallCount + 15;
            } else {
                dCacheHit = dCacheHit + 1;
            }
        }
        if ((destination != -1) && (destination != -100) && (destination != 0)) {
            this.writeScoreboard(destination, opcode, globalMEMClock, 2);
        }
        this.writeBack();
    }

    private void writeBack() {
        globalWBClock = globalMEMClock + 1;
        if ((destination != -1) && (destination != -100) && (destination != 0)) {
            this.writeScoreboard(destination, opcode, globalWBClock, 0);
        }
    }

    private void checkSource(int source) {
        while (globalIDClockExit <= scoreBoard[source][3]) {
            globalIDClockExit = globalIDClockExit + 1;
            stallCount = stallCount + 1;
            stallCountInID = stallCountInID + 1;
        }
        for (int i = 2; i >= 0; i--) {
            if (globalIDClockExit < scoreBoard[source][i]) {
                continue;
            }
            if (globalIDClockExit > scoreBoard[source][i]) {
                continue;
            }
            if (globalIDClockExit == scoreBoard[source][i]) {
                switch (i) {
                    case 2:
                        memForwardCount++;
                        break;
                    case 1:
                        aluForwardCount++;
                        break;
                    case 0:
                        wbForwardCount++;
                        break;
                }
            }
        }
    }

    private void writeScoreboard(int destination, int opcode, int currentTime, int currentStage) {
        if (opcode == 0 && currentStage == 3) {
            scoreBoard[destination][currentStage] = currentTime + 1;
        }
        if (opcode != 0 && currentStage == 3) {
            scoreBoard[destination][currentStage] = currentTime;
        }
        if (opcode == 0 && currentStage == 1) {
            scoreBoard[destination][currentStage] = 0;
        }
        if (opcode != 0 && currentStage == 1) {
            scoreBoard[destination][currentStage] = currentTime;
        }
        if (currentStage == 2) {
            scoreBoard[destination][currentStage] = currentTime;
        }
        if (currentStage == 0) {
            scoreBoard[destination][currentStage] = currentTime;
        }
    }

}

package com.snk.door.enums;

import com.snk.door.api.finger.yz.BioMiniLibrary;

/**
 * 指纹机版本算法转换
 */
public enum FingerVersion {
    /**
     * 07.00
     */
    V_70("07.00", BioMiniLibrary.FpDataConv.DATA_VER_70),

    /**
     * 08.00
     */
    V_80("08.00", BioMiniLibrary.FpDataConv.DATA_VER_70),

    /**
     * 08.50
     */
    V_85("08.50", BioMiniLibrary.FpDataConv.DATA_VER_70),

    /**
     * 08.90
     */
    V_89("08.90", BioMiniLibrary.FpDataConv.DATA_VER_70);

    private String ver;

    private int dataVer;

    FingerVersion(String ver, int dataVer) {
        this.ver = ver;
        this.dataVer = dataVer;
    }

    public String getVer() {
        return ver;
    }

    public int getDataVer() {
        return dataVer;
    }

    public static FingerVersion getFingerVersion(String ver) {
        for (FingerVersion fingerVersion : FingerVersion.values()) {
            if (fingerVersion.getVer().equals(ver)) {
                return fingerVersion;
            }
        }
        return null;
    }

    public static int getDataVer(String ver) {
        for (FingerVersion fingerVersion : FingerVersion.values()) {
            if (fingerVersion.getVer().equals(ver)) {
                return fingerVersion.getDataVer();
            }
        }
        return FingerVersion.V_70.getDataVer();
    }
}

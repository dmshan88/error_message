package com.example.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelInfo {
    private static final Map<Integer, String> panelNameMap = new HashMap<Integer, String>() {
        private static final long serialVersionUID = 1L;
    {
        put(0, "肝肾");
        put(1, "肝功");
        put(2, "肾功");
        put(3, "电解质");
        put(5, "心肌酶");
        put(6, "糖脂");
        put(7, "综合1");
        put(8, "综合2");
        put(9, "临床急诊");
        put(10, "综合1");
        put(11, "综合1+");
        put(12, "综合1A");
        put(13, "半胱氨酸");
        put(14, "肝肾");
        put(15, "肝功");
        put(16, "肾功");
        put(17, "电解质");
        put(18, "心肌酶");
        put(19, "糖脂");
        put(20, "综合1");
        put(21, "综合2");
        put(22, "临床急诊");
        put(23, "临床急诊2");
        put(24, "综合4");

        put(50, "综合诊断");
        put(51, "手术前");
        put(52, "急重症");
        put(55, "健康");
        put(57, "电解质");
        put(60, "肝肾");
        put(61, "肝功");
        put(62, "肾功");
        put(63, "手术前+");
        put(64, "复查两项");
        put(65, "复查三项");
        put(66, "大动物");
        put(67, "血氨");
        put(68, "异宠");
        put(69, "半胱氨酸");
        put(70, "pH");
        put(71, "胰腺炎");
        put(72, "新急重症");
        put(73, "糖尿病");
        put(74, "肾功");
        put(75, "马检测");
       }
    };
    private static final Map<Integer, String> errorNameMap = new HashMap<Integer, String>() {
        private static final long serialVersionUID = 1L;
    {
        put(0, "黄疸");
        put(1, "溶血");
        put(2, "脂血");
        put(3, "0208_1");
        put(4, "0209");
        put(5, "0210");
        
        put(6, "0211");
        put(8, "0215");
        put(9, "0216");
        put(10, "0214");
        put(11, "0213");
        put(12, "0217");
        put(13, "0218");
        put(14, "0219");
        put(15, "0208_2");
        put(16, "0214_leak");
       }
    };
    
    public static String getPanelName(int panelId) {
        return panelNameMap.containsKey(panelId) ? panelNameMap.get(panelId) : "新盘片";
    }
    
    public static String parseError(int errorCode) {
        List<String> errorList=new ArrayList<>();
        for (Map.Entry<Integer, String> entry : errorNameMap.entrySet()) { 
            if ((errorCode & (1 << entry.getKey())) != 0) {
                errorList.add(entry.getValue());
            }
        }
        String errorString = new String();
        for (int i=0; i < errorList.size(); i++) {
            errorString += errorList.get(i);
            if (i < errorList.size() - 1) {
                errorString += ",";
            }
        }
        return errorString;
    }
    
    public static String simpleMachineId(String machineId) {
        String simpleName = "";
        final String celercare = "Celercare";
        final String pointcare = "Pointcare";
        if (machineId.startsWith(celercare)) {;
            simpleName += "C" + machineId.substring(celercare.length(), celercare.length() + 2);
        } else if (machineId.startsWith(pointcare)) {
            simpleName += "P" + machineId.substring(pointcare.length(), pointcare.length() + 2);
        }
        if (machineId.contains("unset")) {
            simpleName += "unset";
        } else {
            simpleName += machineId.substring(machineId.length()-6);
        }
        return simpleName;
    }
}

package com.benjaminwan.compose.chinesettstflite.utils;

/**
 * Create by zhp on 2021/2/6
 * Description:
 */
public class NumberToCH {

    public static String dateToCH(String date) {
        String ss[] = date.split(" ");
        StringBuilder builder = new StringBuilder();
        if (ss.length == 2) {
            dateYearToCH(builder, ss[0]);
            dateHourToCH(builder, ss[1]);
        } else if (ss.length == 1) {
            if (date.contains("-")) {
                dateYearToCH(builder, date);
            } else {
                dateHourToCH(builder, date);
            }
        } else {
            builder.append(date);
        }

        return builder.toString();
    }

    private static void dateHourToCH(StringBuilder builder, String date) {
        String[] ss = date.split(":");
        if (ss.length != 3) {
            builder.append(date);
            return;
        }

        builder.append(stringNumberToCH(ss[0]))
                .append("点")
                .append(stringNumberToCH(ss[1]))
                .append("分")
                .append(stringNumberToCH(ss[2]))
                .append("秒");
    }

    private static void dateYearToCH(StringBuilder builder, String date) {
        String[] ss = date.split("-");
        if (ss.length != 3) {
            builder.append(date);
            return;
        }
        char[] year = ss[0].toCharArray();
        for (int i = 0; i < year.length; i++) {
            builder.append(getCH(Integer.valueOf(String.valueOf(year[i])), true));
        }
        builder.append("年")
                .append(stringNumberToCH(ss[1]))
                .append("月")
                .append(stringNumberToCH(ss[2]))
                .append("日");
    }

    public static String stringNumberToCH(String number) {
        if (number == null) {
            return null;
        }
        number = number.trim();
        if (number.startsWith("0")) {
            number = number.substring(1);
        }

        try {
            return numberToCH(Integer.valueOf(number));
        } catch (Exception e) {
            return number;
        }
    }

    public static String numberToCH(int intInput) {
        String si = String.valueOf(intInput);
        String sd = "";
        if (si.length() == 1) {// 個
            sd += getCH(intInput);
            return sd;
        } else if (si.length() == 2) {// 十
            if (si.startsWith("1"))
                sd += "十";
            else
                sd += (getCH(intInput / 10) + "十");
            sd += numberToCH(intInput % 10);
        } else if (si.length() == 3) {// 百
            sd += (getCH(intInput / 100) + "百");
            int mode = intInput % 100;
            if (mode != 0 && String.valueOf(mode).length() < 2)
                sd += "零";
            sd += numberToCH(mode);
        } else if (si.length() == 4) {// 千
            sd += (getCH(intInput / 1000) + "千");
            int mode = intInput % 1000;
            if (mode != 0 && String.valueOf(mode).length() < 3)
                sd += "零";
            sd += numberToCH(mode);
        } else if (si.length() == 5) {// 萬
            sd += (getCH(intInput / 10000) + "萬");
            int mode = intInput % 10000;
            if (mode != 0 && String.valueOf(mode).length() < 4)
                sd += "零";
            sd += numberToCH(mode);
        }

        return sd;
    }

    private static String getCH(int input, boolean includeZero) {
        String sd = "";
        switch (input) {
            case 0:
                sd = includeZero ? "零" : "";
                break;
            case 1:
                sd = "一";
                break;
            case 2:
                sd = "二";
                break;
            case 3:
                sd = "三";
                break;
            case 4:
                sd = "四";
                break;
            case 5:
                sd = "五";
                break;
            case 6:
                sd = "六";
                break;
            case 7:
                sd = "七";
                break;
            case 8:
                sd = "八";
                break;
            case 9:
                sd = "九";
                break;
            default:
                break;
        }
        return sd;
    }

    private static String getCH(int input) {
        return getCH(input, false);
    }
}

package cn.jmessage.android.uikit.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.TreeSet;

/**
 * 字符创操作类
 *
 * @version v1.0
 * @author baimei
 * @createDate 2016年7月1日
 */
public class StringUtil {

    private static String TAG = "jmessage";

    /**
     * 字符串是否为空。
     */
    public static boolean isEmpty(String str) {
        String string = str;
        if (string == null) {
            return true;
        }
        string = string.trim();
        return "".equals(string) || string == "" ? true : false;
    }

    /**
     * 格式化字符串，显示不全用"…"代替
     *
     * @param str 格式化的文字
     * @param num 每行显示字数
     * @return String
     */
    public static String appendSpaceT(String str, int num) {
        int length = str.length();
        char[] value = new char[length << 1];
        for (int i = 0, j = 0; i < length; ++i, j = i << 1) {
            value[j] = str.charAt(i);
            if (j % num == 0 && j > 0) {
                value[1 + j] = ' ';
                break;
            }
        }
        return new String(value).replaceAll(" ", "...");
    }

    /**
     * 获得最大长度的汉字字串
     *
     * @param source
     * @param maxLength
     * @return
     */
    public static String getChineseStringByMaxLength(String source, int maxLength) {
        String cutString = "";
        double tempLength = 0;
        for (int i = 0; i < source.length(); i++) {
            String temp = source.substring(i, i + 1);
            String chinese = "[\u4e00-\u9fa5]";
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为1
                tempLength += 1;
            } else {
                // 其他字符长度为0.5
                tempLength += 0.5;
            }
            cutString += temp;
            if (Math.ceil(tempLength) == maxLength) {
                break;
            }
        }
        return cutString;
    }

    /**
     * 获取字符串的长度，中文占一个字符,英文数字占半个字符
     *
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static double chineseLength(String value) {
        double valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < value.length(); i++) {
            // 获取一个字符
            String temp = value.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为1
                valueLength += 1;
            } else {
                // 其他字符长度为0.5
                valueLength += 0.5;
            }
        }
        // 进位取整
        return Math.ceil(valueLength);
    }

    /**
     *
     * (根据字数，限制每行显示的文字)
     *
     * @param para 数据
     * @param num 每行显示多少字
     * @return String
     */
    public static String appendSpace(String para, int num) {
        int length = para.length();
        char[] value = new char[length << 1];
        for (int i = 0, j = 0; i < length; ++i, j = i << 1) {
            value[j] = para.charAt(i);
            if (j % num == 0 && j > 0)
                value[1 + j] = ' ';
        }
        return new String(value).replaceAll(" ", "\n");
    }

    /**
     * 判断字符串是否非空
     *
     * @param str
     * @return boolean
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null || str.length() == 0 || "".equals(str) || "null".equals(str) || "NULL".equals(str) || str.equals(null)) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否非空
     *
     * @param str
     * @return boolean
     */
    public static boolean isNull(String str) {
        if (str == null) {
            return true;
        }
        return false;
    }

    /**
     * 字符串转换int
     *
     * @param str 待处理字符串
     * @return int 转换后的数字
     */
    public static int parseInt(String str) {
        if (isNullOrEmpty(str)) {
            return 0;
        }

        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 字符串转换int
     *
     * @param str 待处理字符串
     * @return int 转换后的数字
     */
    public static int parseInt(String str, int def) {
        if (isNullOrEmpty(str)) {
            return def;
        }

        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }

    /**
     * 字符串转换long
     *
     * @param str 待处理字符串
     * @return int 转换后的数字
     */
    public static long parseLong(String str) {
        if (isNullOrEmpty(str)) {
            return 0;
        }

        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 字符串转换long
     *
     * @param str 待处理字符串
     * @return int 转换后的数字
     */
    public static long parseLong(String str, long def) {
        if (isNullOrEmpty(str)) {
            return def;
        }

        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }

    /**
     * 字符串转换double
     *
     * @param str 待处理字符串
     * @return int 转换后的数字
     */
    public static double parseDouble(String str) {
        if (isNullOrEmpty(str)) {
            return 0.0;
        }

        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    /**
     * 字符串转换金钱
     *
     * @param str 待处理字符串
     * @return int 转换后的数字
     */
    public static String parseMoney(String str) {
        if (isNullOrEmpty(str)) {
            return "0.00";
        }

        try {
            DecimalFormat df = new DecimalFormat("######0.00");
            return df.format(Double.parseDouble(str));
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
    }

    /**
     * 字符串转换double
     *
     * @param str 待处理字符串
     * @return int 转换后的数字
     */
    public static double parseDouble(String str, double def) {
        if (isNullOrEmpty(str)) {
            return def;
        }

        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }

    /**
     * 字符串转换float(默认0.00)
     *
     * @param str 待处理字符串
     * @return int 转换后的数字
     */
    public static double parseFloat(String str) {
        if (isNullOrEmpty(str)) {
            return 0.00;
        }

        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.00;
        }
    }

    /**
     * 字符串转换float
     *
     * @param str 待处理字符串
     * @return int 转换后的数字
     */
    public static double parseFloat(String str, float def) {
        if (isNullOrEmpty(str)) {
            return def;
        }

        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            e.printStackTrace();
            return def;
        }
    }

    /**
     * 去掉数字前面的0
     *
     * @param str
     * @return String
     */
    public String formatDataStr(String str) {
        return str.replaceAll("^(0+)", "");
    }

    /**
     * 字符串数组去重
     *
     * @param strs 字符串数组
     * @return String[] 去重后的字符串数组
     */
    public static String[] clearRepeatStrs(String[] strs) {
        TreeSet<String> tr = new TreeSet<String>();
        for (int i = 0; i < strs.length; i++) {
            tr.add(strs[i]);
        }

        String[] s2 = new String[tr.size()];
        for (int i = 0; i < s2.length; i++) {
            s2[i] = tr.pollFirst();// 从TreeSet中取出元素重新赋给数组
        }

        return s2;
    }

    /**
     * 过滤空字符
     *
     * @param str
     * @return String
     */
    public static String filterNull(String str) {
        return str != null ? str : "";
    }

    /**
     * 把一个字符串中的大写转为小写
     *
     * @param str
     * @return String
     */
    public static String exChange(String str) {
        StringBuffer sb = new StringBuffer();
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
                // else if(Character.isLowerCase(c)){
                // sb.append(Character.toUpperCase(c));
                // }
            }
        }

        return sb.toString();
    }

    /**
     * 把一个字符串中的大写转为小写，小写转换为大写：思路2
     *
     * @param str
     * @return String
     */
    public static String exChange2(String str) {
        for (int i = 0; i < str.length(); i++) {
            // 如果是小写
            if (str.substring(i, i + 1).equals(str.substring(i, i + 1).toLowerCase())) {
                // str.substring(i, i+1).toUpperCase();
            } else {
                str.substring(i, i + 1).toLowerCase();
            }
        }
        return str;
    }

    /**
     * 隐藏手机部分号码
     *
     * @param phone
     * @return
     */
    public static String hidePhoneNo(String phone) {
        if (!isNullOrEmpty(phone) && phone.length() >= 11) {
            return phone != "" ? phone.substring(0, 3) + "****" + phone.substring(7, 11) : phone;
        }
        return phone;
    }

    /**
     * 隐藏身份证件部分号码
     *
     * @param idCardNo
     * @return
     */
    public static String hideIdCardNo(String idCardNo) {
        if (!isNullOrEmpty(idCardNo) && idCardNo.length() >= 18) {
            return idCardNo != "" ? idCardNo.substring(0, 4) + "**************" + idCardNo.substring(14, 18) : idCardNo;
        }
        return idCardNo;
    }



    /**
     * 格式化Map数据
     * @param message
     * @param className
     * @return
     */
    public static String format2MapStr(String message, String className){
        Log.i(TAG, TAG + "_@ ----className: " + className + "  -- message: " + message);
        Log.i(TAG, TAG + "_@ ---- message startsWith " + className + ": " + message.startsWith(className));
        String newMsg = message;
        if(null != message && null != className && className.length() > 0 && message.startsWith(className)){
            newMsg = message.substring(className.length(), message.length());
        }

        Log.i(TAG, TAG + "_***** ----json_map_msg: " + newMsg);

        return newMsg;
    }

    /**
     * 格式化JSON数据
     * @param message
     * @param className
     * @return
     */
    public static String format2JSONStr(String message, String className){
        //message = "{, type=group, targetId='10210715', latestText='看到群消息了', latestType=text, lastMsgDate=1468300800909, unReadMsgCnt=0, msgTableName='msg1224873808', targetAppkey=''}";
        String newMsg = message;
        if(null != message && null != className && className.length() > 0 && message.startsWith(className)){
            newMsg = message.substring(className.length(), message.length());
        }

        try {
            JSONObject json = new JSONObject(newMsg);
            newMsg = json.toString();
        } catch (JSONException e) {
            Log.i(TAG, TAG + "_plugin_android_json: *********** json format error");
        }
        Log.i(TAG, TAG + "_***** ----json_msg: " + newMsg);

        return newMsg;
    }

    /**
     * 格式化JSONArray数据
     * @param listStr
     * @return
     */
    public static String list2JSONArrayStr(String listStr){
        String newMsg = "";
        try {
            JSONArray json = new JSONArray(listStr);
            newMsg = json.toString();
        } catch (JSONException e) {
            Log.i(TAG, TAG + "_plugin_android_json: *********** json format error");
        }
        Log.i(TAG, TAG + "_***** ----json_array_msg: " + newMsg);

        return newMsg;
    }

//    /**
//     * String转换Map
//     *
//     * @param mapText
//     *            :需要转换的字符串
//     * @param KeySeparator
//     *            :字符串中的分隔符每一个key与value中的分割
//     * @param ElementSeparator
//     *            :字符串中每个元素的分割
//     * @return Map<?,?>
//     */
//    public static Map<String, Object> StringToMap(String mapText) {
//
//        if (mapText == null || mapText.equals("")) {
//            return null;
//        }
//        mapText = mapText.substring(1);
//
//        mapText = EspUtils.DecodeBase64(mapText);
//
//        Map<String, Object> map = new HashMap<String, Object>();
//        String[] text = mapText.split("\\" + SEP2); // 转换为数组
//        for (String str : text) {
//            String[] keyText = str.split(SEP1); // 转换key与value的数组
//            if (keyText.length < 1) {
//                continue;
//            }
//            String key = keyText[0]; // key
//            String value = keyText[1]; // value
//            if (value.charAt(0) == 'M') {
//                Map<?, ?> map1 = StringToMap(value);
//                map.put(key, map1);
//            } else if (value.charAt(0) == 'L') {
//                List<?> list = StringToList(value);
//                map.put(key, list);
//            } else {
//                map.put(key, value);
//            }
//        }
//        return map;
//    }

}

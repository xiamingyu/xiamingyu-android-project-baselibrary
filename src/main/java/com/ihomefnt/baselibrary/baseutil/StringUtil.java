/*
 * @(#)StringUtil.java 11-5-16 下午7:56 CopyRight 2011. All rights reserved
 */
package com.ihomefnt.baselibrary.baseutil;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import java.io.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kelvin Van
 * @version 1.0
 */
public final class StringUtil {

    private static final String TAG = "StringUtil";
    private static final String QUOTE = "\"";
    private static final String EMPTY_QUOTE_STR = QUOTE + QUOTE;
    /**
     * 手机号码 正则验证
     */
    private static final String NUMBER_REGEX = "(\\+86|86|0086)?(13[0-9]|15[0-35-9]|14[57]|18[02356789])\\d{8}";

    /**
     * @return
     */
    public static boolean checkLength(String phoneNumber) {
        if (!isNullOrEmpty(phoneNumber)) {
            if (phoneNumber.length() > 10) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为null
     *
     * @param str str
     * @return true or false
     */
    public static boolean isNull(String str) {
        return str == null;
    }

    /**
     * @param str str
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() < 1;
    }

    /**
     * 判断str1和str2是否相同
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equals(String str1, String str2) {
        return str1 == str2 || str1 != null && str1.equals(str2);
    }

    /**
     * @param str1
     * @param str2
     */
    public static boolean equalsWithEmpty(String str1, String str2) {
        if (isNullOrEmpty(str1) && isNullOrEmpty(str2)) {
            return true;
        }

        return equals(str1,
                str2);
    }

    /**
     * 判断str1和str2是否相同(不区分大小写)
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null && str1.equalsIgnoreCase(str2);
    }

    /**
     * 判断字符串str1是否包含字符串str2
     *
     * @param str1 源字符串
     * @return true源字符串包含指定字符串，false源字符串不包含指定字符串
     */
    public static boolean contains(String str1, String str2) {
        return str1 != null && str1.contains(str2);
    }

    /**
     * 判断字符串是否为空，为空则返回一个空值，不为空则返回原字符串
     *
     * @param str 待判断字符串
     */
    public static String getString(String str) {
        return str == null ? "" : str;
    }

    /**
     * @param s 源字符串
     */
    public static String unquote(String s, String quote) {
        if (s != null && quote != null) {
            if (s.startsWith(quote) && s.endsWith(quote)) {
                return s.substring(1,
                        s.length() - quote.length());
            }
        }
        return s;
    }

    /**
     * 将输入流转化成字符串
     *
     * @return 转化后的字符
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * @return 过滤了HTML标签的字符串
     */
    public static String filterHtmlTag(String inputString) {
        String htmlStr = inputString;
        String textStr = "";
        Pattern pScript;
        Matcher mScript;
        Pattern pStyle;
        Matcher mStyle;
        Pattern pHtml;
        Matcher mHtml;

        try {
            // 定义script的正则表达式
            String regExScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?/[\\s]*?script[\\s]*?>";
            // 定义style的正则表达式
            String regExStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?/[\\s]*?style[\\s]*?>";
            // 定义HTML标签的正则表达式
            String regExHtml = "<[^>\"]+>";

            pScript = Pattern.compile(regExScript,
                    Pattern.CASE_INSENSITIVE);
            mScript = pScript.matcher(htmlStr);
            // 过滤script标签
            htmlStr = mScript.replaceAll("");

            pStyle = Pattern.compile(regExStyle,
                    Pattern.CASE_INSENSITIVE);
            mStyle = pStyle.matcher(htmlStr);
            // 过滤style标签
            htmlStr = mStyle.replaceAll("");

            pHtml = Pattern.compile(regExHtml,
                    Pattern.CASE_INSENSITIVE);
            mHtml = pHtml.matcher(htmlStr);
            // 过滤html标签
            htmlStr = mHtml.replaceAll("");

            textStr = htmlStr;

        } catch (Exception e) {
            LoggerUtil.e(TAG,
                    "filterHtmlTag failed: " + e.getCause());
        }

        return textStr;
    }

    /**
     * 将字符串数组转化为用逗号连接的字符串
     */
    public static String arrayToString(String[] values) {
        StringBuilder result = new StringBuilder();
        String retStr = "";
        if (values != null) {
            if (values.length > 0) {
                for (String value : values) {
                    result.append(value).append(",");
                }

                retStr = result.substring(0,
                        result.length() - 1).toString();
            }
        }

        return retStr;
    }

    /**
     * 验证字符串是否符合email格式
     * <p/>
     * 如果不为空或Null则验证其是否符合email格式，符合则返回true,不符合则返回false
     */
    public static boolean isEmail(String email) {
        boolean flag = true;
        if (!isNullOrEmpty(email)) {
            flag = email
                    .matches("^(\\w+[\\.\\-]?)*?\\w+@(\\w+[\\.\\-]?)*?\\w+\\.\\w+$");

            return flag;
        }
        return flag;
    }

    /**
     * @return 不是数字返回false，是数字就返回true
     */
    public static boolean isNumeric(String str) {
        // 先判断空字符串，否则会判断出错，因为下面的表达式不能正确识别空字符串
        if (isNullOrEmpty(str)) {
            return false;
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 验证字符串是否符合手机号格式
     *
     * @return 不是手机号返回false，是手机号就返回true
     */
    public static boolean isMobile(String str) {
        Pattern pattern = Pattern
                .compile("(13[0-9]|15[0-35-9]|14[57]|18[0-9]|17[678])\\d{8}|(1700|1705|1709)\\d{7}");
        Matcher isMobile = pattern.matcher(str);
        return isMobile.matches();
    }

    /**
     * 替换字符串中特殊字符
     *
     * @param strData 源字符串
     * @return 替换了特殊字符后的字符串
     */
    public static String encodeString(String strData) {
        if (strData == null) {
            return "";
        }
        strData = strData.replaceAll("&",
                "&amp;");
        strData = strData.replaceAll("<",
                "&lt;");
        strData = strData.replaceAll(">",
                "&gt;");
        strData = strData.replaceAll("'",
                "&apos;");
        strData = strData.replaceAll("\"",
                "&quot;");
        return strData;
    }

    /**
     * [功能详细描述]
     *
     * @param strData 给定的字符串
     */
    public static String encodeStrngOnly(String strData) {
        if (strData == null) {
            return "";
        }
        strData = strData.replaceAll("&",
                "&amp;");
        return strData;
    }

    /**
     * @param str str
     */
    public static String getQuoteString(String str) {
        return isNull(str) ? EMPTY_QUOTE_STR : (QUOTE + str + QUOTE);
    }

    /**
     * 还原字符串中特殊字符
     *
     * @param strData strData
     */
    public static String decodeString(String strData) {
        if (strData == null) {
            return "";
        }
        strData = strData.replaceAll("&lt;",
                "<");
        strData = strData.replaceAll("&gt;",
                ">");
        strData = strData.replaceAll("&apos;",
                "'");
        strData = strData.replaceAll("&quot;",
                "\"");
        strData = strData.replaceAll("&amp;",
                "&");

        return strData;
    }

    /**
     */
    public static String generateXml(Map<String, Object> map) {

        StringBuffer xml = new StringBuffer();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<root>");
        if (map != null) {
            Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                String key = entry.getKey();
                xml.append("<");
                xml.append(key);
                xml.append(">");
                xml.append(entry.getValue());
                xml.append("</");
                xml.append(key);
                xml.append(">");
            }
        }
        xml.append("</root>");
        return xml.toString();
    }

    /**
     * @param values key、value依次排列
     */
    public static String generateXml(String... values) {
        StringBuffer xml = new StringBuffer();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<root>");
        if (values != null) {
            int size = values.length >> 1;
            for (int i = 0; i < size; i++) {
                xml.append("<");
                xml.append(values[i << 1]);
                xml.append(">");
                xml.append(values[(i << 1) + 1]);
                xml.append("</");
                xml.append(values[i << 1]);
                xml.append(">");
            }
        }
        xml.append("</root>");
        return xml.toString();
    }

    public static String fixPortalPhoneNumber(String number) {
        if (StringUtil.isNullOrEmpty(number)) {
            return number;
        }

        String retPhoneNumber = number.trim();

        // 确定是否是手机号码，只保留纯号码
        if (isMobile(retPhoneNumber)) {
            if (retPhoneNumber.startsWith("+86")) {
                retPhoneNumber = retPhoneNumber.substring(3);
            } else if (retPhoneNumber.startsWith("86")) {
                retPhoneNumber = retPhoneNumber.substring(2);
            } else if (retPhoneNumber.startsWith("0086")) {
                retPhoneNumber = retPhoneNumber.substring(4);
            }
        }

        return retPhoneNumber;
    }

    public static String fixAasPhoneNumber(String number) {
        if (StringUtil.isNullOrEmpty(number)) {
            return number;
        }

        number = number.trim();
        if (isMobile(number)) {
            if (number.length() == 11) {
                number = "+86" + number;
            } else if (number.length() == 13 && number.startsWith("86")) {
                number = "+" + number;
            } else if (number.length() == 15 && number.startsWith("0086")) {
                number = "+" + number.substring(2);
            }
        }
        return number;
    }

    /**
     * @param list
     * @param splitStr
     * @return
     */
    public static String parseListToString(List<String> list, String splitStr) {
        if (list != null && list.size() > 0) {
            StringBuilder result = new StringBuilder();
            if (StringUtil.isNullOrEmpty(splitStr)) {
                splitStr = "|";
            }
            for (int i = 0; i < list.size(); i++) {
                result.append(list.get(i));
                result.append(splitStr);
            }
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        } else {
            return null;
        }

    }

    /**
     * @param str
     * @param splitStr
     * @return
     */
    public static List<String> parseStringToList(String str, String splitStr) {
        List<String> list = null;
        if (!StringUtil.isNullOrEmpty(str)) {
            if (StringUtil.isNullOrEmpty(splitStr)) {
                splitStr = "\\|";
            }
            String[] temp = str.split(splitStr);
            if (temp != null && temp.length > 0) {
                list = new ArrayList<String>();
                for (int i = 0; i < temp.length; i++) {
                    list.add(temp[i]);
                }
            }

        }
        return list;
    }

    /**
     * 去掉url中多余的斜杠
     *
     * @param url
     * @return
     */
    public static String fixUrl(String url) {
        if (url == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(url);
        for (int i = stringBuffer.indexOf("//",
                stringBuffer.indexOf("//") + 2); i != -1; i = stringBuffer
                .indexOf("//",
                        i + 1)) {
            stringBuffer.deleteCharAt(i);
        }
        return stringBuffer.toString();
    }

    /**
     * @param string
     * @return
     */
    public static int count2BytesChar(String string) {
        int count = 0;
        if (string != null) {
            for (char c : string.toCharArray()) {
                count++;
                if (isChinese(c)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 判断字符串中是否包含中文 <BR>
     * [功能详细描述] [added by 杨凡]
     *
     * @param str
     * @return
     */
    public static boolean hasChinese(String str) {
        boolean hasChinese = false;
        if (str != null) {
            for (char c : str.toCharArray()) {
                if (isChinese(c)) {
                    hasChinese = true;
                    break;
                }
            }
        }
        return hasChinese;
    }

    /**
     * [功能详细描述] [added by 杨凡]
     *
     * @param src        源字符串
     * @param charLength
     */
    public static String subString(String src, int charLength) {
        if (src != null) {
            int i = 0;
            for (char c : src.toCharArray()) {
                i++;
                charLength--;
                if (isChinese(c)) {
                    charLength--;
                }
                if (charLength <= 0) {
                    if (charLength < 0) {
                        i--;
                    }
                    break;
                }
            }
            return src.substring(0,
                    i);
        }
        return src;
    }

    /**
     * 对字符串进行截取, 超过则以...结束
     *
     * @param originStr 原字符串
     */
    public static String trim(String originStr, int maxCharLength) {
        int index = 0;
        int originLen = originStr.length();
        for (int i = 0; index < originLen && i < maxCharLength; i++) {
            if (isChinese(originStr.charAt(index++))) {
                i++;
            }
        }
        if (index < originLen) {
            return originStr.substring(0,
                    index) + "...";
        } else {
            return originStr;
        }
    }

    /**
     * @param c char
     * @return 是中文字符返回true，反之false
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;

    }

    /**
     * 密码强度
     *
     * @param password
     * @return 密码强度1：低 2：中 3：高
     */
    public static final int checkStrong(String password) {
        boolean num = false;
        boolean lowerCase = false;
        boolean upperCase = false;
        boolean other = false;

        int fourMode = 0;

        for (int i = 0; i < password.length(); i++) {
            if (password.codePointAt(i) >= 48 && password.codePointAt(i) <= 57) {
                num = true;
            } else if (password.codePointAt(i) >= 97
                    && password.codePointAt(i) <= 122) {
                lowerCase = true;
            } else if (password.codePointAt(i) >= 65
                    && password.codePointAt(i) <= 90) {
                upperCase = true;
            }
            // 特殊字符
            else {
                other = true;
            }
        }

        if (num) {
            fourMode++;
        }

        if (lowerCase) {
            fourMode++;
        }

        if (upperCase) {
            fourMode++;
        }

        if (other) {
            fourMode++;
        }

        if (fourMode == 1) {
            return 1;
        } else if (fourMode == 2) {
            return 2;
        } else if (fourMode >= 3) {
            return 3;
        }
        // 正常情况下不会出现该判断
        else {
            return 3;
        }
    }

    /**
     * @param min 范围下限
     * @param max 范围上限
     */
    public static String createRandomString(int min, int max) {
        StringBuffer strB = new StringBuffer();
        Random random = new Random();
        int lenght = min;
        if (max > min) {
            lenght += random.nextInt(max - min + 1);
        }
        for (int i = 0; i < lenght; i++) {
            strB.append((char) (97 + random.nextInt(26)));
        }
        return strB.toString();
    }

    /**
     * [用于获取字符串中字符的个数]<BR>
     * [功能详细描述]
     *
     * @param content 文本内容
     */
    public static int getStringLeng(String content) {
        double num = 0;

        if (isNullOrEmpty(content)) {
            return 0;
        }

        for (int i = 0; i < content.length(); i++) {
            if (StringUtil.isChinese(content.charAt(i))) {
                num += 2;
            } else {
                num += 1;
            }
        }
        return (int) Math.ceil(num / 2.0);
    }

    /**
     * [用于获取字符串中字符的个数]<BR>
     * [功能详细描述]
     *
     * @param content 文本内容
     */
    public static int getCharLeng(String content) {
        int num = 0;
        if (isNullOrEmpty(content)) {
            return num;
        }

        for (int i = 0; i < content.length(); i++) {
            if (StringUtil.isChinese(content.charAt(i))) {
                num += 2;
            } else {
                num += 1;
            }
        }
        return num;
    }

    /**
     * @param length 字符长度
     */
    public static String getString(String content, int length) {
        double num = 0;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < content.length(); i++) {
            if (StringUtil.isChinese(content.charAt(i))) {
                num += 2;
            } else {
                num += 1;
            }
            stringBuffer.append(content.charAt(i));
            if (num >= length) {
                break;
            }
        }
        return stringBuffer.toString();
    }

    public static String append(Object... args) {
        if (args == null || args.length < 1) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (Object arg : args) {
            buf.append(arg);
        }
        return buf.toString();
    }

    public static boolean startsWith(String str, String prefix) {
        return str != null && str.startsWith(prefix);
    }

    public static boolean endsWith(String str, String suffix) {
        return str != null && str.endsWith(suffix);
    }

    /**
     * 判断是否是电话号
     *
     * @return true 是电话号，false不是电话号码
     */
    public static boolean isPhoneNumber(String resetInfo) {
        return Pattern.matches(NUMBER_REGEX,
                resetInfo);
    }

    /**
     * 截取字符
     *
     * @param str       要截取的字符
     * @param encoding  编码
     * @param maxLength 字节长度
     * @return 截取后的字符
     */
    public static String trim(String str, String encoding, int maxLength) {
        if (str == null || encoding == null || maxLength < 1) {
            return str;
        }
        if (("UTF-8".equalsIgnoreCase(encoding) && maxLength > str.length() * 3)
                || (("GBK".equalsIgnoreCase(encoding) || "GB2312"
                .equalsIgnoreCase(encoding)) && maxLength > str.length() * 2)) {
            return str;
        }
        if (maxLength < str.length()) {
            str = str.substring(0,
                    maxLength);
        }
        try {
            byte[] bytes = str.getBytes(encoding);
            int end = bytes.length;
            if (maxLength < end) {
                end = maxLength;
            }
            String newStr = new String(bytes,
                    0,
                    end,
                    encoding);
            if (newStr.charAt(newStr.length() - 1) != str.charAt(newStr
                    .length() - 1)) {
                newStr = newStr.substring(0,
                        newStr.length() - 1);
            }
            return newStr;
        } catch (UnsupportedEncodingException e) {
            LoggerUtil.w(TAG,
                    "trim error",
                    e);
            return str;
        }
    }

    private static int getTimes(String all, String choice) {
        char array_char[];
        int total = 0;
        array_char = all.toCharArray();
        for (int i = 0; i < array_char.length; i++) {
            if (array_char[i] == (choice.charAt(0))) {
                total++;
            }
        }
        return total;
    }

    /**
     * MD5加密
     *
     * @param str 要截取的字符
     * @return 截取后的字符串s
     */
    public static String getMD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        if (str == null) {
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @param text
     * @param start
     * @param color
     * @return
     */
    public static SpannableStringBuilder setSpan(Context context, String text,
                                                 int start, int end, int color, int fontsize) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(color),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        style.setSpan(new AbsoluteSizeSpan(fontsize,
                        true),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        return style;
    }

    public static SpannableStringBuilder setSpan(Context context, String text,
                                                 int color, int start, int end) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(context
                        .getResources()
                        .getColor(color)),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style;
    }

    public static SpannableStringBuilder setSpan(Context context, int bgColor, String text,
                                                 int color, int start, int end) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(context
                        .getResources()
                        .getColor(color)),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new BackgroundColorSpan(bgColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    public static String setNeededStringSpan(Context context, String string, int color, int bgColor) {
        int i = string.indexOf("{");
        String s = string.replace("{", "");
        int j = s.indexOf("}");
        String ss = s.replace("}", "");
        setSpan(context, bgColor, ss, color, i, j);
        return ss;
    }

    public static String StringFormat(String string) {
        String s = string.replace("{", "").replace("}", "");
        return s;
    }

    public static Object doubleFormat(Double d) {
        if (d.compareTo(Math.ceil(d)) == 0) {
            return d.intValue();
        } else {
            return d;
        }
    }

    public static String stack2String(Stack stack) {
        Object[] objects = stack.toArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < objects.length; i++) {
            sb.append(objects[i]);
        }
        return sb.toString();
    }


}
